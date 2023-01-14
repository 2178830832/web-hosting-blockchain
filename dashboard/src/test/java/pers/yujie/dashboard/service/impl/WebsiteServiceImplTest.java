package pers.yujie.dashboard.service.impl;

import io.ipfs.api.IPFS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebsiteServiceImplTest {
  IPFS ipfs;

  @BeforeEach
  void setUp() {
    ipfs = new IPFS("/ip4/10.176.32.128/tcp/5001");
//    ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
  }

  @Test
  void testConnect() {
    System.out.println(ipfs.toString());
  }
}