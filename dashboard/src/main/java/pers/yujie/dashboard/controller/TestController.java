package pers.yujie.dashboard.controller;

import cn.hutool.json.JSONObject;
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

/**
 * Contains the {@link Controller} related to the test page. The business logic is in {@link
 * TestService}.
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
   * Receive results from the test server.
   *
   * @param matrix {@link JSONObject} containing TTFB
   * @return {@link HttpStatus#OK} and success
   */
  @ResponseBody
  @PostMapping("/receive")
  public ResponseEntity<String> receiveResult(@RequestBody JSONObject matrix) {
    if (matrix.getStr("name").equalsIgnoreCase("TTFB")) {
      double result = matrix.getDouble("value");
      if (result > 0) {
        testService.appendTestResult(result);
        log.info("Received TTFB result: " + result);
      }
    }
    return new ResponseEntity<>("success", HttpStatus.OK);
  }

  /**
   * Get the current stored test results.
   *
   * @return a string representing the results
   */
  @Encrypted
  @GetMapping("/result")
  public ResponseEntity<String> getTestResult() {
    return new ResponseEntity<>(testService.getTestResult().toString(), HttpStatus.OK);
  }

  /**
   * Set custom test parameters.
   *
   * @param params {@link JSONObject} containing necessary test parameters
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} otherwise
   */
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

  /**
   * Stop a running test.
   *
   * @return {@link HttpStatus#OK} and success
   */
  @Encrypted
  @GetMapping("/stop")
  public ResponseEntity<String> stopTest() {
    testService.stopTest();
    return new ResponseEntity<>("success", HttpStatus.OK);
  }
}
