package pers.yujie.dashboard.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.micrometer.core.lang.Nullable;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pers.yujie.dashboard.service.TestService;
import pers.yujie.dashboard.utils.EncryptUtil;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class TestControllerTest {

  static final String URL = "https://template-8gf1js6t49b08525-1316531086.tcloudbaseapp.com/text.html";
  @Autowired
  MockMvc mockMvc;

  @Test
  @Order(1)
  void testStartTestChrome() {
    JSONObject params = JSONUtil.createObj();
    params.set("url", URL);
    params.set("mode", "chrome");

    assertDoesNotThrow(() -> {
      String auth = mockMvc.perform(post("/test/param")
          .header("Authorization", setAuthentication("/test/param", params))
          .contentType(MediaType.APPLICATION_JSON)
          .content(params.toString()))
          .andExpect(status().isOk())
          .andReturn().getResponse().getHeader("Authorization");
      assertTrue(EncryptUtil.verifySHA256withRSA("WEB_CHAIN", auth));
    });
  }

  @Test
  @Order(2)
  void testReceiveResult() {
    JSONObject matrix = JSONUtil.createObj();
    matrix.set("name", "TTFB");
    matrix.set("value", 1000);

    assertDoesNotThrow(() -> {
      mockMvc.perform(post("/test/receive")
          .contentType(MediaType.APPLICATION_JSON)
          .content(matrix.toString()))
          .andExpect(status().isOk());
    });
  }

  @Test
  @Order(3)
  void testGetTestResult() {
    assertDoesNotThrow(() -> {
      mockMvc.perform(get("/test/result")
          .header("Authorization", setAuthentication("/test/result", null)))
          .andExpect(status().isOk());
    });
  }

  @Test
  @Order(4)
  void testStopTest() {
    assertDoesNotThrow(() -> {
      mockMvc.perform(get("/test/stop")
          .header("Authorization", setAuthentication("/test/stop", null)))
          .andExpect(status().isOk());
    });
  }

  protected static String setAuthentication(String url, @Nullable JSONObject params) {
    JSONObject authObj = JSONUtil.createObj();
    authObj.set("url", url);
    authObj.set("params", params);
    return EncryptUtil.encryptPublicRSA(authObj.toString());
  }
}