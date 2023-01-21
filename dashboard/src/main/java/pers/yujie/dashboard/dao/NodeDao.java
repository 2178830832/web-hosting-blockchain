package pers.yujie.dashboard.dao;

import cn.hutool.json.JSONObject;
import java.math.BigInteger;
import java.util.List;
import pers.yujie.dashboard.entity.Node;

public interface NodeDao {

  void initNodeDao();

  List<Node> selectAllNode();

  boolean insertNode(JSONObject node);

  boolean updateNode(JSONObject node);

  boolean deleteNode(BigInteger id);

//  List<Node> selectOnlineByCluster(String clusterName);

//  void updateNodeBatch(List<Node> nodes);

//  void updateNodeBatchByCluster(List<Node> nodes);
//void insertNode(Node node);
//
//  void deleteByNodeId(BigInteger node_id);
//
//  void updateNode(Node node);
//

//
//  Node selectByNodeId(BigInteger node_id);
//
//  Node selectMasterNodeByCluster(BigInteger cluster_id);
//
//  List<Node> selectByClusterId(BigInteger clusterId);
//

//
//  BigInteger selectMaxNodeId();
//
//  int selectAllFreeSpace();
//
//  List<Node> selectMaxNodeByClusterId(BigInteger clusterId);
}
