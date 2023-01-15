package pers.yujie.dashboard.common;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

  public final static String IPFS_PREFIX = "ipfs";

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

  @Value("${tester.address}")
  private String testerAddress;

  @PostConstruct
  private void initConstants() {
    IPFS_ADDRESS = ipfsAddress;
    WEB3_ADDRESS = web3Address;
    DOCKER_ADDRESS = dockerAddress;
    DOCKER_VOLUME = dockerVolume;
    WEB3_CONTRACT = web3Contract;
    WEB3_ACCOUNT = web3Account;
    TESTER_ADDRESS = testerAddress;
  }

  public static String IPFS_ADDRESS;

  public static String WEB3_ADDRESS;

  public static String DOCKER_ADDRESS;

  public static String DOCKER_VOLUME;

  public static String WEB3_CONTRACT;

  public static String WEB3_ACCOUNT;

  public static String TESTER_ADDRESS;
}
