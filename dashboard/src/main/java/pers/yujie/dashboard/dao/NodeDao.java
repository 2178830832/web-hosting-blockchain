package pers.yujie.dashboard.dao;

import java.util.List;
import pers.yujie.dashboard.entity.Node;

public interface NodeDao {

  List<Node> selectOnlineByCluster(String clusterName);

  void updateNodeBatch(List<Node> nodes);
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
