package pers.yujie.dashboard.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.yujie.dashboard.service.ConfigService;

@SpringBootTest
class ConfigServiceImplTest {

  @Autowired
  ConfigService configService;

  @Test
  void testConnectIPFSStatus() {
    String address = "/ip4/127.0.0.1/tcp/5001";
    assertEquals(configService.connectIPFS(address), "");
    JSONObject status = configService.getIPFSStatus();
    assertNotNull(status.getStr("Address"));
  }

  @Test
  void testConnectDockerStatus() {
    String address = "tcp://124.223.10.94:2375";
    assertEquals(configService.connectDocker(address), "");
    JSONObject status = configService.getDockerStatus();
    assertNotNull(status.getStr("Address"));
  }

  @Test
  void testConnectWeb3Status() {
    String address = "http://127.0.0.1:8545";
    assertEquals(configService.connectWeb3(address, null, null), "");
    JSONObject status = configService.getWeb3Status();
    assertNotNull(status.getStr("Address"));
  }

  @Test
  void testConnectWeb3AccountStatus() {
    String address = "http://127.0.0.1:8545";
    String account = "0xa2Cba398E8E4378803b071c68556D24bE51D4B0b";
    String contract = "0x6D2E5Ef426459EB5145880d141C3B8F3520dF1D2";
    assertEquals(configService.connectWeb3(address, account, contract), "");
    JSONObject status = configService.getWeb3Status();
    assertNotNull(status.getStr("Address"));
  }
}