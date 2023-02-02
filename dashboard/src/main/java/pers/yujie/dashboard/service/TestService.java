package pers.yujie.dashboard.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public interface TestService {

  JSONObject getTestResult();

  void appendTestResult(double result);

  String startTest(JSONObject params);

  void stopTest();
}
