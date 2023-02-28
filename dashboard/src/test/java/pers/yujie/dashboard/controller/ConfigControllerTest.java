package pers.yujie.dashboard.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pers.yujie.dashboard.controller.TestControllerTest.setAuthentication;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pers.yujie.dashboard.common.Constants;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class ConfigControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void testAuthorize() {
    assertDoesNotThrow(() -> {
      mockMvc.perform(get("/config"))
          .andExpect(status().isUnauthorized());
    });
  }

  @Test
  void testGetConfigStatus() {
    assertDoesNotThrow(() -> {
      mockMvc.perform(get("/config")
          .header("Authorization", setAuthentication("/config", null)))
          .andExpect(status().isOk());
    });
  }

  @Test
  void testSetCustomIPFS() {
    JSONObject request = JSONUtil.createObj();
    request.set("address", "/ip4/127.0.0.1/tcp/5001");

    assertDoesNotThrow(() -> {
      mockMvc.perform(post("/config/ipfs")
          .header("Authorization", setAuthentication("/config/ipfs", request))
          .contentType(MediaType.APPLICATION_JSON)
          .content(request.toString()))
          .andExpect(status().isOk());
    });
  }

  @Test
  void testSetCustomWeb3() {
    JSONObject request = JSONUtil.createObj();
    request.set("address", Constants.WEB3_ADDRESS);
    request.set("account", Constants.WEB3_ACCOUNT);
    request.set("contract", Constants.WEB3_CONTRACT);

    assertDoesNotThrow(() -> {
      mockMvc.perform(post("/config/web3")
          .header("Authorization", setAuthentication("/config/web3", request))
          .contentType(MediaType.APPLICATION_JSON)
          .content(request.toString()))
          .andExpect(status().isOk());
    });
  }

  @Test
  void testSetCustomDocker() {
    JSONObject request = JSONUtil.createObj();
    request.set("address", "tcp://124.223.10.94:2375");

    assertDoesNotThrow(() -> {
      mockMvc.perform(post("/config/docker")
          .header("Authorization", setAuthentication("/config/docker", request))
          .contentType(MediaType.APPLICATION_JSON)
          .content(request.toString()))
          .andExpect(status().isOk());
    });
  }
}