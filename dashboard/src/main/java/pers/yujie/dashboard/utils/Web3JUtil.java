package pers.yujie.dashboard.utils;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
public class Web3JUtil {

  private static final String ip = "http://127.0.0.1:8545";
  private volatile static Web3j web3j;
  private static final String account = "0xa2Cba398E8E4378803b071c68556D24bE51D4B0b";
  private static final String contract = "0xCB5C9434453D544ae03dBB39D58Ddafb8Ec5B429";
  private static final BigInteger gasLimit = BigInteger.valueOf(3000000);

  public static Web3j getClient() {
    if (web3j == null) {
      synchronized (Web3JUtil.class) {
        if (web3j == null) {
          web3j = Web3j.build(new HttpService(ip));
        }
      }
    }
    return web3j;
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

