package org.nottingham.dashboard.dao;

import java.math.BigInteger;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.nottingham.dashboard.entity.Node;

@Mapper
public interface NodeMapper {

  int insertNode(Node node);

  int deleteByNodeId(BigInteger node_id);

  int updateNode(Node node);

  Node selectByNodeId(BigInteger node_id);

  List<Node> selectByClusterId(BigInteger clusterId);
}
