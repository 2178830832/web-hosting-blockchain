package pers.yujie.dashboard.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import io.ipfs.api.IPFS;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.print.Doc;
import javax.ws.rs.ProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import pers.yujie.dashboard.common.Constants;
import pers.yujie.dashboard.service.ConfigService;
import pers.yujie.dashboard.utils.DockerUtil;
import pers.yujie.dashboard.utils.IPFSUtil;
import pers.yujie.dashboard.utils.Web3JUtil;

@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

  private static JSONObject defaultObj = new JSONObject();

  @PostConstruct
  private void initConfig() {
    connectIPFS(Constants.IPFS_PORT);
    connectDocker(Constants.DOCKER_PORT);
    connectWeb3(Constants.WEB3_PORT);
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
  public void connectIPFS(String port) {
    try {
      IPFSUtil.setIpfs(new IPFS(port));
      log.info("Connected to IPFS at: " + port);
      IPFSUtil.setPort(port);
    } catch (RuntimeException e) {
      log.warn("Unable to connect to the default IPFS address: " + port);
      IPFSUtil.setIpfs(null);
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public void connectDocker(String port) {
    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost(port)
        .build();

    DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory()
        .withReadTimeout(1000)
        .withConnectTimeout(1000);

    // Create a Docker client
    DockerUtil.setDocker(DockerClientBuilder.getInstance(config)
        .withDockerCmdExecFactory(dockerCmdExecFactory)
        .build());

    try {
      DockerUtil.getDocker().infoCmd().exec();
      log.info("Connected to Docker at: " + port);
      DockerUtil.setPort(port);
    } catch (ProcessingException e) {
      log.warn("Unable to connect to the default docker address: " + port);
      DockerUtil.setDocker(null);
    }
  }

  @Override
  public void connectWeb3(String port) {
    Web3JUtil.setWeb3(Web3j.build(new HttpService(port)));

    try {
      Web3JUtil.getWeb3().web3ClientVersion().send().getWeb3ClientVersion();
      log.info("Connected to Web3 at: " + port);
      Web3JUtil.setPort(port);
    } catch (IOException e) {
      log.info("Unable to connect to the default web3J address: " + port);
      Web3JUtil.setWeb3(null);
    }
  }

  @Override
  public JSONObject getIPFSStatus() {
    if (IPFSUtil.getIpfs() == null) {
      return defaultObj;
    }

    try {
      JSONObject status = JSONObject.parseObject(JSONObject.toJSONString(IPFSUtil.getIpfs().id()));
      status.put("Port", IPFSUtil.getPort());
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
      status.put("Port", DockerUtil.getPort());
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
      status.put("Port", Web3JUtil.getPort());
      return status;
    } catch (IOException e) {
      log.error("Error when requesting web3j info");
    }
    return defaultObj;
  }
}
