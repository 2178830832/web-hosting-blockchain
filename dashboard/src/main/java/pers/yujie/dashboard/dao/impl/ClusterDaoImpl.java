package pers.yujie.dashboard.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Repository;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import pers.yujie.dashboard.dao.ClusterDao;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.utils.AppUtil;
import pers.yujie.dashboard.utils.Web3JUtil;

@Repository
@Slf4j
public class ClusterDaoImpl implements ClusterDao {

  private List<Cluster> clusters;

  @Resource
  private ApplicationContext ctx;

  @PostConstruct
  @SuppressWarnings({"unchecked", "rawtypes"})
  private void initClusterDao() {
    try {
      clusters = (List) Web3JUtil.sendQuery("selectAllClusters",
          Collections.emptyList(),
          Collections.singletonList(new TypeReference<DynamicArray<Cluster>>() {
          })).get(0).getValue();
    } catch (ExecutionException | InterruptedException e) {
      log.info("Unable to request local ganache server");
      AppUtil.exitApplication(ctx, 1);
    }
  }

  @Override
  public List<Cluster> selectAllHealthyCluster() {
    List<Cluster> healthyClusters = new ArrayList<>();
    for (Cluster cluster : clusters) {
      if (cluster.isHealthy()) {
        healthyClusters.add(cluster);
      }
    }
    return healthyClusters;
  }

  @Override
  public BigInteger selectFreeSpaceByCluster(String clusterName) {
    for (Cluster cluster : clusters) {
      if (cluster.getName().equals(clusterName)) {
        return cluster.getTotalSpace().subtract(cluster.getUsedSpace());
      }
    }
    return BigInteger.ZERO;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void updateClusterBatch(List<Cluster> clusters) {
    try {
      EthSendTransaction response = Web3JUtil.sendTransaction("updateClusterBatch",
          Collections.singletonList(new DynamicArray(DynamicStruct.class, clusters)));
      if (response.getError() == null) {
        log.info("Transaction receipt: " + response.getResult());
        this.clusters = clusters;
      } else {
        log.warn("Transaction encountered error: " + response.getError().toString());
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
