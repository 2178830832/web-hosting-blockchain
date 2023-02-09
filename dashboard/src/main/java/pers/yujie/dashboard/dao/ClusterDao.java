package pers.yujie.dashboard.dao;

import cn.hutool.json.JSONObject;
import java.math.BigInteger;
import java.util.List;
import pers.yujie.dashboard.entity.Cluster;

public interface ClusterDao {

  List<JSONObject> selectAllCluster();

  void initClusterDao();

  boolean updateCluster(JSONObject cluster);

  boolean updateClusterBatch(List<JSONObject> clusterList);

  JSONObject selectClusterById(BigInteger id);

  JSONObject selectMinCluster();

  JSONObject selectMinHealthyCluster();

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
