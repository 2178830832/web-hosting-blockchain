package pers.yujie.dashboard.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

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
    DoubleSummaryStatistics lss = res.stream().collect(Collectors.summarizingDouble(n-> n));
    System.out.println(lss.getAverage());
    System.out.println(lss.getMax());
    System.out.println(lss.getMin());
  }



}