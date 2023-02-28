package pers.yujie.dashboard.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.yujie.dashboard.service.TestService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestServiceImplTest {

  static final String URL = "https://template-8gf1js6t49b08525-1316531086.tcloudbaseapp.com/text.html";
  @Autowired
  TestService testService;

  @Test
  @Order(1)
  void testStartTestChrome() {
    JSONObject params = JSONUtil.createObj();
    params.set("url", URL);
    params.set("mode", "chrome");
    assertEquals(testService.startTest(params), "");
  }

  @Test
  @Order(2)
  void testAppendTestResult() {
    double result = 1.11;
    testService.appendTestResult(result);
    assertEquals(testService.getTestResult().getDouble("avg"), result);
  }

  @Test
  @Order(3)
  void testStopTest() {
    testService.stopTest();
    assertEquals(testService.getTestResult(), JSONUtil.createObj());
  }
}