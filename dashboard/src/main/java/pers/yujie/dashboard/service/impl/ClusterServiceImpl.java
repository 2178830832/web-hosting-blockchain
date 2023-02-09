package pers.yujie.dashboard.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.yujie.dashboard.dao.ClusterDao;
import pers.yujie.dashboard.entity.Block;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.service.ClusterService;
import pers.yujie.dashboard.service.NodeService;

@Service
@Slf4j
public class ClusterServiceImpl implements ClusterService {

  @Resource
  private ClusterDao clusterDao;

  @Resource
  private NodeService nodeService;

  @Override
  public List<JSONObject> selectAllCluster() {
    return clusterDao.selectAllCluster();
  }

  @Override
  public BigInteger assignToMinCluster(BigInteger nodeSpace) {
    BigInteger minSize = BigInteger.valueOf(Integer.MAX_VALUE);
    Cluster minCluster = null;
//    List<Cluster> clusters = clusterDao.selectAllCluster();
    List<Cluster> clusters = new ArrayList<>();
    for (Cluster cluster : clusters) {
      BigInteger freeSpace = cluster.getTotalSpace().subtract(cluster.getUsedSpace());
      if (freeSpace.compareTo(minSize) < 0) {
        minSize = freeSpace;
        minCluster = cluster;
      }
    }
    assert minCluster != null;
    JSONObject clusterObj = JSONUtil.parseObj(minCluster);
    clusterObj.set("totalSpace", minCluster.getTotalSpace().add(nodeSpace));
    clusterDao.updateCluster(clusterObj);
    return minCluster.getId();
  }

  @Override
  public void deleteNodeSpace(Node node) {
    JSONObject cluster = clusterDao.selectClusterById(node.getClusterId());

    cluster.set("totalSpace", cluster.getBigInteger("totalSpace").subtract(node.getTotalSpace()));
    cluster.set("usedSpace", cluster.getBigInteger("usedSpace").subtract(node.getUsedSpace()));
    clusterDao.updateCluster(cluster);
  }

  @Override
  public String distributeWebsite(JSONObject website, List<Block> blockList) {

    JSONObject minCluster = clusterDao.selectMinHealthyCluster();
    BigInteger clusterSize = minCluster.getBigInteger("totalSpace")
        .subtract(minCluster.getBigInteger("usedSpace"));
    if (clusterSize.compareTo(website.getBigInteger("size")) < 0) {
      return "Not enough storage space available";
    }
    List<JSONObject> clusters = clusterDao.selectAllCluster();
    List<BigInteger> locations = new ArrayList<>();
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

  @Override
  public void removeWebsite(JSONObject website) {
    List<JSONObject> clusters = clusterDao.selectAllCluster();

    List<BigInteger> locations = website.getJSONArray("location").toList(BigInteger.class);
    for (JSONObject cluster : clusters) {
      if (cluster.getStr("status").equals("healthy")) {
        nodeService.releaseWebsiteSpace(cluster, website);
        cluster.set("usedSpace", cluster.getBigInteger("usedSpace")
            .subtract(website.getBigInteger("size")));
        locations.remove(cluster.getBigInteger("id"));
      }
    }
    website.set("location", locations);
    clusterDao.updateClusterBatch(clusters);

  }

  //
//  @PostConstruct
//  private void initClusterService() {
//    List<Cluster> clusters = clusterDao.selectAllHealthyCluster();
//    for (Cluster cluster : clusters) {
//      List<Node> nodes = nodeDao.selectOnlineByCluster(cluster.getName());
//      List<String> containerNameList = dockerUtil.getNameContainerList();
//
//      for (Node node : nodes) {
//        String nodeName = "/" + node.getName();
//        if (!containerNameList.contains(nodeName) && node.isOnline()) {
//          dockerUtil.startContainer(nodeName);
//        }
//      }
//    }
//  }

//  @Override
//  public String distributeWebsite(String cid) {
//    List<Cluster> clusters = clusterDao.selectAllHealthyCluster();
//    List<String> clusterNames = new ArrayList<>();
//
//    for (Cluster cluster : clusters) {
//      BigInteger websiteSize = distributeManager(cluster.getName(), cid);
//      cluster.setUsedSpace(cluster.getUsedSpace().add(websiteSize));
//      clusterNames.add(cluster.getName());
//    }
//    clusterDao.updateClusterBatch(clusters);
//    return JSON.toJSONString(clusterNames);
//  }

//  private BigInteger distributeManager(String clusterName, String cid) {
//    try {
//      List<Multihash> blockList = ipfsUtil.getBlockHashList(cid);
//      BigInteger blockListSize = BigInteger.valueOf(blockList.size());
//      if (blockListSize.compareTo(clusterDao.selectFreeSpaceByCluster(clusterName)) > 0) {
//        log.info("Not enough storage space");
//        return BigInteger.ZERO;
//      }
//      nodeService.distributeBlockList(clusterName, blockList, cid);
//      return blockListSize;
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    return BigInteger.ZERO;
//  }

//  @Override
//  public List<BigInteger> removeWebsite(String cid) {
//    List<Cluster> clusters = clusterDao.selectAllHealthyCluster();
//    List<BigInteger> clusterIdList = new ArrayList<>();
//    for (Cluster cluster : clusters) {
//      nodeService.removeWebsite(cluster.getClusterId(), cid);
//      clusterIdList.add(cluster.getClusterId());
//    }
//
//    return clusterIdList;
//    return new ArrayList<>();
//  }

//  @Override
//  public void updateClusterContent(Cluster cluster) {
//    List<Website> websites = websiteMapper.selectAllWebsite();
//    BigInteger clusterId = cluster.getClusterId();
//    for (Website website : websites) {
//      List<BigInteger> clusterIdList = JSON.parseArray(
//          website.getClusterStatus(), BigInteger.class);
//
//      if (website.isOnline() && !clusterIdList.contains(clusterId)) {
//        nodeService.distributeWebsite(clusterId, website.getCid());
//      } else if (!website.isOnline() && clusterIdList.contains(clusterId)){
//        nodeService.removeWebsite(clusterId, website.getCid());
//      }
//    }
//  }
}
