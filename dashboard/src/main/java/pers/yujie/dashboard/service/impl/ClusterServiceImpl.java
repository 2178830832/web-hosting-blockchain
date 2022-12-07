package pers.yujie.dashboard.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import pers.yujie.dashboard.dao.ClusterMapper;
import pers.yujie.dashboard.dao.NodeMapper;
import pers.yujie.dashboard.dao.WebsiteMapper;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.service.ClusterService;
import pers.yujie.dashboard.service.NodeService;
import pers.yujie.dashboard.utils.DockerUtil;
import org.springframework.stereotype.Service;

@Service
public class ClusterServiceImpl implements ClusterService {

  @Resource
  private ClusterMapper clusterMapper;
  @Resource
  private NodeMapper nodeMapper;

  @Resource
  private WebsiteMapper websiteMapper;

  @Resource
  private NodeService nodeService;

  @Resource
  private DockerUtil dockerUtil;

  @PostConstruct
  private void initClusterService() {
//    List<Cluster> clusters = clusterMapper.selectAllCluster();
//    if (clusters.size() < 3) {
//      createInitialCluster();
//    }
//    for (Cluster cluster : clusters) {
//      BigInteger clusterId = cluster.getClusterId();
//      if (nodeMapper.selectHealthyByClusterId(clusterId).size() < 1) {
//        nodeService.createInitNode(clusterId);
//        clusterMapper.updateClusterSpace(clusterId);
//      }
//    }
//    checkClusterStatus();
  }

  private void createInitialCluster() {
    for (int i = 0; i < 3; i++) {
      BigInteger index = BigInteger.valueOf(i);
      Cluster cluster = new Cluster(index);
      clusterMapper.insertCluster(cluster);
      nodeService.createInitNode(index);
      clusterMapper.updateClusterSpace(index);
    }
  }

  @Override
  public void checkClusterStatus() {
    List<Cluster> clusters = clusterMapper.selectAllHealthyCluster();
    for (Cluster cluster : clusters) {
      List<Node> nodes = nodeMapper.selectHealthyByClusterId(cluster.getClusterId());
      List<String> containerNameList = dockerUtil.getNameContainerList();

      boolean is_updated = false;
      for (Node node : nodes) {
        String nodeName = "/" + node.getNodeName();
        if (!containerNameList.contains(nodeName) && node.isHealthy()) {
          dockerUtil.startContainer(nodeName);
          node.setUpdateTime(new Date());
          is_updated = true;
        }
      }
      if (is_updated) {
        nodeMapper.updateNodeBatch(nodes);
      }
    }
  }

  @Override
  public String distributeWebsite(String cid) {
    List<Cluster> clusters = clusterMapper.selectAllHealthyCluster();
    List<BigInteger> clusterIdList = new ArrayList<>();

    for (Cluster cluster : clusters) {
      nodeService.distributeWebsite(cluster.getClusterId(), cid);
      clusterIdList.add(cluster.getClusterId());
    }

    return JSON.toJSONString(clusterIdList);
  }

  @Override
  public List<BigInteger> removeWebsite(String cid) {
    List<Cluster> clusters = clusterMapper.selectAllHealthyCluster();
    List<BigInteger> clusterIdList = new ArrayList<>();
    for (Cluster cluster : clusters) {
      nodeService.removeWebsite(cluster.getClusterId(), cid);
      clusterIdList.add(cluster.getClusterId());
    }

    return clusterIdList;
  }

  @Override
  public void updateClusterContent(Cluster cluster) {
    List<Website> websites = websiteMapper.selectAllWebsite();
    BigInteger clusterId = cluster.getClusterId();
    for (Website website : websites) {
      List<BigInteger> clusterIdList = JSON.parseArray(
          website.getClusterStatus(), BigInteger.class);

      if (website.isOnline() && !clusterIdList.contains(clusterId)) {
        nodeService.distributeWebsite(clusterId, website.getCid());
      } else if (!website.isOnline() && clusterIdList.contains(clusterId)){
        nodeService.removeWebsite(clusterId, website.getCid());
      }
    }
  }
}
