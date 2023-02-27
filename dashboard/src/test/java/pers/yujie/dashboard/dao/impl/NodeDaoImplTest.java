package pers.yujie.dashboard.dao.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.yujie.dashboard.dao.NodeDao;
import pers.yujie.dashboard.entity.Node;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NodeDaoImplTest {

  @Autowired
  NodeDao nodeDao;

  @Test
  @Order(1)
  void testInsertNode() {
    Node node = new Node(BigInteger.ZERO, BigInteger.ZERO, "node",
        "created", BigInteger.ZERO, BigInteger.ZERO, JSONUtil.createArray());
    Node node2 = new Node(BigInteger.ONE, BigInteger.ZERO, "node2",
        "created", BigInteger.ZERO, BigInteger.ZERO, JSONUtil.createArray());
    assertTrue(nodeDao.insertNode(JSONUtil.parseObj(node)));
    assertTrue(nodeDao.insertNode(JSONUtil.parseObj(node2)));
  }

  @Test
  @Order(2)
  void testSelectNodeByCluster() {
    List<JSONObject> nodeList = nodeDao.selectNodeByCluster(BigInteger.ZERO);
    assertFalse(nodeList.isEmpty());
  }

  @Test
  @Order(3)
  void testUpdateNode() {
    Node node = new Node(BigInteger.ZERO, BigInteger.ZERO, "node",
        "updated", BigInteger.ZERO, BigInteger.ZERO, JSONUtil.createArray());
    assertTrue(nodeDao.updateNode(JSONUtil.parseObj(node)));
  }

  @Test
  @Order(4)
  void testUpdateNodeBatch() {
    List<JSONObject> nodeList = new ArrayList<>();
    Node node = new Node(BigInteger.ZERO, BigInteger.ZERO, "node",
        "updated", BigInteger.ZERO, BigInteger.ZERO, JSONUtil.createArray());
    Node node2 = new Node(BigInteger.ONE, BigInteger.ZERO, "node2",
        "updated", BigInteger.ZERO, BigInteger.ZERO, JSONUtil.createArray());
    nodeList.add(JSONUtil.parseObj(node));
    nodeList.add(JSONUtil.parseObj(node2));
    assertTrue(nodeDao.updateNodeBatch(nodeList));
  }

  @Test
  @Order(5)
  void testDeleteNode() {
    assertTrue(nodeDao.deleteNode(BigInteger.ZERO));
    assertTrue(nodeDao.deleteNode(BigInteger.ONE));
  }
}