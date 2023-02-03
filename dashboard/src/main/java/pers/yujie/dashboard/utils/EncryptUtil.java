package pers.yujie.dashboard.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EncryptUtil {

  private static SymmetricCrypto aes;

  static {
    try {
      byte[] key = IoUtil.readBytes(FileUtil.getInputStream(
          ResourceUtil.getResource("encryption/key").getPath()), true);
      aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
    } catch (NullPointerException e) {
      log.error("Unable to locate the encryption key");
    }
  }

  public static String aesEncrypt(String content) {
    return aes.encryptHex(content);
  }

  public static String aesDecrypt(String content) {
    return aes.decryptStr(content, CharsetUtil.UTF_8);
  }
}