package pers.yujie.dashboard.dao.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.yujie.dashboard.dao.ClusterDao;
import pers.yujie.dashboard.entity.Cluster;

@SpringBootTest
class ClusterDaoImplTest {

  @Autowired
  ClusterDao clusterDao;

  @Test
  void testSelectMinCluster() {
    JSONObject minCluster = clusterDao.selectMinHealthyCluster();
    assertNotNull(minCluster);
  }

  @Test
  void testUpdateCluster() {
    Cluster cluster = new Cluster(BigInteger.ZERO, "cluster0",
        "healthy", BigInteger.ZERO, BigInteger.ZERO);
    assertTrue(clusterDao.updateCluster(JSONUtil.parseObj(cluster)));
  }

  @Test
  void testUpdateClusterBatch() {
    List<JSONObject> clusters = new ArrayList<>();
    Cluster cluster0 = new Cluster(BigInteger.ZERO, "cluster0",
        "healthy", BigInteger.ZERO, BigInteger.ZERO);
    Cluster cluster1 = new Cluster(BigInteger.ONE, "cluster1",
        "healthy", BigInteger.ZERO, BigInteger.ZERO);
    clusters.add(JSONUtil.parseObj(cluster0));
    clusters.add(JSONUtil.parseObj(cluster1));
    assertTrue(clusterDao.updateClusterBatch(clusters));
  }
}