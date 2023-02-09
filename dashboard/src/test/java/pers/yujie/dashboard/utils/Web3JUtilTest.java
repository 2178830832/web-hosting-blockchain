package pers.yujie.dashboard.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import io.ipfs.api.IPFS;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.primitive.Byte;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.service.ConfigService;
import pers.yujie.dashboard.service.impl.ConfigServiceImpl;

class Web3JUtilTest {

  private static Web3j web3;
  private static final String ip = "http:/127.0.0.1:8545";
  private static final String account = "0xa2Cba398E8E4378803b071c68556D24bE51D4B0b";
  private static final String contract = "0x6D2E5Ef426459EB5145880d141C3B8F3520dF1D2";

  @BeforeAll
  static void setUp() {
    web3 = Web3j.build(new HttpService(ip));
  }

  @Test
  void testConnect() throws IOException {
    System.out.println(web3.web3ClientVersion().send().getWeb3ClientVersion());

  }

  @Test
  void testString() {
    Web3JUtil.setWeb3(Web3j.build(new HttpService(ip,
        new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build())));

      Web3JUtil.setAddress(ip);
      Web3JUtil.setAccount(account);
      Web3JUtil.setContract(contract);

    try {
      Web3JUtil.getWeb3().web3ClientVersion().send().getWeb3ClientVersion();
      List<Type> list = Web3JUtil.sendQuery("getClusters",
          Collections.emptyList(),
          Collections.singletonList(new TypeReference<Utf8String>() {
          }));

      String clusterEncodedStr = (String) list.get(0).getValue();
      if (clusterEncodedStr == null || clusterEncodedStr.equals("")) {
        return;
      }
      clusterEncodedStr = EncryptUtil.aesDecrypt(clusterEncodedStr);
      System.out.println(JSONUtil.toList(JSONUtil.parseArray(clusterEncodedStr), Cluster.class));
    } catch (ExecutionException | InterruptedException | IOException ignored) {

    }
  }

  @Test
  void testBalance() throws ExecutionException, InterruptedException {
    EthGetBalance ethGetBalance = web3.ethGetBalance(account, DefaultBlockParameterName.LATEST)
        .sendAsync().get();
    System.out.println(ethGetBalance.getBalance());
  }

  @Test
  void testTrans() throws IOException {
    ConfigService configService = new ConfigServiceImpl();
    configService.connectDocker("tcp://192.168.80.128:2375");
//    IPFS ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
//    JSONObject status = JSONUtil.parseObj(ipfs.id());
    JSONObject status = JSONUtil.parseObj(
        DockerUtil.getDocker().infoCmd().exec());
    status.set("Address", DockerUtil.getAddress());
    JSONObject object = JSONUtil.createObj();
    object.set("docker", status);
    String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\"}";
    JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
    System.out.println(object.getJSONObject("docker").getStr("id"));
  }
}