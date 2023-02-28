package pers.yujie.dashboard.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.service.NodeService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NodeServiceImplTest {

  @Autowired
  NodeService nodeService;

  @Test
  @Order(1)
  void testInsertNode() {
    Node node = new Node(BigInteger.ZERO, BigInteger.ZERO, "node",
        "created", BigInteger.ZERO, new BigInteger("1000"), JSONUtil.createArray());
    assertEquals(nodeService.insertNode(JSONUtil.parseObj(node)), "");
  }

  @Test
  @Order(2)
  void testUpdateNode() {
    Node node = new Node(BigInteger.ZERO, BigInteger.ZERO, "node",
        "updated", BigInteger.ZERO, new BigInteger("1000"), JSONUtil.createArray());
    assertEquals(nodeService.updateNode(JSONUtil.parseObj(node)), "");

    node = new Node(BigInteger.ZERO, BigInteger.ZERO, "node",
        "updated", BigInteger.ZERO, new BigInteger("2000"), JSONUtil.createArray());
    assertEquals(nodeService.updateNode(JSONUtil.parseObj(node)), "");

    node = new Node(BigInteger.ZERO, BigInteger.ZERO, "node",
        "online", BigInteger.ZERO, new BigInteger("1000"), JSONUtil.createArray());
    assertEquals(nodeService.updateNode(JSONUtil.parseObj(node)), "");
  }

  @Test
  @Order(3)
  void testChangeNodeStatus() {
    assertEquals(nodeService.changeNodeStatus(BigInteger.ZERO), "");
    assertEquals(nodeService.changeNodeStatus(BigInteger.ZERO), "");
  }

  @Test
  @Order(4)
  void testDeleteNode() {
    assertEquals(nodeService.deleteNode(BigInteger.ZERO), "");
  }
}