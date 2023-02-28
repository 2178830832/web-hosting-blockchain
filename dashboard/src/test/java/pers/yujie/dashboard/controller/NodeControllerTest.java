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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pers.yujie.dashboard.entity.Node;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class NodeControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @Order(1)
  void testListNode() {
    assertDoesNotThrow(() -> {
      mockMvc.perform(get("/node/list")
          .header("Authorization", setAuthentication("/node/list", null)))
          .andExpect(status().isOk());
    });
  }

  @Test
  @Order(2)
  void testInsertNode() {
    Node node = new Node(BigInteger.ZERO, BigInteger.ZERO, "node",
        "created", BigInteger.ZERO, new BigInteger("1000"), JSONUtil.createArray());

    assertDoesNotThrow(() -> {
      mockMvc.perform(post("/node/insert")
          .header("Authorization", setAuthentication("/node/insert", JSONUtil.parseObj(node)))
          .contentType(MediaType.APPLICATION_JSON)
          .content(JSONUtil.parseObj(node).toString()))
          .andExpect(status().isOk());
    });
  }

  @Test
  @Order(3)
  void testUpdateNode() {
    Node node = new Node(BigInteger.ZERO, BigInteger.ZERO, "node",
        "updated", BigInteger.ZERO, new BigInteger("2000"), JSONUtil.createArray());

    assertDoesNotThrow(() -> {
      mockMvc.perform(post("/node/update")
          .header("Authorization", setAuthentication("/node/update", JSONUtil.parseObj(node)))
          .contentType(MediaType.APPLICATION_JSON)
          .content(JSONUtil.parseObj(node).toString()))
          .andExpect(status().isOk());
    });
  }

  @Test
  @Order(4)
  void testChangeNodeStatus() {
    Node node = new Node(BigInteger.ZERO);

    assertDoesNotThrow(() -> {
      mockMvc.perform(post("/node/status")
          .header("Authorization", setAuthentication("/node/status", JSONUtil.parseObj(node)))
          .contentType(MediaType.APPLICATION_JSON)
          .content(JSONUtil.parseObj(node).toString()))
          .andExpect(status().isOk());
    });
  }

  @Test
  @Order(5)
  void testDeleteNode() {
    Node node = new Node(BigInteger.ZERO);

    assertDoesNotThrow(() -> {
      mockMvc.perform(post("/node/delete")
          .header("Authorization", setAuthentication("/node/delete", JSONUtil.parseObj(node)))
          .contentType(MediaType.APPLICATION_JSON)
          .content(JSONUtil.parseObj(node).toString()))
          .andExpect(status().isOk());
    });
  }
}