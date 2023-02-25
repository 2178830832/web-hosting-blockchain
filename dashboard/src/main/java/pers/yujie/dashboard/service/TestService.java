package pers.yujie.dashboard.service;

import cn.hutool.json.JSONObject;

/**
 * This is the interface providing test services for outer usages.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see pers.yujie.dashboard.service.impl.TestServiceImpl
 * @since 17/01/2023
 */
public interface TestService {

  JSONObject getTestResult();

  void appendTestResult(double result);

  String startTest(JSONObject params);

  void stopTest();
}
