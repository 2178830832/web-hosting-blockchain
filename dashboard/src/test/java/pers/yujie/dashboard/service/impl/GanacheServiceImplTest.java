package pers.yujie.dashboard.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
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
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.utils.Web3JUtil;

@Slf4j
class GanacheServiceImplTest {


  @Test
  void testWeb3() throws IOException, ExecutionException, InterruptedException {
    Web3j web3j = Web3JUtil.getClient();

    List<Type> inputParameters = new ArrayList<>();
//    Arrays.asList(new Type(value);
    inputParameters.add(new Uint(BigInteger.ONE));

    List<TypeReference<?>> outputParameters=  new ArrayList<>();
    outputParameters.add(new TypeReference<Website>() {
    });

    Function function = new Function("get",
        inputParameters,outputParameters);
    String encodedFunction = FunctionEncoder.encode(function);
    org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
        Transaction.createEthCallTransaction("0xF5d97270dD56963Fd971b414B0eaE4c0B2974aa2",
            "0xA902F52C1B9eF46C15E2f777fefc6206e01AC4F7", encodedFunction),
        DefaultBlockParameterName.LATEST)
        .sendAsync().get();
    List<Type> result = FunctionReturnDecoder.decode(
        response.getValue(), function.getOutputParameters());
    System.out.println(result.toString());
    EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST,
        DefaultBlockParameterName.LATEST, "0xF5d97270dD56963Fd971b414B0eaE4c0B2974aa2");
    web3j.ethLogFlowable(filter).subscribe(logs -> log.info(String.valueOf(logs)));
  }


  @Test
  void testWeb() throws IOException, ExecutionException, InterruptedException {
    Web3j web3j = Web3JUtil.getClient();

    List<Type<?>> inputParameters = new ArrayList<>();
//    Arrays.asList(new Type(value);
    inputParameters.add(new Uint(BigInteger.ONE));

    List<TypeReference<?>> outputParameters=  new ArrayList<>();
    outputParameters.add(new TypeReference<DynamicArray<Node>>() {});

    Function function = new Function("selectAllNodes",
        Collections.emptyList(),outputParameters);

    String encodedFunction = FunctionEncoder.encode(function);
    org.web3j.protocol.core.methods.response.EthCall response = web3j.ethCall(
        Transaction.createEthCallTransaction("0xF5d97270dD56963Fd971b414B0eaE4c0B2974aa2",
            "0xD0591FD65942502A421CF1B0Ce68BD8eE48dE680", encodedFunction),
        DefaultBlockParameterName.LATEST)
        .sendAsync().get();
    List<Type> result = FunctionReturnDecoder.decode(
        response.getValue(),function.getOutputParameters());

    List<Type> list = (List) result.get(0).getValue();
    for (Type results : list) {
      System.out.println(results.toString());
    }
  }

  @Test
  void testIn() throws IOException, ExecutionException, InterruptedException {
    Web3j web3j = Web3JUtil.getClient();
    List<Node> nodes = new ArrayList<>();
    nodes.add(new Node("cluster0", "2, ", true, BigInteger.ONE, BigInteger.ONE));
    List<Cluster> clusters = new ArrayList<>();
    clusters.add(new Cluster(" 1",  true, BigInteger.ONE, BigInteger.ONE));
    clusters.add(new Cluster("2",  true, BigInteger.ONE, BigInteger.ONE));
    clusters.add(new Cluster("23",  true, BigInteger.ONE, BigInteger.ONE));
    List<Type> inputParameters = new ArrayList<>();
    inputParameters.add(new DynamicArray(DynamicStruct.class, nodes));
    inputParameters.add(new Utf8String("cluster0"));
    List<Website> websites = new ArrayList<>();
    websites.add(new Website("cid1", "location", true));
    websites.add(new Website("cid", "location", true));

    Function function = new Function("updateNodeBatchByCluster",
        Collections.singletonList(new DynamicArray(DynamicStruct.class, nodes)),
//        Collections.singletonList(nodes.get(0)),
        Collections.emptyList());

    String encodedFunction = FunctionEncoder.encode(function);

    EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
        "0xa2Cba398E8E4378803b071c68556D24bE51D4B0b", DefaultBlockParameterName.LATEST).sendAsync().get();

    BigInteger nonce = ethGetTransactionCount.getTransactionCount();
    org.web3j.protocol.core.methods.response.EthSendTransaction response = web3j.ethSendTransaction(
        Transaction.createFunctionCallTransaction("0xa2Cba398E8E4378803b071c68556D24bE51D4B0b",
            nonce,BigInteger.ZERO, BigInteger.valueOf(3000000),"0x2BE1Fda8d355833b64393E642e59456685772424",
            encodedFunction))
        .sendAsync().get();

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
}}
