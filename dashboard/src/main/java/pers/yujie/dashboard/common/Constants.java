package pers.yujie.dashboard.common;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
  public final static String IPFS_PREFIX = "ipfs";

  @Value("${ipfs.port}")
  private String ipfsPort;

  @Value("${web3.port}")
  private String web3Port;

  @Value("${docker.port}")
  private String dockerPort;

  @Value("${docker.volume}")
  private String dockerVolume;

  @PostConstruct
  private void initConstants() {
    IPFS_PORT = ipfsPort;
    WEB3_PORT = web3Port;
    DOCKER_PORT = dockerPort;
    DOCKER_VOLUME = dockerVolume;
  }

  public static String IPFS_PORT;

  public static String WEB3_PORT;

  public static String DOCKER_PORT;

  public static String DOCKER_VOLUME;
}
