package pers.yujie.dashboard.service.impl;

import cn.hutool.json.JSONObject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.yujie.dashboard.dao.ClusterDao;
import pers.yujie.dashboard.entity.Block;
import pers.yujie.dashboard.service.ClusterService;
import pers.yujie.dashboard.service.NodeService;
import pers.yujie.dashboard.utils.IPFSUtil;

/**
 * This class is responsible for providing cluster services.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see NodeService
 * @see ClusterDao
 * @since 20/01/2023
 */
@Service
@Slf4j
public class ClusterServiceImpl implements ClusterService {

  @Resource
  private ClusterDao clusterDao;
  @Resource
  private NodeService nodeService;

  /**
   * Retrieve all clusters as a list.
   *
   * @return {@link List} of {@link JSONObject} representing the clusters
   */
  @Override
  public List<JSONObject> selectAllCluster() {
    return clusterDao.selectAllCluster();
  }

  /**
   * Distribute a given website to clusters
   *
   * @param website   {@link JSONObject} containing the website information
   * @param blockList {@link List} of {@link Block}, the Merkle nodes of this website
   * @return a blank string if succeeded, an error message otherwise
   */
  @Override
  public String distributeWebsite(JSONObject website, List<Block> blockList) {
    // check that there is enough space for this website
    JSONObject minCluster = clusterDao.selectMinHealthyCluster();
    BigInteger clusterSize = minCluster.getBigInteger("totalSpace")
        .subtract(minCluster.getBigInteger("usedSpace"));
    if (clusterSize.compareTo(website.getBigInteger("size")) < 0) {
      return "Not enough storage space available";
    }
    List<JSONObject> clusters = clusterDao.selectAllCluster();
    List<BigInteger> locations = new ArrayList<>();
    // distribute this website to the nodes
    for (JSONObject cluster : clusters) {
      if (cluster.getStr("status").equals("healthy")) {
        nodeService.distribute(cluster, website, blockList);
        locations.add(cluster.getBigInteger("id"));
        cluster.set("usedSpace", cluster.getBigInteger("usedSpace")
            .add(website.getBigInteger("size")));
      }
    }
    website.set("location", locations);
    if (clusterDao.updateClusterBatch(clusters)) {
      return "";
    } else {
      return "Unable to commit changes of the clusters";
    }
  }

  /**
   * Remove an existing website from the system.
   *
   * @param website {@link JSONObject} of the specified website
   * @return true if succeeded, false otherwise
   */
  @Override
  public boolean removeWebsite(JSONObject website) {
    List<JSONObject> clusters = clusterDao.selectAllCluster();

    List<BigInteger> locations = website.getJSONArray("location").toList(BigInteger.class);
    for (JSONObject cluster : clusters) {
      if (cluster.getStr("status").equals("healthy")) {
        if (IPFSUtil.getAddress() != null) {
          nodeService.releaseWebsiteSpace(cluster, website);
        }
        cluster.set("usedSpace", cluster.getBigInteger("usedSpace")
            .subtract(website.getBigInteger("size")));
        locations.remove(cluster.getBigInteger("id"));
      }
    }
    website.set("location", locations);
    return clusterDao.updateClusterBatch(clusters);
  }
}
