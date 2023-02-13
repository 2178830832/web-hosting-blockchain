package pers.yujie.dashboard.utils;

import static org.junit.jupiter.api.Assertions.*;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.PemUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.fasterxml.jackson.databind.ser.Serializers.Base;
import java.io.File;
import java.security.KeyPair;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import sun.security.pkcs.PKCS8Key;

class EncryptUtilTest {

  @Test
  void testEnc() {
//    KeyPair pair = SecureUtil.generateKeyPair("RSA", RSAKeyGenParameterSpec.PKCS5Padding);
//    IoUtil.write(FileUtil.getOutputStream(System.getProperty("user.dir") + File.separator + "private"),
//        true, pair.getPrivate().getEncoded());
//    IoUtil.write(FileUtil.getOutputStream(System.getProperty("user.dir") + File.separator + "public"),
//        true, pair.getPublic().getEncoded());

//    RSA rsa = new RSA(Base64.encode(pair.getPrivate().getEncoded()),
//        Base64.encode(pair.getPublic().getEncoded()));
//    RSA rsa = new RSA(IoUtil.readBytes(FileUtil.getInputStream(
//        ResourceUtil.getResource("encryption/private").getPath()), true),
//        IoUtil.readBytes(FileUtil.getInputStream(
//            ResourceUtil.getResource("encryption/public").getPath()), true));
    RSA rsa = new RSA(PemUtil.readPemPrivateKey(FileUtil.getInputStream(
        ResourceUtil.getResource("encryption/private").getPath())),
        PemUtil.readPemPublicKey(FileUtil.getInputStream(
            ResourceUtil.getResource("encryption/public").getPath())));

//公钥加密，私钥解密
    byte[] encrypt = rsa.encrypt("test", KeyType.PublicKey);
    System.out.println(Base64.encode(encrypt));
    byte[] decrypt = rsa.decrypt(Base64.decode(Base64.encode(encrypt)), KeyType.PrivateKey);

//Junit单元测试
    assertEquals("test", StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));

//私钥加密，公钥解密
    String encrypt2 = rsa.encryptHex("test", CharsetUtil.CHARSET_UTF_8, KeyType.PrivateKey);
    String decrypt2 = rsa.decryptStr(encrypt2, KeyType.PublicKey);

//Junit单元测试
    System.out.println(decrypt2);
    assertEquals("test", decrypt2);
  }

}