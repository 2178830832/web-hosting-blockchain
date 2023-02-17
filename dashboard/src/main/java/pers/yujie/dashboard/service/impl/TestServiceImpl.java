package pers.yujie.dashboard.service.impl;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import pers.yujie.dashboard.common.Constants;
import pers.yujie.dashboard.service.TestService;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

  List<Double> results = new ArrayList<>();
  private static final JSONObject resultObj = JSONUtil.createObj();
  private static boolean isRunning = false;

  @Override
  public JSONObject getTestResult() {
    if (results.size() < 1) {
      return resultObj;
    }
    DoubleSummaryStatistics dss = results.stream().collect(Collectors.summarizingDouble(n -> n));
    resultObj.set("count", dss.getCount());
    resultObj.set("avg", dss.getAverage());
    resultObj.set("max", dss.getMax());
    resultObj.set("min", dss.getMin());
    log.info("Test results: " + resultObj);
    return resultObj;
  }

  @Override
  public void appendTestResult(double result) {
    if (isRunning) {
      results.add(result);
    }
  }

  @Override
  public String startTest(JSONObject params) {
    isRunning = true;
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
    ResponseEntity<String> response;
    try {
      response = client.getForEntity(Constants.TESTER_SERVER, String.class, map);
    } catch (HttpServerErrorException e) {
      log.warn(e.getMessage());
      return e.getMessage();
    }

    if (response.getStatusCodeValue() == 200) {
      return "";
    }
    log.warn(response.getBody());
    return response.getBody();
  }

  @Override
  public void stopTest() {
    results = new ArrayList<>();
    isRunning = false;
  }
}
