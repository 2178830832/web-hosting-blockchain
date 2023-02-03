package pers.yujie.dashboard.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.ipfs.multihash.Multihash;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.yujie.dashboard.dao.ClusterDao;
import pers.yujie.dashboard.dao.NodeDao;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.service.ClusterService;
import pers.yujie.dashboard.service.NodeService;
import pers.yujie.dashboard.utils.DockerUtil;
import pers.yujie.dashboard.utils.IPFSUtil;

@Service
@Slf4j
public class ClusterServiceImpl implements ClusterService {

  @Resource
  private ClusterDao clusterDao;
  @Resource
  private NodeDao nodeDao;
  @Resource
  private NodeService nodeService;
  @Resource
  private DockerUtil dockerUtil;
  @Resource
  private IPFSUtil ipfsUtil;

  @Override
  public BigInteger assignToMinCluster(BigInteger nodeSpace) {
    BigInteger minSize = BigInteger.valueOf(Integer.MAX_VALUE);
    Cluster minCluster = null;
    List<Cluster> clusters = clusterDao.selectAllCluster();
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
    Cluster cluster = clusterDao.selectClusterById(node.getClusterId());
    JSONObject clusterObj = JSONUtil.parseObj(cluster);

    clusterObj.set("totalSpace", cluster.getTotalSpace().subtract(node.getTotalSpace()));
    clusterObj.set("usedSpace", cluster.getUsedSpace().subtract(node.getUsedSpace()));
    clusterDao.updateCluster(clusterObj);
  }

  @Override
  public void distributeWebsite(JSONObject website) {
    // do something
  }

  @Override
  public void removeWebsite(Website website) {
    // do something
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
