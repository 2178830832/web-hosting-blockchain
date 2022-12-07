package pers.yujie.dashboard.dao;

import java.math.BigInteger;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import pers.yujie.dashboard.entity.Cluster;

@Mapper
public interface ClusterMapper {

  void insertCluster(Cluster cluster);

  void deleteByClusterId(BigInteger cluster_id);

  void updateCluster(Cluster cluster);

  void updateClusterBatch(List<Cluster> clusters);

  void updateClusterSpace(BigInteger cluster_id);

  Cluster selectByClusterId(BigInteger cluster_id);

  List<Cluster> selectAllCluster();

  List<Cluster> selectAllHealthyCluster();

  List<Cluster> selectMinCluster();
}
