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
import pers.yujie.dashboard.common.Encrypted;
import pers.yujie.dashboard.service.TestService;
import pers.yujie.dashboard.service.WebsiteService;

/**
 * Contains the {@link Controller} related to the controller page. The business logic is in
 * {@link WebsiteService}.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 25/01/2023
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

  @Resource
  private TestService testService;

  /**
   *
   * @param matrix
   * @return
   */
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

  @Encrypted
  @GetMapping("/result")
  public ResponseEntity<String> getTestResult() {
    return new ResponseEntity<>(testService.getTestResult().toString(), HttpStatus.OK);
  }

  @Encrypted
  @PostMapping("/param")
  public ResponseEntity<String> setTestParams(@RequestBody JSONObject params) {
    String message = testService.startTest(params);
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  @Encrypted
  @GetMapping("/stop")
  public ResponseEntity<String> stopTest() {
    testService.stopTest();
    return new ResponseEntity<>("success", HttpStatus.OK);
  }
}
