package org.nottingham.dashboard.dao;

import java.math.BigInteger;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.nottingham.dashboard.entity.Cluster;

@Mapper
public interface ClusterMapper {

  int insertCluster(Cluster cluster);

  int deleteByClusterId(BigInteger cluster_id);

  int updateCluster(Cluster cluster);

  Cluster selectByClusterId(BigInteger cluster_id);

  List<Cluster> selectAllCluster();

}
