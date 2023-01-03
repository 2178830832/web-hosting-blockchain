package pers.yujie.dashboard.service.impl;

import io.ipfs.multihash.Multihash;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.yujie.dashboard.common.Constants;
import pers.yujie.dashboard.dao.NodeDao;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.service.NodeService;
import pers.yujie.dashboard.utils.DockerUtil;
import pers.yujie.dashboard.utils.IPFSUtil;

@Service
@Slf4j
public class NodeServiceImpl implements NodeService {

  @Resource
  private IPFSUtil ipfsUtil;
  @Resource
  private DockerUtil dockerUtil;
  @Resource
  private NodeDao nodeDao;

  private String cid;
  private List<Node> nodes;
  private Node masterNode;
  private List<Multihash> blockList;

  @Override
  public void distributeBlockList(String clusterName, List<Multihash> blockList, String cid) {
    nodes = nodeDao.selectOnlineByCluster(clusterName);
    masterNode = nodes.get(0);
    this.blockList = blockList;
    this.cid = cid;

    try {
      distributePointerBlocks();
      distributeStorageBlocks();
      nodeDao.updateNodeBatchByCluster(nodes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void distributePointerBlocks() throws IOException {
    List<Multihash> pointerHashList = ipfsUtil.getPointerHashList(blockList, cid);

    for (Multihash pointerHash : pointerHashList) {
      dockerUtil.execDockerCmd(masterNode.getName(), Constants.IPFS_PREFIX,
          "block", "pin", "add", "--recursive=false", pointerHash.toString());
    }
    dockerUtil.execDockerCmd(masterNode.getName(), Constants.IPFS_PREFIX, "repo", "gc");
    BigInteger pointerSize = BigInteger.valueOf(pointerHashList.size());
    masterNode.setUsedSpace(masterNode.getUsedSpace().add(pointerSize));
    blockList.removeAll(pointerHashList);
  }

  private void distributeStorageBlocks() {
    List<List<Multihash>> partitions = partitionBlockList();
    int i = 0;
    for (List<Multihash> part : partitions) {
      Node node = nodes.get(i++);
      String nodeName = node.getName();
      for (Multihash hash : part) {

        String blockString = hash.toString();
        dockerUtil.execDockerCmd(nodeName, Constants.IPFS_PREFIX,
            "block", "pin", "add", "--recursive=false", blockString);
        dockerUtil.execDockerCmd(nodeName, Constants.IPFS_PREFIX, "repo", "gc");
      }
      node.setUsedSpace(node.getUsedSpace().add(BigInteger.valueOf(part.size())));
    }
  }

  private List<List<Multihash>> partitionBlockList() {
    List<List<Multihash>> partitionList = new ArrayList<>();
    int maxStorage = 0;

    for (Node node : nodes) {
      maxStorage += (node.getTotalSpace().subtract(node.getUsedSpace())).intValue();
    }
    List<Multihash> copyBlockList = blockList;
    DecimalFormat df = new DecimalFormat("0.00");
    for (Node node : nodes) {
      if (node.getName().equals(nodes.get(nodes.size() - 1).getName())) {
        partitionList.add(copyBlockList);
        break;
      }
      double weight = Double.parseDouble(df.format(
          (node.getTotalSpace().subtract(node.getUsedSpace())).intValue() / maxStorage));
      int pivot = Math.round((float) (blockList.size() * weight));
      List<Multihash> singleList = copyBlockList.subList(0, pivot);
      copyBlockList = copyBlockList.subList(pivot, copyBlockList.size());
      partitionList.add(singleList);
    }

    return partitionList;
  }
//
//  @Override
//  public void removeWebsite(BigInteger cluster_id, String cid) {
//    initNode(cluster_id, cid);
//    if (masterNode == null) {
//      log.info("There is no master node");
//      return;
//    }
//    try {
//      removeBlock(IPFSUtil.getRefList(cid));
//      clusterMapper.updateClusterSpace(cluster_id);
//    } catch (IOException | InterruptedException e) {
//      e.printStackTrace();
//    }
//
//  }
//
//  private void removeBlock(List<Multihash> refList) throws InterruptedException {
//    StringBuilder builder = new StringBuilder();
//    for (Multihash ref : refList) {
//      builder.append(ref.toString());
//      builder.append(" ");
//    }
//    String refHashList = builder.toString();
//    for (Node node : nodeList) {
//      dockerUtil.execDockerCmd(node.getNodeName(), Constants.IPFS_PREFIX,
//          "block", "rm", "-f", refHashList);
//      dockerUtil.execDockerCmd(node.getNodeName(), Constants.IPFS_PREFIX, "repo", "gc");
////      node.setUsedSpace(node.getUsedSpace() - 0);
//      node.setUpdateTime(new Date());
////      cmd = "docker exec -it ipfs" + node.getNodeId() + " ipfs block rm -f " + refHashList;
////      linuxUtil.executeCmd(cmd);
//    }
//    nodeMapper.updateNodeBatch(nodeList);
//  }
//
//  @Override
//  public void offlineNode(Node offNode) {
//
//    updateNodeHelper(offNode, false, false);
//  }
//
//  public void onlineNode(Node onNode) {
//    Cluster cluster = clusterMapper.selectByClusterId(onNode.getClusterId());
//    nodeList = nodeMapper.selectByClusterId(cluster.getClusterId());
//    nodeList.removeAll(nodeMapper.selectHealthyByClusterId(cluster.getClusterId()));
//    if (nodeList.size() > 1) {
////      clusterService.updateClusterContent(cluster);
//      updateNodeHelper(onNode, true, false);
//    } else {
//      updateNodeHelper(onNode, true, true);
//    }
//  }
//
//  private void updateNodeHelper(Node node, boolean turnOnNode, boolean turnOnCluster) {
//    node.setUpdateTime(new Date());
//    node.setHealthy(turnOnNode);
//    nodeMapper.updateNode(node);
//    Cluster cluster = clusterMapper.selectByClusterId(node.getClusterId());
//    cluster.setUpdateTime(new Date());
//    cluster.setHealthy(turnOnCluster);
//    clusterMapper.updateCluster(cluster);
//  }
//
//  public void registerNode(Node inNode) {
//    Cluster cluster = clusterMapper.selectMinCluster().get(0);
//    inNode.setClusterId(cluster.getClusterId());
//    inNode.setNodeId(nodeMapper.selectMaxNodeId().add(BigInteger.ONE));
//    inNode.setUpdateTime(new Date());
//    inNode.setCreateTime(new Date());
//
//    cluster.setUpdateTime(new Date());
//    cluster.setTotalSpace(cluster.getTotalSpace() + inNode.getTotalSpace());
//    clusterMapper.updateCluster(cluster);
//    nodeMapper.insertNode(inNode);
////    clusterService.checkClusterStatus();
//  }
//
//
//  public boolean removeDamagedNode(Node rmNode) {
//    if (!rmNode.isHealthy()) {
//      return false;
//    }
//    rmNode.setHealthy(false);
//    nodeList = nodeMapper.selectHealthyByClusterId(rmNode.getClusterId());
//    if (rmNode.isMaster()) {
//
//    }
//    rmNode.setUpdateTime(new Date());
//    nodeMapper.updateNode(rmNode);
//    return true;
//  }
//
//  private boolean removeMasterNode() {
//    for (Node node : nodeList) {
////      if (!node.isMaster()) {
////        masterNodeName = node.getNodeName();
////        break;
////      }
////      if (masterNodeName == null) {
////        return false;
////      }
////      rmNode.setMaster(false);
//    }
//    return true;
//  }
}