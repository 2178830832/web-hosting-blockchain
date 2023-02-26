package pers.yujie.dashboard.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

class Web3JUtilTest {

  static final String address = "http://127.0.0.1:8545";
  static final String account = "0xa2Cba398E8E4378803b071c68556D24bE51D4B0b";
  static final String contract = "0x6D2E5Ef426459EB5145880d141C3B8F3520dF1D2";

  @BeforeAll
  static void setUp() {
    Web3JUtil.setWeb3(Web3j.build(new HttpService(address,
        new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build())));
    Web3JUtil.setContract(contract);
    Web3JUtil.setAccount(account);
  }

  @AfterAll
  static void tearDown() {
    Web3JUtil.getWeb3().shutdown();
  }

  @Test
  void testConnectWeb3J() {
    assertDoesNotThrow(() -> {
      Web3JUtil.getWeb3().web3ClientVersion().send().getWeb3ClientVersion();
    });
  }

  @Test
  void testGetAccountBalance() {
    BigDecimal balanceCount = new BigDecimal("100.00");
    assertDoesNotThrow(() -> {
      BigDecimal balance = Web3JUtil.getAccountBalance(account);
      assertEquals(balance, balanceCount);
    });
  }

  @Test
  void testSendQueryAndTransaction() {
    String queryFunctionName = "getWebsites";
    String transactionFunctionName = "setWebsites";
    assertDoesNotThrow(() -> {
      String encodedStr = (String) Web3JUtil.sendQuery(queryFunctionName,
          Collections.emptyList(),
          Collections.singletonList(new TypeReference<Utf8String>() {
          })).get(0).getValue();
      assertNotNull(encodedStr);

      EthSendTransaction response = Web3JUtil.sendTransaction(transactionFunctionName,
          Collections.singletonList(new Utf8String(encodedStr)));
      assertNull(response.getError());
    });
  }
}