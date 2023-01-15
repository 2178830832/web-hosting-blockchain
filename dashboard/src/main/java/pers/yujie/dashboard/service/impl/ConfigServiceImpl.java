package pers.yujie.dashboard.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import io.ipfs.api.IPFS;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.ProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import pers.yujie.dashboard.common.Constants;
import pers.yujie.dashboard.service.ConfigService;
import pers.yujie.dashboard.utils.DockerUtil;
import pers.yujie.dashboard.utils.IPFSUtil;
import pers.yujie.dashboard.utils.Web3JUtil;

@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

  private static final JSONObject defaultObj = new JSONObject();

  @PostConstruct
  private void initConfig() {
    connectIPFS(Constants.IPFS_ADDRESS);
    connectDocker(Constants.DOCKER_ADDRESS);
    connectWeb3(Constants.WEB3_ADDRESS);
  }

  @PreDestroy
  private void exit() {
    exitDocker();
    exitWeb3j();
  }

  private void exitWeb3j() {
    if (Web3JUtil.getWeb3() != null) {
      Web3JUtil.getWeb3().shutdown();
    }
  }

  private void exitDocker() {
    if (DockerUtil.getDocker() != null) {
      try {
        DockerUtil.getDocker().close();
      } catch (IOException e) {
        e.printStackTrace();
        log.warn("Docker is not properly exited");
      }
    }
  }

  @Override
  public String connectIPFS(String address) {
    try {
      address = address.trim().toLowerCase();
      IPFSUtil.setIpfs(new IPFS(address));
      log.info("Connected to IPFS at: " + address);
      IPFSUtil.setAddress(address);
    } catch (RuntimeException e) {
      log.warn("Unable to connect to the IPFS address: " + address);
      return e.getMessage();
    }
    return "";
  }

  @Override
  @SuppressWarnings("deprecation")
  public String connectDocker(String address) {
    address = address.trim().toLowerCase();
    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost(address)
        .build();

    DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory()
        .withReadTimeout(1000)
        .withConnectTimeout(1000);

    try {
      // Create a Docker client
      DockerUtil.setDocker(DockerClientBuilder.getInstance(config)
          .withDockerCmdExecFactory(dockerCmdExecFactory)
          .build());

      DockerUtil.getDocker().infoCmd().exec();
      log.info("Connected to Docker at: " + address);
      DockerUtil.setAddress(address);
    } catch (ProcessingException | IllegalArgumentException e) {
      log.warn("Unable to connect to the docker address: " + address);
      return e.getMessage();
    } catch (NullPointerException e) {
      log.warn("Unable to connect to the docker address: " + address);
      return "Empty input field";
    }
    return "";
  }

  @Override
  public String connectWeb3(String address) {
    address = address.trim().toLowerCase();
    Web3JUtil.setWeb3(Web3j.build(new HttpService(address,
        new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build())));

    try {
      Web3JUtil.getWeb3().web3ClientVersion().send().getWeb3ClientVersion();
      log.info("Connected to Web3 at: " + address);
      Web3JUtil.setAddress(address);
      Web3JUtil.setAccount(Constants.WEB3_ACCOUNT);
      Web3JUtil.setContract(Constants.WEB3_CONTRACT);
    } catch (IOException | IllegalArgumentException e) {
      log.warn("Unable to connect to the web3J address: " + address);
      return e.getMessage();
    }
    return "";
  }

  @Override
  public JSONObject getIPFSStatus() {
    if (IPFSUtil.getIpfs() == null) {
      return defaultObj;
    }

    try {
      JSONObject status = JSONObject.parseObject(JSONObject.toJSONString(IPFSUtil.getIpfs().id()));
      status.put("Address", IPFSUtil.getAddress());
      return status;
    } catch (IOException e) {
      log.error("Error when requesting ipfs id");
    }
    return defaultObj;
  }

  @Override
  public JSONObject getDockerStatus() {
    if (DockerUtil.getDocker() == null) {
      return defaultObj;
    }
    try {
      JSONObject status = JSONObject.parseObject(JSONObject
          .toJSONString(DockerUtil.getDocker().infoCmd().exec()));
      status.put("Address", DockerUtil.getAddress());
      return status;
    } catch (ProcessingException e) {
      log.error("Error when requesting docker info");
    }
    return defaultObj;
  }

  @Override
  public JSONObject getWeb3Status() {
    if (Web3JUtil.getWeb3() == null) {
      return defaultObj;
    }

    try {
      JSONObject status = new JSONObject();
      status.put("Client", Web3JUtil.getWeb3().web3ClientVersion().send().getWeb3ClientVersion());
      status.put("Address", Web3JUtil.getAddress());
      status.put("Account", Web3JUtil.getAccount());
      status.put("Contract", Web3JUtil.getContract());
      status.put("Balance", Web3JUtil.getAccountBalance());
      return status;
    } catch (IOException | ExecutionException | InterruptedException | IllegalArgumentException e) {
      log.error("Error when requesting web3j info");
    }

    return defaultObj;
  }
}
