package pers.yujie.dashboard.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pers.yujie.dashboard.service.TestService;

@Slf4j
@Service
public class TestServiceImpl implements TestService {
  List<Double> results = new ArrayList<>();
  private final JSONObject resultObj = JSONUtil.createObj();
  int i = 0;
  @Override
  public JSONObject getTestResult() {
    if (results.size() < 1) {
      return resultObj;
    }
    DoubleSummaryStatistics dss = results.stream().collect(Collectors.summarizingDouble(n-> n));
    resultObj.set("avg", dss.getAverage());
    resultObj.set("max", dss.getMax());
    resultObj.set("min", dss.getMin());
    log.info("Test results: " + dss);
    return resultObj;
  }

  @Override
  public void appendTestResult(double result) {
    results.add(result);
  }

  @Override
  public String startTest(JSONObject params) {
    String url = params.getStr("url");
    String mode = params.getStr("mode");

    if (url == null) {
      return "Unexpected Json format";
    }
    try {
      url = URLUtil.normalize(url);
      new URL(url);
    } catch (MalformedURLException e) {
      return "Invalid URL format";
    }

    Map<String, String> map = new HashMap<>();
    map.put("url", url);
    map.put("mode", mode);
    RestTemplate client = new RestTemplate();
//    String body = client.getForEntity(url, String.class, map).getBody();
//    log.info(body);
    results.add((double) i++);
    return "";
  }

  @Override
  public void stopTest() {
    i = 0;
    results = new ArrayList<>();
  }
}
