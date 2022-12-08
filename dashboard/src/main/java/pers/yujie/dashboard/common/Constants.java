package pers.yujie.dashboard.common;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
  public final static String IPFS_PREFIX = "ipfs";

  @Value("${local.ipfs.port}")
  private String ipfsPort;

  @PostConstruct
  private void initConstants() {
    IPFS_PORT = ipfsPort;
  }

  public static String IPFS_PORT;
}
