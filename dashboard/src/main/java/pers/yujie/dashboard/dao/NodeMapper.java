package pers.yujie.dashboard.dao;

import java.math.BigInteger;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import pers.yujie.dashboard.entity.Node;

@Mapper
public interface NodeMapper {

  void insertNode(Node node);

  void deleteByNodeId(BigInteger node_id);

  void updateNode(Node node);

  void updateNodeBatch(List<Node> nodes);

  Node selectByNodeId(BigInteger node_id);

  Node selectMasterNodeByCluster(BigInteger cluster_id);

  List<Node> selectByClusterId(BigInteger clusterId);

  List<Node> selectHealthyByClusterId(BigInteger clusterId);

  BigInteger selectMaxNodeId();

  int selectAllFreeSpace();

  List<Node> selectMaxNodeByClusterId(BigInteger clusterId);

}
