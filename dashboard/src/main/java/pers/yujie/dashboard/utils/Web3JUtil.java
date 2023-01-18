package pers.yujie.dashboard.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

@Component
@Slf4j
public class Web3JUtil {

  private static final String ip = "http://127.0.0.1:8545";

  @Getter
  @Setter
  private static Web3j web3;

  @Getter
  @Setter
  private static String address;

  @Getter
  @Setter
  private static String account;

  @Getter
  @Setter
  private static String contract;

  private static final BigInteger gasLimit = BigInteger.valueOf(3000000);
  private static final DefaultBlockParameterName latestBlock = DefaultBlockParameterName.LATEST;


//  public static Web3j getClient() {
//    if (web3 == null) {
//      synchronized (Web3JUtil.class) {
//        if (web3 == null) {
//          web3 = Web3j.build(new HttpService(ip));
//        }
//      }
//    }
//    return web3;
//  }

  public static BigDecimal getAccountBalance(String account)
      throws ExecutionException, InterruptedException {
    EthGetBalance ethGetBalance = web3.ethGetBalance(account, latestBlock)
        .sendAsync().get();
    BigDecimal balance = new BigDecimal(ethGetBalance.getBalance());
    return Convert.fromWei(balance, Unit.ETHER).setScale(2, RoundingMode.HALF_UP);
  }

  @SuppressWarnings({"rawtypes"})
  public static List<Type> sendQuery(String functionName, List<Type> inputParameters,
      List<TypeReference<?>> outputParameters)
      throws ExecutionException, InterruptedException {
    Function function = new Function(functionName, inputParameters, outputParameters);

    String encodedFunction = FunctionEncoder.encode(function);
    EthCall response = web3.ethCall(
        Transaction.createEthCallTransaction(account, contract, encodedFunction),
        DefaultBlockParameterName.LATEST).sendAsync().get();
    return FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
  }

  @SuppressWarnings({"rawtypes"})
  public static EthSendTransaction sendTransaction(String functionName, List<Type> inputParameters)
      throws ExecutionException, InterruptedException {
    Function function = new Function(functionName, inputParameters, Collections.emptyList());
    String encodedFunction = FunctionEncoder.encode(function);

    BigInteger nonce = web3.ethGetTransactionCount(
        account, latestBlock)
        .sendAsync().get().getTransactionCount();

    return web3.ethSendTransaction(
        Transaction.createFunctionCallTransaction(
            account, nonce, BigInteger.ZERO, gasLimit, contract, encodedFunction))
        .sendAsync().get();
  }

}

