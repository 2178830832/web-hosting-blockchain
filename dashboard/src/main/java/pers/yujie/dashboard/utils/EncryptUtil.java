package pers.yujie.dashboard.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.crypto.PemUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
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

  // Initialise the AES and RSA instances by reading the keys from local files.
  static {
    try {
      byte[] key = IoUtil.readBytes(FileUtil.getInputStream(
          ResourceUtil.getResource("encryption/key").getPath()), true);
      aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

      rsa = new RSA(PemUtil.readPemPrivateKey(FileUtil.getInputStream(
          ResourceUtil.getResource("encryption/private").getPath())),
          PemUtil.readPemPublicKey(FileUtil.getInputStream(
              ResourceUtil.getResource("encryption/public").getPath())));
    } catch (NullPointerException e) {
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
  public static String rsaEncryptPublic(String content) {
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
   * SHA256 digital sign.
   *
   * @param content {@link String} to be signed
   * @return signed content
   */
  public static String signSHA256withRSA(String content) {
    return Base64.encode(SecureUtil.sign(SignAlgorithm.SHA256withRSA, rsa.getPrivateKeyBase64(),
        rsa.getPublicKeyBase64()).sign(content));
  }
}