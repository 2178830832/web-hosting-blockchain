package pers.yujie.dashboard.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.GlobalBouncyCastleProvider;
import cn.hutool.crypto.PemUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import io.netty.util.CharsetUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * This is a utility class responsible for functionalities related to encryption.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 12/01/2023
 */
@Component
@Slf4j
public class EncryptUtil {

  private static SymmetricCrypto aes;
  private static RSA rsa;
  private static Sign sign;

  // Initialise the AES and RSA instances by reading the keys from local files.
  static {
    // disable global BC to bypass digital authentication for Jar files
    GlobalBouncyCastleProvider.setUseBouncyCastle(false);
    // read encryption keys and create Crypto instances
    try {
      byte[] key = IoUtil.readBytes(
          new ClassPathResource("encryption/key").getInputStream(), true);
      aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

      rsa = new RSA(PemUtil.readPemPrivateKey(
          new ClassPathResource("encryption/private").getInputStream()),
          PemUtil.readPemPublicKey(
              new ClassPathResource("encryption/public").getInputStream()));

      sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, rsa.getPrivateKeyBase64(),
          rsa.getPublicKeyBase64());
    } catch (NullPointerException | IOException e) {
      log.error("Unable to locate the encryption key");
    }
  }

  /**
   * Symmetric AES encryption.
   *
   * @param content {@link String} to be encrypted
   * @return encrypted content
   */
  public static String encryptAES(String content) {
    return aes.encryptHex(content);
  }

  /**
   * Symmetric AES decryption.
   *
   * @param content {@link String} to be decrypted
   * @return decrypted content
   */
  public static String decryptAES(String content) {
    return aes.decryptStr(content, CharsetUtil.UTF_8);
  }

  /**
   * Asymmetric RSA encryption with public key.
   *
   * @param content {@link String} to be encrypted
   * @return encrypted content
   */
  public static String encryptPublicRSA(String content) {
    return rsa.encryptHex(content, CharsetUtil.UTF_8, KeyType.PublicKey);
  }

  /**
   * Asymmetric RSA decryption with private key.
   *
   * @param content {@link String} to be decrypted
   * @return decrypted content
   */
  public static String decryptPrivateRSA(String content) {
    return rsa.decryptStr(content, KeyType.PrivateKey);
  }

  /**
   * SHA256 digital sign with RSA.
   *
   * @param content {@link String} to be signed
   * @return signed content
   */
  public static String signSHA256withRSA(String content) {
    return Base64.encode(sign.sign(content));
  }

  /**
   * Verify the signature of a string using SHA256 with RSA.
   *
   * @param content original signature
   * @param signed  signed content
   * @return true if validate, false otherwise
   */
  public static boolean verifySHA256withRSA(String content, String signed) {
    return sign.verify(content.getBytes(StandardCharsets.UTF_8), Base64.decode(signed));
  }
}