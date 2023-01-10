package pers.yujie.dashboard.service.impl;

import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import io.ipfs.api.IPFS;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.ProcessingException;
import lombok.extern.slf4j.Slf4j;
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
      log.info("Connected to IPFS at: " + Constants.IPFS_PORT);
    } catch (RuntimeException e) {
      log.warn("Unable to connect to the default IPFS address: " + Constants.IPFS_PORT);
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
      log.info("Connected to docker at: " + Constants.DOCKER_PORT);
    } catch (ProcessingException e) {
      log.warn("Unable to connect to the default docker address: " + Constants.DOCKER_PORT);
      DockerUtil.setDocker(null);
    }
  }

  @Override
  public void connectWeb3(String port) {
    Web3JUtil.setWeb3(Web3j.build(new HttpService(port)));

    try {
      Web3JUtil.getWeb3().web3ClientVersion().send().getWeb3ClientVersion();
      log.info("Connected to Web3J at: " + Constants.WEB3_PORT);
    } catch (IOException e) {
      log.info("Unable to connect to the default web3J address: " + Constants.WEB3_PORT);
      Web3JUtil.setWeb3(null);
    }
  }

}
