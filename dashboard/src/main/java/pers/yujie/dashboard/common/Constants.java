package pers.yujie.dashboard.common;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

  public final static String IPFS_PREFIX = "ipfs";

  public final static String TESTER_SERVER = "http://localhost:6001/webdriver?url={url}&mode={mode}";

  @Value("${ipfs.address}")
  private String ipfsAddress;

  @Value("${web3.address}")
  private String web3Address;

  @Value("${web3.contract}")
  private String web3Contract;

  @Value("${web3.account}")
  private String web3Account;

  @Value("${docker.address}")
  private String dockerAddress;

  @Value("${docker.volume}")
  private String dockerVolume;

  @PostConstruct
  private void initConstants() {
    IPFS_ADDRESS = ipfsAddress;
    WEB3_ADDRESS = web3Address;
    DOCKER_ADDRESS = dockerAddress;
    DOCKER_VOLUME = dockerVolume;
    WEB3_CONTRACT = web3Contract;
    WEB3_ACCOUNT = web3Account;
  }

  public static String IPFS_ADDRESS;

  public static String WEB3_ADDRESS;

  public static String DOCKER_ADDRESS;

  public static String DOCKER_VOLUME;

  public static String WEB3_CONTRACT;

  public static String WEB3_ACCOUNT;
}
