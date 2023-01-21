package pers.yujie.dashboard.dao;

import java.math.BigInteger;
import java.util.List;
import pers.yujie.dashboard.entity.Cluster;

public interface ClusterDao {

  List<Cluster> selectAllCluster();

  void initClusterDao();

//  List<Cluster1> selectAllHealthyCluster();
//
//  BigInteger selectFreeSpaceByCluster(String clusterName);
//
//  void updateClusterBatch(List<Cluster1> clusters);
  // void insertCluster(Cluster cluster);
  //
  //  void deleteByClusterId(BigInteger cluster_id);
  //
  //  void updateCluster(Cluster cluster);
  //
  //  void updateClusterBatch(List<Cluster> clusters);
  //

  //  Cluster selectByClusterId(BigInteger cluster_id);
  //
  //  List<Cluster> selectAllCluster();
  //
  //  List<Cluster> selectMinCluster();
}
