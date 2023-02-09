package pers.yujie.dashboard.dao.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.SerializeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import pers.yujie.dashboard.dao.ClusterDao;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.utils.EncryptUtil;
import pers.yujie.dashboard.utils.Web3JUtil;

@Repository
@Slf4j
public class ClusterDaoImpl extends BaseDaoImpl implements ClusterDao {

  private List<Cluster> clusters = new ArrayList<>();

  @Override
  public List<JSONObject> selectAllCluster() {
    List<JSONObject> clusterList = new ArrayList<>();
    for (Cluster cluster : clusters) {
      clusterList.add(JSONUtil.parseObj(cluster));
    }
    return clusterList;
  }

  @Override
  public JSONObject selectMinCluster() {
    JSONObject minCluster = JSONUtil.createObj();
    BigInteger size = BigInteger.ZERO;
    for (Cluster cluster : clusters) {
      BigInteger clusterSize = cluster.getTotalSpace().subtract(cluster.getUsedSpace());
      if (clusterSize.compareTo(size) <= 0) {
        size = clusterSize;
        minCluster = JSONUtil.parseObj(cluster);
      }
    }
    return minCluster;
  }

  @Override
  public JSONObject selectMinHealthyCluster() {
    JSONObject minCluster = JSONUtil.createObj();
    BigInteger size = BigInteger.ZERO;
    for (Cluster cluster : clusters) {
      if (cluster.getStatus().equals("healthy")) {
        BigInteger clusterSize = cluster.getTotalSpace().subtract(cluster.getUsedSpace());
        if (clusterSize.compareTo(size) <= 0) {
          size = clusterSize;
          minCluster = JSONUtil.parseObj(cluster);
        }
      }
    }
    return minCluster;
  }

  @Override
  public void initClusterDao() {
    try {
      String clusterEncodedStr = (String) Web3JUtil.sendQuery("getClusters",
          Collections.emptyList(),
          Collections.singletonList(new TypeReference<Utf8String>() {
          })).get(0).getValue();
      if (clusterEncodedStr == null || clusterEncodedStr.equals("")) {
        createInitialClusters();
        return;
      }
      clusterEncodedStr = EncryptUtil.aesDecrypt(clusterEncodedStr);
      clusters = JSONUtil.toList(JSONUtil.parseArray(clusterEncodedStr), Cluster.class);
    } catch (ExecutionException | InterruptedException e) {
      log.error("Unable to retrieve cluster list from the database");
    }
  }

  private void createInitialClusters() {
    if (clusters.size() < 4) {
      List<Cluster> updatedClusters = SerializeUtil.clone(clusters);
      for (int i = 0; i < 3; i++) {
        Cluster cluster = new Cluster(BigInteger.valueOf(i), "cluster" + i,
            "healthy", BigInteger.ZERO, BigInteger.ZERO);
        updatedClusters.add(cluster);
      }
      commitChange(updatedClusters);
    }
  }


  @Override
  public boolean updateCluster(JSONObject cluster) {
    List<Cluster> updatedClusters = SerializeUtil.clone(clusters);
    for (Cluster upCluster : updatedClusters) {
      if (upCluster.getId().equals(cluster.getBigInteger("id"))) {
        setUpHelper(upCluster, cluster);
        updatedClusters.set(updatedClusters.indexOf(upCluster), upCluster);
        return commitChange(updatedClusters);
      }
    }
    return false;
  }

  @Override
  public boolean updateClusterBatch(List<JSONObject> clusterList) {
    List<Cluster> updatedClusters = SerializeUtil.clone(clusters);
    for (JSONObject cluster : clusterList) {
      if (updatedClusters.contains(cluster.toBean(Cluster.class))) {
        cluster.set("updateTime", DateTime.now().toString());
        updatedClusters.set(cluster.getInt("id"), cluster.toBean(Cluster.class));
      }
    }
    return commitChange(updatedClusters);
  }

  @Override
  public JSONObject selectClusterById(BigInteger id) {
    return JSONUtil.parseObj(clusters.get(id.intValue()));
  }

  //  @Override
//  public List<Cluster1> selectAllHealthyCluster() {
//    List<Cluster1> healthyClusters = new ArrayList<>();
//    for (Cluster1 cluster : clusters) {
//      if (cluster.isHealthy()) {
//        healthyClusters.add(cluster);
//      }
//    }
//    return healthyClusters;
//  }
//
//  @Override
//  public BigInteger selectFreeSpaceByCluster(String clusterName) {
//    for (Cluster1 cluster : clusters) {
//      if (cluster.getName().equals(clusterName)) {
//        return cluster.getTotalSpace().subtract(cluster.getUsedSpace());
//      }
//    }
//    return BigInteger.ZERO;
//  }
//
//  @Override
//  @SuppressWarnings({"unchecked", "rawtypes"})
//  public void updateClusterBatch(List<Cluster1> clusters) {
//    try {
//      EthSendTransaction response = Web3JUtil.sendTransaction("updateClusterBatch",
//          Collections.singletonList(new DynamicArray(DynamicStruct.class, clusters)));
//      if (response.getError() == null) {
//        log.info("Transaction succeeded: " + response.getResult());
//        this.clusters = clusters;
//      } else {
//        log.error("Transaction encountered error: " + response.getError().getMessage());
//      }
//    } catch (ExecutionException | InterruptedException e) {
//      e.printStackTrace();
//    }
//  }

  private boolean commitChange(List<Cluster> updatedClusters) {
    String clusterDecodedStr = JSONUtil.parseArray(updatedClusters).toString();
    clusterDecodedStr = EncryptUtil.aesEncrypt(clusterDecodedStr);

    try {
      EthSendTransaction response = Web3JUtil.sendTransaction("setClusters",
          Collections.singletonList(new Utf8String(clusterDecodedStr)));
      if (response.getError() == null) {
        log.info("Transaction succeeded: " + response.getResult());
        clusters = updatedClusters;
        return true;
      } else {
        log.error("Transaction encountered error: " + response.getError().getMessage());
        return false;
      }
    } catch (ExecutionException | InterruptedException e) {
      log.error("Unable to commit changes to the database");
      return false;
    }

  }
}
