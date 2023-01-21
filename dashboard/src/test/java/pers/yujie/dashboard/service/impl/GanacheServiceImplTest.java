package pers.yujie.dashboard.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import pers.yujie.dashboard.entity.Cluster1;
import pers.yujie.dashboard.entity.Node1;
import pers.yujie.dashboard.entity.Website1;
import pers.yujie.dashboard.utils.Web3JUtil;

@Slf4j
class GanacheServiceImplTest {

  static Web3j web3;

  @BeforeAll
  static void setUp() {
    web3 = Web3j.build(new HttpService("http://127.0.0.1:8545",
        new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build()));
  }

  @Test
  void testBalance() throws ExecutionException, InterruptedException {
    EthGetBalance ethGetBalance = web3.ethGetBalance("0xa2Cba398E8E4378803b071c68556D24bE51D4B0b",
        DefaultBlockParameterName.LATEST)
        .sendAsync().get();
    EthGetCode ethGetCode = web3
        .ethGetCode("0xcB95417EE2124D4e7C5C3Ab56cc8b198BD1E553", DefaultBlockParameterName.LATEST)
        .sendAsync().get();
    BigInteger balance = ethGetBalance.getBalance();
    BigDecimal data = Convert.fromWei(new BigDecimal(balance), Unit.ETHER)
        .setScale(2, RoundingMode.HALF_UP);
    System.out.println(data);
    System.out.println(ethGetCode.getCode().equals("0x"));
    JSONObject obj = JSONUtil.createObj();
    obj.set("data", "99.99");
    System.out.println(obj.toString());
  }

  //100000000000000000000
//1000000000000000000
  @Test
  void testWeb3() throws IOException, ExecutionException, InterruptedException {
    Web3j web3j = Web3JUtil.getWeb3();

    List<Type> inputParameters = new ArrayList<>();
//    Arrays.asList(new Type(value);
    inputParameters.add(new Uint(BigInteger.ONE));

    List<TypeReference<?>> outputParameters = new ArrayList<>();
    outputParameters.add(new TypeReference<Website1>() {
    });

    Function function = new Function("get",
        inputParameters, outputParameters);
    String encodedFunction = FunctionEncoder.encode(function);
    org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
        Transaction.createEthCallTransaction("0xa2Cba398E8E4378803b071c68556D24bE51D4B0b",
            "0xE0eCfAcC90e5A8C178629AC25c0de44719E8e19D", encodedFunction),
        DefaultBlockParameterName.LATEST)
        .sendAsync().get();
    List<Type> result = FunctionReturnDecoder.decode(
        response.getValue(), function.getOutputParameters());
    System.out.println(result.toString());
    EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST,
        DefaultBlockParameterName.LATEST, "0xa2Cba398E8E4378803b071c68556D24bE51D4B0b");
    web3j.ethLogFlowable(filter).subscribe(logs -> log.info(String.valueOf(logs)));
  }


  @Test
  void testWeb() throws IOException, ExecutionException, InterruptedException {

    List<Type<?>> inputParameters = new ArrayList<>();
//    Arrays.asList(new Type(value);
    inputParameters.add(new Uint(BigInteger.ONE));

    List<TypeReference<?>> outputParameters = new ArrayList<>();
//    outputParameters.add(new TypeReference<DynamicArray<Node>>() {});
    outputParameters.add(new TypeReference<Utf8String>() {
    });
    Function function = new Function("getWebsites",
        Collections.emptyList(), Collections.singletonList(new TypeReference<Utf8String>() {
    }));

    String encodedFunction = FunctionEncoder.encode(function);
    org.web3j.protocol.core.methods.response.EthCall response = web3.ethCall(
        Transaction.createEthCallTransaction("0xa2Cba398E8E4378803b071c68556D24bE51D4B0b",
            "0xE0eCfAcC90e5A8C178629AC25c0de44719E8e19D", encodedFunction),
        DefaultBlockParameterName.LATEST)
        .sendAsync().get();
    List<Type> result = FunctionReturnDecoder.decode(
        response.getValue(), function.getOutputParameters());
    System.out.println(result.get(0).getValue().toString());
//
//    List<Type> list = (List) result.get(0).getValue();
//    for (Type results : list) {
//      System.out.println(results.toString());
//    }
  }

  @Test
  void testIn() throws IOException, ExecutionException, InterruptedException {
    List<Node1> node1s = new ArrayList<>();
    node1s.add(new Node1("cluster0", "2, ", true, BigInteger.ONE, BigInteger.ONE));
    List<Cluster1> clusters = new ArrayList<>();
    clusters.add(new Cluster1(" 1", true, BigInteger.ONE, BigInteger.ONE));
    clusters.add(new Cluster1("2", true, BigInteger.ONE, BigInteger.ONE));
    clusters.add(new Cluster1("23", true, BigInteger.ONE, BigInteger.ONE));
    List<Type> inputParameters = new ArrayList<>();
    inputParameters.add(new DynamicArray(DynamicStruct.class, node1s));
    inputParameters.add(new Utf8String("cluster0"));
    List<Website1> website1s = new ArrayList<>();
    website1s.add(new Website1("cid1", "location", true));
    website1s.add(new Website1("cid", "location", true));

    Function function = new Function("setWebsites",
//        Collections.singletonList(new DynamicArray(DynamicStruct.class, nodes)),
        Collections.singletonList(new Utf8String("cluster0")),
        Collections.emptyList());

    String encodedFunction = FunctionEncoder.encode(function);

    EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(
        "0xa2Cba398E8E4378803b071c68556D24bE51D4B0b", DefaultBlockParameterName.LATEST).sendAsync()
        .get();

    BigInteger nonce = ethGetTransactionCount.getTransactionCount();
    org.web3j.protocol.core.methods.response.EthSendTransaction response = web3.ethSendTransaction(
        Transaction.createFunctionCallTransaction("0xa2Cba398E8E4378803b071c68556D24bE51D4B0b",
            nonce, BigInteger.ZERO, BigInteger.valueOf(3000000),
            "0xE0eCfAcC90e5A8C178629AC25c0de44719E8e19D",
            encodedFunction))
        .sendAsync().get();
    System.out.println(response.getResult());
    System.out.println(response.getError().getMessage());
//    TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(
//        response.getTransactionHash()).send().getTransactionReceipt().get();
//    String revertReason = RevertReasonExtractor.extractRevertReason(
//        transactionReceipt,
//        nodes.toString(),
//        Web3JUtil.getClient(),
//        true);
//    System.out.println(transactionReceipt.getStatus());
//    System.out.println(revertReason);
  }
}
