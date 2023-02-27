package pers.yujie.dashboard.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import io.ipfs.api.IPFS;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ws.rs.ProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import pers.yujie.dashboard.common.Constants;
import pers.yujie.dashboard.dao.ClusterDao;
import pers.yujie.dashboard.dao.NodeDao;
import pers.yujie.dashboard.dao.WebsiteDao;
import pers.yujie.dashboard.service.ConfigService;
import pers.yujie.dashboard.utils.DockerUtil;
import pers.yujie.dashboard.utils.IPFSUtil;
import pers.yujie.dashboard.utils.Web3JUtil;

/**
 * This class is responsible for providing website services.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see WebsiteDao
 * @see ClusterDao
 * @see NodeDao
 * @since 05/01/2023
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

  private static final JSONObject defaultObj = JSONUtil.createObj();

  @Resource
  private WebsiteDao websiteDao;

  @Resource
  private ClusterDao clusterDao;

  @Resource
  private NodeDao nodeDao;

  /**
   * Initialise the configs when the app starts running.
   *
   * @see PostConstruct
   */
  @PostConstruct
  private void initConfig() {
//    connectIPFS(Constants.IPFS_ADDRESS);
    connectDocker(Constants.DOCKER_ADDRESS);
//    connectWeb3(Constants.WEB3_ADDRESS, Constants.WEB3_ACCOUNT, Constants.WEB3_CONTRACT);

    if (Web3JUtil.getAddress() != null) {
      websiteDao.initWebsiteDao();
      nodeDao.initNodeDao();
      clusterDao.initClusterDao();
    }
  }

  /**
   * Clear instances before exiting.
   *
   * @see PreDestroy
   */
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

  /**
   * Connect to IPFS API.
   *
   * @param address the address (port) of the IPFS API
   * @return a blank string if succeeded, an error message otherwise
   */
  @Override
  public String connectIPFS(String address) {
    try {
      address = address.trim().toLowerCase(Locale.ROOT);
      IPFSUtil.setIpfs(new IPFS(address));
      log.info("Connected to IPFS at: " + address);
      IPFSUtil.setAddress(address);
    } catch (RuntimeException e) {
      log.warn("Unable to connect to the IPFS address: " + address);
      return e.getMessage();
    }
    return "";
  }

  /**
   * Connect to Docker.
   *
   * @param address the address (port) of the remote Docker server
   * @return a blank string if succeeded, an error message otherwise
   */
  @Override
  @SuppressWarnings("deprecation")
  public String connectDocker(String address) {
    address = address.trim().toLowerCase(Locale.ROOT);
    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost(address)
        .build();

    // set timeout to 2 seconds
    DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory()
        .withReadTimeout(2000)
        .withConnectTimeout(2000);

    try {
      // create a Docker client
      DockerUtil.setDocker(DockerClientBuilder.getInstance(config)
          .withDockerCmdExecFactory(dockerCmdExecFactory)
          .build());
      // request the Docker info
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

  /**
   * Connect to the Ganache server and valid the account and contract
   *
   * @param address  the address (port) of the Ganache
   * @param account  Ethereum account
   * @param contract Ethereum contract
   * @return a blank string if succeeded, an error message otherwise
   */
  @Override
  public String connectWeb3(String address, String account, String contract) {
    if (address == null) {
      return "Ethereum address not configured.";
    }
    // check if the account and contract is in the correct hash format
    String regex = "^0x[a-fA-F0-9]{40}$";
    if (account != null) {
      account = account.trim().toLowerCase(Locale.ROOT);
      if (!ReUtil.isMatch(regex, account)) {
        return "Invalid Ethereum account: " + account;
      }
      Web3JUtil.setAccount(account);
    }

    if (contract != null) {
      contract = contract.trim().toLowerCase(Locale.ROOT);
      if (!ReUtil.isMatch(regex, contract)) {
        return "Invalid Ethereum contract: " + contract;
      }
      Web3JUtil.setContract(contract);
    }

    // normalise the address and create a new Web3J instance
    address = address.trim().toLowerCase(Locale.ROOT);
    // set timeout to three seconds
    Web3JUtil.setWeb3(Web3j.build(new HttpService(address,
        new OkHttpClient.Builder().readTimeout(3, TimeUnit.SECONDS).build())));

    try {
      Web3JUtil.getWeb3().web3ClientVersion().send().getWeb3ClientVersion();

      log.info("Connected to Web3 at: " + address);
      Web3JUtil.setAddress(address);
    } catch (IOException | IllegalArgumentException e) {
      log.warn("Unable to connect to the web3J address: " + address);
      return e.getMessage();
    }
    return "";
  }

  /**
   * Get the current IPFS status, also check if the connection is still open.
   *
   * @return {@link JSONObject} containing information of the connected IPFS server
   */
  @Override
  public JSONObject getIPFSStatus() {
    if (IPFSUtil.getIpfs() == null) {
      return defaultObj;
    }

    try {
      JSONObject status = JSONUtil.parseObj(IPFSUtil.getIpfs().id());
      status.set("Address", IPFSUtil.getAddress());
      return status;
    } catch (IOException | RuntimeException e) {
      log.error("Error when requesting ipfs id");
      IPFSUtil.setAddress(null);
    }
    return defaultObj;
  }

  /**
   * Get the current Docker status, also check if the connection is still open.
   *
   * @return {@link JSONObject} containing information of the connected Docker server
   */
  @Override
  public JSONObject getDockerStatus() {
    if (DockerUtil.getDocker() == null) {
      return defaultObj;
    }
    try {
      JSONObject status = JSONUtil.parseObj(DockerUtil.getDocker().infoCmd().exec());
      status.set("Address", DockerUtil.getAddress());
      return status;
    } catch (ProcessingException e) {
      log.error("Error when requesting docker info");
      DockerUtil.setAddress(null);
    }
    return defaultObj;
  }

  /**
   * Get the current Ethereum status, also check if the connection is still open.
   *
   * @return {@link JSONObject} containing information of the connected Ganache
   */
  @Override
  public JSONObject getWeb3Status() {
    if (Web3JUtil.getWeb3() == null) {
      return defaultObj;
    }

    try {
      JSONObject status = new JSONObject();
      status.set("Client", Web3JUtil.getWeb3().web3ClientVersion().send().getWeb3ClientVersion());
      status.set("Address", Web3JUtil.getAddress());
      status.set("Account", Web3JUtil.getAccount());
      status.set("Contract", Web3JUtil.getContract());
      status.set("Balance", Web3JUtil.getAccountBalance(Web3JUtil.getAccount()));
      return status;
    } catch (IOException | ExecutionException | InterruptedException | IllegalArgumentException e) {
      log.error("Error when requesting web3j info");
      Web3JUtil.setAddress(null);
    }

    return defaultObj;
  }
}
