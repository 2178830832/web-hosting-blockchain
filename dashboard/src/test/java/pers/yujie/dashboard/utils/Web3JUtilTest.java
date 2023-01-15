package pers.yujie.dashboard.utils;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

class Web3JUtilTest {

  private static Web3j web3;
  private static final String ip = "http:/127.0.0.1:8545";
  private static final String account = "0xa2Cba398E8E4378803b071c68556D24bE51D4B0b";
  private static final String contract = "0x93335cA438449dDc0B089163b7e953E21EAFF7C5";

  @BeforeAll
  static void setUp() {
    web3 = Web3j.build(new HttpService(ip));
  }

  @Test
  void testConnect() throws IOException {
    System.out.println(web3.web3ClientVersion().send().getWeb3ClientVersion());

  }

  @Test
  void testBalance() throws ExecutionException, InterruptedException {
    EthGetBalance ethGetBalance = web3.ethGetBalance(account, DefaultBlockParameterName.LATEST)
        .sendAsync().get();
    System.out.println(ethGetBalance.getBalance());
  }
}