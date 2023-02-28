package pers.yujie.dashboard.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.service.ClusterService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClusterServiceImplTest {

  @Autowired
  ClusterService clusterService;

  @Test
  @Order(1)
  void testDistributeWebsite() {
    Website website = new Website(BigInteger.ZERO, "cid", "website",
        BigInteger.ZERO, "online", JSONUtil.createArray());
    assertEquals(clusterService.distributeWebsite(
        JSONUtil.parseObj(website), new ArrayList<>()), "");
  }

  @Test
  @Order(2)
  void testRemoveWebsite() {
    Website website = new Website(BigInteger.ZERO, "cid", "website",
        BigInteger.ZERO, "online", JSONUtil.createArray());
    assertTrue(clusterService.removeWebsite(JSONUtil.parseObj(website)));
  }
}