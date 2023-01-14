package pers.yujie.dashboard.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

class Web3JUtilTest {
  private static Web3j web3;
  private static final String ip = "http:/127.0.0.1:8545";

  @BeforeAll
  static void setUp() {
    web3 = Web3j.build(new HttpService(ip));
  }

  @Test
  void testConnect() throws IOException {
    System.out.println(web3.web3ClientVersion().send().getWeb3ClientVersion());

  }
}