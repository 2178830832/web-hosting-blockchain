package pers.yujie.dashboard.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pers.yujie.dashboard.controller.TestControllerTest.setAuthentication;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class ClusterControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void testListClusters() {
    assertDoesNotThrow(() -> {
      mockMvc.perform(get("/cluster/list")
          .header("Authorization", setAuthentication("/cluster/list", null)))
          .andExpect(status().isOk());
    });
  }
}