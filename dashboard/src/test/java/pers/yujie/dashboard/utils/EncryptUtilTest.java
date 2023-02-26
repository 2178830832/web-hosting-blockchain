package pers.yujie.dashboard.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EncryptUtilTest {

  static final String content = "testAES";

  @Test
  void testEncryptAES() {
    String encryptedStr = EncryptUtil.encryptAES(content);
    String decryptedStr = EncryptUtil.decryptAES(encryptedStr);
    assertEquals(content, decryptedStr);
  }

  @Test
  void testEncryptRSA() {
    String encryptedStr = EncryptUtil.encryptPublicRSA(content);
    String decryptedStr = EncryptUtil.decryptPrivateRSA(encryptedStr);
    assertEquals(content, decryptedStr);
  }

  @Test
  void testSignSHA256() {
    String encryptedStr = EncryptUtil.signSHA256withRSA(content);
    assertTrue(EncryptUtil.verifySHA256withRSA(content, encryptedStr));
  }
}