package pers.yujie.dashboard.controller;

import cn.hutool.json.JSONObject;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.yujie.dashboard.service.TestService;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

  @Resource
  private TestService testService;

  @ResponseBody
  @PostMapping("/receive")
  public ResponseEntity<String> getRequest(@RequestBody JSONObject matrix) {
    if (matrix.getStr("name").equalsIgnoreCase("TTFB")) {
      double result = matrix.getDouble("value");
      if (result > 0) {
        testService.appendTestResult(result);
        log.info("Received TTFB result: " + result);
      }
    }
    return new ResponseEntity<>("success", HttpStatus.OK);
  }

  @GetMapping("/result")
  public ResponseEntity<String> getTestResult() {
    return new ResponseEntity<>(testService.getTestResult().toString(), HttpStatus.OK);
  }

  @PostMapping("/param")
  public ResponseEntity<String> setTestParams(@RequestBody JSONObject params) {
    String message = testService.startTest(params);
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/stop")
  public ResponseEntity<String> stopTest() {
    testService.stopTest();
    return new ResponseEntity<>("success", HttpStatus.OK);
  }
}
