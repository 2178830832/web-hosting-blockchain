package pers.yujie.dashboard.service.impl;

import cn.hutool.json.JSONObject;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.yujie.dashboard.dao.NodeDao;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.service.BlockService;
import pers.yujie.dashboard.service.ClusterService;
import pers.yujie.dashboard.service.NodeService;

@Service
@Slf4j
public class NodeServiceImpl implements NodeService {

  @Resource
  private NodeDao nodeDao;

  @Resource
  private ClusterService clusterService;

  @Resource
  private BlockService blockService;

//  private String cid;
//  private List<Node> nodes;
//  private Node masterNode;
//  private List<Multihash> blockList;

  @Override
  public List<Node> selectAllNode() {
    return nodeDao.selectAllNode();
  }

  @Override
  public String updateNode(JSONObject node) {
    BigInteger id = node.getBigInteger("id");
    String name = node.getStr("name");
    BigInteger totalSpace = node.getBigInteger("totalSpace");

    if (id == null || name == null || totalSpace == null) {
      return "Unexpected Json format";
    }
    if (totalSpace.compareTo(BigInteger.ONE) < 0) {
      return "Node space must be a positive number";
    }
    Node upNode = nodeDao.selectNodeById(id);
    if (totalSpace.compareTo(upNode.getUsedSpace()) < 0) {
      blockService.redistributeNodeSize(id, upNode.getUsedSpace().subtract(totalSpace));
    }
    if (nodeDao.updateNode(node)) {
      return "";
    } else {
      return "Unable to commit changes to the database";
    }
  }

  @Override
  public String insertNode(JSONObject node) {
    String name = node.getStr("name");
    String totalSpace = node.getStr("totalSpace");

    if (name == null || totalSpace == null) {
      return "Unexpected Json format";
    }
    if ((new Integer(totalSpace)) < 1) {
      return "Node space must be a positive number";
    }
    node.set("clusterId", clusterService.assignToMinCluster(new BigInteger(totalSpace)));
    // docker conn
    if (nodeDao.insertNode(node)) {
      return "";
    } else {
      return "Unable to commit changes to the database";
    }
  }

  @Override
  public String deleteNode(BigInteger id) {
    Node node = nodeDao.selectNodeById(id);
    node.setStatus("deleted");
//    clusterService.deleteNodeSpace(node);
    // redistribute block
    // remove docker
    if (nodeDao.deleteNode(id)) {
      return "";
    } else {
      return "Unable to commit changes to the database";
    }
  }

  //  @Override
//  public void distributeBlockList(String clusterName, List<Multihash> blockList, String cid) {
//    nodes = nodeDao.selectOnlineByCluster(clusterName);
//    masterNode = nodes.get(0);
//    this.blockList = blockList;
//    this.cid = cid;
//
//    try {
//      distributePointerBlocks();
//      distributeStorageBlocks();
//      nodeDao.updateNodeBatchByCluster(nodes);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  private void distributePointerBlocks() throws IOException {
//    List<Multihash> pointerHashList = ipfsUtil.getPointerHashList(blockList, cid);
//
//    for (Multihash pointerHash : pointerHashList) {
//      dockerUtil.execDockerCmd(masterNode.getName(), Constants.IPFS_PREFIX,
//          "block", "pin", "add", "--recursive=false", pointerHash.toString());
//    }
//    dockerUtil.execDockerCmd(masterNode.getName(), Constants.IPFS_PREFIX, "repo", "gc");
//    BigInteger pointerSize = BigInteger.valueOf(pointerHashList.size());
//    masterNode.setUsedSpace(masterNode.getUsedSpace().add(pointerSize));
//    blockList.removeAll(pointerHashList);
//  }
//
//  private void distributeStorageBlocks() {
//    List<List<Multihash>> partitions = partitionBlockList();
//    int i = 0;
//    for (List<Multihash> part : partitions) {
//      Node node = nodes.get(i++);
//      String nodeName = node.getName();
//      for (Multihash hash : part) {
//
//        String blockString = hash.toString();
//        dockerUtil.execDockerCmd(nodeName, Constants.IPFS_PREFIX,
//            "block", "pin", "add", "--recursive=false", blockString);
//        dockerUtil.execDockerCmd(nodeName, Constants.IPFS_PREFIX, "repo", "gc");
//      }
//      node.setUsedSpace(node.getUsedSpace().add(BigInteger.valueOf(part.size())));
//    }
//  }
//
//  private List<List<Multihash>> partitionBlockList() {
//    List<List<Multihash>> partitionList = new ArrayList<>();
//    int maxStorage = 0;
//
//    for (Node node : nodes) {
//      maxStorage += (node.getTotalSpace().subtract(node.getUsedSpace())).intValue();
//    }
//    List<Multihash> copyBlockList = blockList;
//    DecimalFormat df = new DecimalFormat("0.00");
//    for (Node node : nodes) {
//      if (node.getName().equals(nodes.get(nodes.size() - 1).getName())) {
//        partitionList.add(copyBlockList);
//        break;
//      }
//      double weight = Double.parseDouble(df.format(
//          (node.getTotalSpace().subtract(node.getUsedSpace())).intValue() / maxStorage));
//      int pivot = Math.round((float) (blockList.size() * weight));
//      List<Multihash> singleList = copyBlockList.subList(0, pivot);
//      copyBlockList = copyBlockList.subList(pivot, copyBlockList.size());
//      partitionList.add(singleList);
//    }
//
//    return partitionList;
//  }
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