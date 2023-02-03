package pers.yujie.dashboard.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Node;

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
    Node cluster = new Node(BigInteger.ONE);
    JSONObject obj = JSONUtil.parseObj(cluster);
    obj.set("totalSpace", 1);
    System.out.println(obj);
  }


}