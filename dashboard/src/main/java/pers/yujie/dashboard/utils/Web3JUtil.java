package pers.yujie.dashboard.utils;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

@Component
@Slf4j
public class Web3JUtil {

  private static final String ip = "http://127.0.0.1:8545";

  @Getter
  @Setter
  private static Web3j web3;
  private static final String account = "0xa2Cba398E8E4378803b071c68556D24bE51D4B0b";
  private static final String contract = "0x93335cA438449dDc0B089163b7e953E21EAFF7C5";
  private static final BigInteger gasLimit = BigInteger.valueOf(3000000);

  @PostConstruct
  private void initWeb3J() {
//    web3j = Web3j.build(new HttpService(ip));
//    log.info("Connected to Web3J at: " + ip);
  }


  public static Web3j getClient() {
    if (web3 == null) {
      synchronized (Web3JUtil.class) {
        if (web3 == null) {
          web3 = Web3j.build(new HttpService(ip));
        }
      }
    }
    return web3;
  }

  @SuppressWarnings({"rawtypes"})
  public static List<Type> sendQuery(String functionName, List<Type> inputParameters,
      List<TypeReference<?>> outputParameters)
      throws ExecutionException, InterruptedException {
    Function function = new Function(functionName, inputParameters, outputParameters);

    String encodedFunction = FunctionEncoder.encode(function);
    EthCall response = getClient().ethCall(
        Transaction.createEthCallTransaction(account, contract, encodedFunction),
        DefaultBlockParameterName.LATEST).sendAsync().get();
    return FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
  }

  @SuppressWarnings({"rawtypes"})
  public static EthSendTransaction sendTransaction(String functionName, List<Type> inputParameters)
      throws ExecutionException, InterruptedException {
    Function function = new Function(functionName, inputParameters, Collections.emptyList());
    String encodedFunction = FunctionEncoder.encode(function);

    BigInteger nonce = getClient().ethGetTransactionCount(
        account, DefaultBlockParameterName.LATEST)
        .sendAsync().get().getTransactionCount();

    return getClient().ethSendTransaction(
        Transaction.createFunctionCallTransaction(
            account, nonce, BigInteger.ZERO, gasLimit, contract, encodedFunction))
        .sendAsync().get();
  }

}

