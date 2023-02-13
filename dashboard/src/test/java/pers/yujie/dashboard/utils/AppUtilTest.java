package pers.yujie.dashboard.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.PemUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.sun.jndi.toolkit.url.UrlUtil;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import pers.yujie.dashboard.entity.Block;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.entity.Website;

class AppUtilTest {

  @Test
  void testPartition() {
    int size = 3;
    List<Integer> stringList = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      stringList.add(i + 1);
    }
    List<List<Integer>> lists = new ArrayList<>();
    double i1 = 0.9;
    double i2 = 0.5;
    double i3 = 0.3;
    double x = i1 + i2 + i3;
    List<Double> list = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.00");
    double x1 = Double.parseDouble(df.format(i1 / x));
    double x2 = Double.parseDouble(df.format(i2 / x));
//    double x3 = 1.00 - x1 - x2;
    int p1 = Math.round((float) (stringList.size() * x1));
    int p2 = Math.round((float) (stringList.size() * x2));
    System.out.println(x1 + " " + x2);
    List<Integer> list1 = stringList.subList(0, p1);
    stringList = stringList.subList(p1, stringList.size());
    List<Integer> list2 = stringList.subList(0, p2);
    stringList = stringList.subList(p2, stringList.size());
    List<Integer> list3 = stringList;
    lists.add(list1);
    lists.add(list2);
    lists.add(list3);

    System.out.println(lists);
  }

  @Test
  void testResource() throws IOException {
    List<Double> res = new ArrayList<>();
    res.add(1.1);
    System.out.println(JSONUtil.parse(res));
//    System.out.println(res.stream().mapToDouble(n-> n).sum());
    DoubleSummaryStatistics lss = res.stream().collect(Collectors.summarizingDouble(n -> n));
    System.out.println(lss.getAverage());
    System.out.println(lss.getMax());
    System.out.println(lss.getMin());
  }

  @Test
  void testSec() {
    String context = "this";
    byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
    System.out.println(Arrays.toString(key));
    SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
    String encryptHex = aes.encryptHex(context);
    System.out.println(context.equals(aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8)));

    IoUtil.write(FileUtil.getOutputStream(System.getProperty("user.dir") + File.separator + "test"),
        true, key);
//    FileWriter writer = new FileWriter(System.getProperty("user.dir") + File.separator + "test");
//    writer.write(Arrays.toString(key));
    FileReader fileReader = new FileReader(
        System.getProperty("user.dir") + File.separator + "test");
    byte[] result = IoUtil.readBytes(
        FileUtil.getInputStream(System.getProperty("user.dir") + File.separator + "test"), true);
    System.out.println(Arrays.toString(result));
    System.out.println(Arrays.equals(result, key));
  }

  @Test
  void testSp() {
    String str = ResourceUtil.getResource("encryption/key.text").getPath();
    byte[] result = IoUtil.readBytes(
        FileUtil.getInputStream(str), true);
    System.out.println(Arrays.toString(result));
  }

  @Test
  void tests() {
    Node node = new Node(BigInteger.ZERO);
    node.getBlockList().add(new Block("", BigInteger.ZERO));
    System.out.println(node.getBlockList().get(0));

    JSONObject nodeo = JSONUtil.parseObj(node);
    List<Block> nodeBlockList = nodeo.getJSONArray("blockList").toList(Block.class);
    System.out.println(nodeo.getJSONArray("blockList").contains(new Block("", BigInteger.ZERO)));
  }

  @Test
  void testcase() {
    JSONArray location = new JSONArray();
    location.add(new BigInteger("0"));
    location.add(new BigInteger("1"));
    location.add(new BigInteger("2"));
    System.out.println(location);
    Website website = new Website(BigInteger.ZERO);
    website.setLocation(location);
    JSONObject webObj = JSONUtil.parseObj(website);
    System.out.println(webObj.getJSONArray("location").containsAll(location));
    location = new JSONArray();
    location.add(new BigInteger("0"));
    location.add(new BigInteger("1"));
    System.out.println(webObj);
    System.out.println(location);
    webObj.getJSONArray("location").removeAll(location);
    System.out.println(webObj);
  }

  @Test
  void testss() {
    byte[] key = IoUtil.readBytes(FileUtil.getInputStream(
        ResourceUtil.getResource("encryption/key").getPath()), true);

    RSA rsa = new RSA(PemUtil.readPemPrivateKey(FileUtil.getInputStream(
        ResourceUtil.getResource("encryption/private").getPath())),
        PemUtil.readPemPublicKey(FileUtil.getInputStream(
            ResourceUtil.getResource("encryption/public").getPath())));
    Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA, rsa.getPrivateKeyBase64(),
        rsa.getPublicKeyBase64());
    String str= Base64.encode(sign.sign("content"));
    System.out.println(sign.verify("content".getBytes(StandardCharsets.UTF_8), Base64.decode(str)));
  }

}