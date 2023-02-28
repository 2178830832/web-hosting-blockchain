package pers.yujie.dashboard.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pers.yujie.dashboard.controller.TestControllerTest.setAuthentication;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class WebsiteControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @Order(1)
  void testListWebsite() {
    assertDoesNotThrow(() -> {
      mockMvc.perform(get("/website/list")
          .header("Authorization", setAuthentication("/website/list", null)))
          .andExpect(status().isOk());
    });
  }

  @Test
  @Order(2)
  void testUploadWebsite() {
    JSONObject website = JSONUtil.createObj();
    website.set("name", "website");

    assertDoesNotThrow(() -> {
      website.set("path",
          new ClassPathResource("application.properties").getFile().getAbsolutePath());
      mockMvc.perform(post("/website/insert")
          .header("Authorization", setAuthentication("/website/insert", JSONUtil.parseObj(website)))
          .contentType(MediaType.APPLICATION_JSON)
          .content(JSONUtil.parseObj(website).toString()))
          .andExpect(status().isOk());
    });
  }

  @Test
  @Order(3)
  void testUpdateWebsite() {
    JSONObject website = JSONUtil.createObj();
    website.set("id", BigInteger.ZERO);
    website.set("name", "website");

    assertDoesNotThrow(() -> {
      website.set("path",
          new ClassPathResource("application.properties").getFile().getAbsolutePath());
      mockMvc.perform(post("/website/update")
          .header("Authorization", setAuthentication("/website/update", JSONUtil.parseObj(website)))
          .contentType(MediaType.APPLICATION_JSON)
          .content(JSONUtil.parseObj(website).toString()))
          .andExpect(status().isOk());
    });
  }

  @Test
  @Order(4)
  void testDeleteWebsite() {
    JSONObject website = JSONUtil.createObj();
    website.set("id", BigInteger.ZERO);

    assertDoesNotThrow(() -> {
      mockMvc.perform(post("/website/delete")
          .header("Authorization", setAuthentication("/website/delete", JSONUtil.parseObj(website)))
          .contentType(MediaType.APPLICATION_JSON)
          .content(JSONUtil.parseObj(website).toString()))
          .andExpect(status().isOk());
    });
  }
}