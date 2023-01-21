package pers.yujie.dashboard.dao.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import pers.yujie.dashboard.dao.ClusterDao;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.utils.Web3JUtil;

@Repository
@Slf4j
public class ClusterDaoImpl implements ClusterDao {

  private List<Cluster> clusters = new ArrayList<>();

  @Override
  public List<Cluster> selectAllCluster() {
    return clusters;
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
      clusters = JSONUtil.toList(JSONUtil.parseArray(clusterEncodedStr), Cluster.class);
    } catch (ExecutionException | InterruptedException e) {
      log.error("Unable to retrieve cluster list from the database");
    }
  }

  private void createInitialClusters() {
    if (clusters.size() < 4) {
      List<Cluster> updatedClusters = new ArrayList<>(clusters);
      for (int i = 0; i < 3; i++) {
        Cluster cluster = new Cluster(BigInteger.valueOf(i), "cluster" + i, "healthy",
            BigInteger.ZERO, BigInteger.ZERO);
        updatedClusters.add(cluster);
      }
      commitChange(updatedClusters);
    }
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
