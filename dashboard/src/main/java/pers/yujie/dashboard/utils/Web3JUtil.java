package pers.yujie.dashboard.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

/**
 * This is a utility class responsible for conducting Ethereum query and transaction.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 01/11/2022
 */
@Component
@Slf4j
public class Web3JUtil {

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

  // set gas limit to a fixed large value
  private static final BigInteger gasLimit = BigInteger.valueOf(3000000);
  // use latest block parameter
  private static final DefaultBlockParameterName latestBlock = DefaultBlockParameterName.LATEST;

  /**
   * Get account balance to verify its correctness
   *
   * @param account an Ethereum account
   * @return {@link BigDecimal} representing the balance
   * @throws ExecutionException   thrown by {@link CompletableFuture#get()}
   * @throws InterruptedException thrown by {@link CompletableFuture#get()}
   */
  public static BigDecimal getAccountBalance(String account)
      throws ExecutionException, InterruptedException {
    EthGetBalance ethGetBalance = web3.ethGetBalance(account, latestBlock)
        .sendAsync().get();
    BigDecimal balance = new BigDecimal(ethGetBalance.getBalance());
    return Convert.fromWei(balance, Unit.ETHER).setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * Send a query to an existing Ethereum contract.
   *
   * @param functionName     the query function name
   * @param inputParameters  function input parameters
   * @param outputParameters function output parameters
   * @return query result
   * @throws ExecutionException   thrown by {@link CompletableFuture#get()}
   * @throws InterruptedException thrown by {@link CompletableFuture#get()}
   */
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

  /**
   * Send a transaction to an existing Ethereum contract.
   *
   * @param functionName    the transaction function name
   * @param inputParameters function input parameters
   * @return transaction result
   * @throws ExecutionException   thrown by {@link CompletableFuture#get()}
   * @throws InterruptedException thrown by {@link CompletableFuture#get()}
   */
  @SuppressWarnings({"rawtypes"})
  public static EthSendTransaction sendTransaction(String functionName, List<Type> inputParameters)
      throws ExecutionException, InterruptedException {
    Function function = new Function(functionName, inputParameters, Collections.emptyList());
    String encodedFunction = FunctionEncoder.encode(function);

    // get the latest nonce
    BigInteger nonce = web3.ethGetTransactionCount(
        account, latestBlock)
        .sendAsync().get().getTransactionCount();

    // send transaction
    return web3.ethSendTransaction(
        Transaction.createFunctionCallTransaction(
            account, nonce, BigInteger.ZERO, gasLimit, contract, encodedFunction))
        .sendAsync().get();
  }

}

