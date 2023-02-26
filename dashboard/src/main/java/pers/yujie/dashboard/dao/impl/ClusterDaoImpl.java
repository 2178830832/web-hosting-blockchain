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

/**
 * This is the Data Access Object (Dao) class for {@link Cluster}.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see BaseDaoImpl
 * @since 29/12/2022
 */
@Repository
@Slf4j
public class ClusterDaoImpl extends BaseDaoImpl implements ClusterDao {

  private List<Cluster> clusters = new ArrayList<>();

  /**
   * Select all clusters as a list.
   *
   * @return a {@link List} of {@link JSONObject} representing the clusters
   */
  @Override
  public List<JSONObject> selectAllCluster() {
    List<JSONObject> clusterList = new ArrayList<>();
    for (Cluster cluster : clusters) {
      clusterList.add(JSONUtil.parseObj(cluster));
    }
    return clusterList;
  }

  /**
   * Select the cluster with the smallest available space.
   *
   * @return a {@link JSONObject} of the selected cluster
   */
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

  /**
   * Select the cluster which is healthy and has the smallest available space.
   *
   * @return a {@link JSONObject} of the selected cluster.
   */
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

  /**
   * Initialise this class by reading from the Ganache and decrypting with {@link EncryptUtil}.
   */
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
      clusterEncodedStr = EncryptUtil.decryptAES(clusterEncodedStr);
      clusters = JSONUtil.toList(JSONUtil.parseArray(clusterEncodedStr), Cluster.class);
    } catch (IndexOutOfBoundsException | ExecutionException | InterruptedException e) {
      log.error("Unable to retrieve cluster list from the data source");
    }
  }

  /**
   * A helper method to create initial three clusters if the data source is empty.
   */
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

  /**
   * Update a specific cluster.
   *
   * @param cluster {@link JSONObject} representing the cluster to be updated
   * @return true if succeeded, false otherwise
   */
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

  /**
   * Batch-update the clusters.
   *
   * @param clusterList a {@link List} of {@link JSONObject} representing the clusters
   * @return true if succeeded, false otherwise
   */
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

  /**
   * Select a cluster given its ID.
   *
   * @param id {@link BigInteger} of the specified cluster ID.
   * @return a {@link JSONObject} representing the cluster.
   */
  @Override
  public JSONObject selectClusterById(BigInteger id) {
    return JSONUtil.parseObj(clusters.get(id.intValue()));
  }

  /**
   * Encrypt the clusters with symmetric algorithm and commit it to the data source.
   *
   * @param updatedClusters a {@link List} of {@link Cluster}
   * @return true if succeeded, false otherwise
   */
  private boolean commitChange(List<Cluster> updatedClusters) {
    String clusterDecodedStr = JSONUtil.parseArray(updatedClusters).toString();
    clusterDecodedStr = EncryptUtil.encryptAES(clusterDecodedStr);

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
      log.error("Unable to commit changes to the data source");
      return false;
    }

  }
}
