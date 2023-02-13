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
import java.security.Key;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EncryptUtil {

  private static SymmetricCrypto aes;
  private static RSA rsa;

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

  public static String encryptAES(String content) {
    return aes.encryptHex(content);
  }

  public static String decryptAES(String content) {
    return aes.decryptStr(content, CharsetUtil.UTF_8);
  }

  public static String rsaEncryptPublic(String content) {
    return rsa.encryptHex(content, CharsetUtil.UTF_8, KeyType.PublicKey);
  }

  public static String decryptPrivateRSA(String content) {
    return rsa.decryptStr(content, KeyType.PrivateKey);
  }

  public static String signSHA256withRSA(String content) {
    return Base64.encode(SecureUtil.sign(SignAlgorithm.SHA256withRSA, rsa.getPrivateKeyBase64(),
        rsa.getPublicKeyBase64()).sign(content));
  }

  public static String rsaEncryptPrivate(String content) {
    return rsa.encryptBase64(content, KeyType.PrivateKey);
//    return Base64.encode(rsa.encrypt(content, KeyType.PrivateKey));
//    return rsa.encryptHex(content, KeyType.PrivateKey);
  }


  public static String rsaDecryptPublic(String content) {
    return rsa.decryptStr(content, KeyType.PublicKey);
  }

  public static String getRSAPublicKey() {
    return Arrays.toString(rsa.getPublicKey().getEncoded());
  }
}