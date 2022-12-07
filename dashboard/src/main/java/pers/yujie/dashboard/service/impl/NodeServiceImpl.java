package pers.yujie.dashboard.service.impl;

import io.ipfs.api.MerkleNode;
import io.ipfs.multihash.Multihash;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import pers.yujie.dashboard.Common.Constants;
import pers.yujie.dashboard.dao.ClusterMapper;
import pers.yujie.dashboard.dao.NodeMapper;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.service.ClusterService;
import pers.yujie.dashboard.service.NodeService;
import pers.yujie.dashboard.utils.DockerUtil;
import pers.yujie.dashboard.utils.IPFSUtil;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NodeServiceImpl implements NodeService {

  @Resource
  private NodeMapper nodeMapper;

  @Resource
  private ClusterMapper clusterMapper;

  @Resource
  private IPFSUtil ipfsUtil;

  @Resource
  private DockerUtil dockerUtil;

//  @Resource
//  private ClusterService clusterService;

  private String cid;
  private List<Node> nodeList;
  private Node masterNode;
  private List<Multihash> blockList;

  private void initNode(BigInteger cluster_id, String cid) {
    this.cid = cid;
    nodeList = nodeMapper.selectHealthyByClusterId(cluster_id);
    masterNode = nodeMapper.selectMasterNodeByCluster(cluster_id);
  }

  @Override
  public void createInitNode(BigInteger cluster_id) {
    Node node = new Node(nodeMapper.selectMaxNodeId().add(BigInteger.ONE), cluster_id);
    nodeMapper.insertNode(node);
  }

  private void setMasterNode(BigInteger cluster_id) {
    masterNode = nodeMapper.selectMaxNodeByClusterId(cluster_id).get(0);
    masterNode.setMaster(true);
    masterNode.setUpdateTime(new Date());
    nodeMapper.updateNode(masterNode);
  }

  public void distributeWebsite(BigInteger cluster_id, String cid) {
    initNode(cluster_id, cid);
    if (masterNode == null) {
      log.info("No master node in this cluster");
      setMasterNode(cluster_id);
    }

    try {
      blockList = ipfsUtil.getBlockList(cid);
      if (blockList.size() >= nodeMapper.selectAllFreeSpace()) {
        log.info("Not enough storage space");
        return;
      }
      distributeMasterNode();
      distributeBlock();
      clusterMapper.updateClusterSpace(cluster_id);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void distributeMasterNode() throws IOException, InterruptedException {

    List<Multihash> mainHashList = ipfsUtil.getMainHashList(blockList, cid);

    for (Multihash mainHash : mainHashList) {
//        cmd = "docker exec -it ipfs" + masterNode.getNodeId()
//            + " ipfs block pin add --recursive=false " + mainHash;
//        linuxUtil.executeCmd(cmd);
      dockerUtil.execDockerCmd(masterNode.getNodeName(), Constants.IPFS_PREFIX,
          "block", "pin", "add", "--recursive=false", mainHash.toString());
    }

//      cmd = "docker exec -it ipfs" + masterNode.getNodeId() + " ipfs repo gc";
//      linuxUtil.executeCmd(cmd);
    dockerUtil.execDockerCmd(masterNode.getNodeName(), Constants.IPFS_PREFIX, "repo", "gc");
    masterNode.setUsedSpace(masterNode.getUsedSpace() + mainHashList.size());
    masterNode.setUpdateTime(new Date());
  }

  private void distributeBlock() throws InterruptedException {
    List<List<Multihash>> partitions = partitionBlockList();
    int i = 0;
    for (List<Multihash> blocks : partitions) {
      Node node = nodeList.get(i++);
      String nodeName = node.getNodeName();
      for (Multihash block : blocks) {

        String blockString = block.toString();
//        dockerUtil.execDockerCmd(nodeName, Constants.IPFS_PREFIX, "get", blockString);
        dockerUtil.execDockerCmd(nodeName, Constants.IPFS_PREFIX,
            "block", "pin", "add", "--recursive=false",
            blockString);
        dockerUtil.execDockerCmd(nodeName, Constants.IPFS_PREFIX, "repo", "gc");
//        cmd = "docker exec -it ipfs" + nodeId + " ipfs get " + block.hash.toString();
//        linuxUtil.executeCmd(cmd);
//        cmd = "docker exec -it ipfs" + nodeId
//            + " ipfs pin add --recursive=false " + block.hash.toString();
//        linuxUtil.executeCmd(cmd);
      }
      node.setUsedSpace(node.getUsedSpace() + blocks.size());
      node.setUpdateTime(new Date());
    }
    nodeMapper.updateNodeBatch(nodeList);
  }

  private List<List<Multihash>> partitionBlockList() {
    List<List<Multihash>> partitionList = new ArrayList<>();
    int maxStorage = 0;

    for (Node node : nodeList) {
      maxStorage += node.getTotalSpace() - node.getUsedSpace();
    }
    List<Multihash> copyBlockList = blockList;
    DecimalFormat df = new DecimalFormat("0.00");
    for (Node node : nodeList) {
      if (node.getNodeId().equals(nodeList.get(nodeList.size() - 1).getNodeId())) {
        partitionList.add(copyBlockList);
        break;
      }
      double weight = Double.parseDouble(df.format(
          (node.getTotalSpace() - node.getUsedSpace()) / maxStorage));
      int pivot = Math.round((float) (blockList.size() * weight));
      List<Multihash> singleList = copyBlockList.subList(0, pivot);
      copyBlockList = copyBlockList.subList(pivot, copyBlockList.size());
      partitionList.add(singleList);
    }

    return partitionList;
  }

  @Override
  public void removeWebsite(BigInteger cluster_id, String cid) {
    initNode(cluster_id, cid);
    if (masterNode == null) {
      log.info("There is no master node");
      return;
    }
    try {
      removeBlock(IPFSUtil.getRefList(cid));
      clusterMapper.updateClusterSpace(cluster_id);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

  }

  private void removeBlock(List<Multihash> refList) throws InterruptedException {
    StringBuilder builder = new StringBuilder();
    for (Multihash ref : refList) {
      builder.append(ref.toString());
      builder.append(" ");
    }
    String refHashList = builder.toString();
    for (Node node : nodeList) {
      dockerUtil.execDockerCmd(node.getNodeName(), Constants.IPFS_PREFIX,
          "block", "rm", "-f", refHashList);
      dockerUtil.execDockerCmd(node.getNodeName(), Constants.IPFS_PREFIX, "repo", "gc");
//      node.setUsedSpace(node.getUsedSpace() - 0);
      node.setUpdateTime(new Date());
//      cmd = "docker exec -it ipfs" + node.getNodeId() + " ipfs block rm -f " + refHashList;
//      linuxUtil.executeCmd(cmd);
    }
    nodeMapper.updateNodeBatch(nodeList);
  }

  @Override
  public void offlineNode(Node offNode) {

    updateNodeHelper(offNode, false, false);
  }

  public void onlineNode(Node onNode) {
    Cluster cluster = clusterMapper.selectByClusterId(onNode.getClusterId());
    nodeList = nodeMapper.selectByClusterId(cluster.getClusterId());
    nodeList.removeAll(nodeMapper.selectHealthyByClusterId(cluster.getClusterId()));
    if (nodeList.size() > 1) {
//      clusterService.updateClusterContent(cluster);
      updateNodeHelper(onNode, true, false);
    } else {
      updateNodeHelper(onNode, true, true);
    }
  }

  private void updateNodeHelper(Node node, boolean turnOnNode, boolean turnOnCluster) {
    node.setUpdateTime(new Date());
    node.setHealthy(turnOnNode);
    nodeMapper.updateNode(node);
    Cluster cluster = clusterMapper.selectByClusterId(node.getClusterId());
    cluster.setUpdateTime(new Date());
    cluster.setHealthy(turnOnCluster);
    clusterMapper.updateCluster(cluster);
  }

  public void registerNode(Node inNode) {
    Cluster cluster = clusterMapper.selectMinCluster().get(0);
    inNode.setClusterId(cluster.getClusterId());
    inNode.setNodeId(nodeMapper.selectMaxNodeId().add(BigInteger.ONE));
    inNode.setUpdateTime(new Date());
    inNode.setCreateTime(new Date());

    cluster.setUpdateTime(new Date());
    cluster.setTotalSpace(cluster.getTotalSpace() + inNode.getTotalSpace());
    clusterMapper.updateCluster(cluster);
    nodeMapper.insertNode(inNode);
//    clusterService.checkClusterStatus();
  }


  public boolean removeDamagedNode(Node rmNode) {
    if (!rmNode.isHealthy()) {
      return false;
    }
    rmNode.setHealthy(false);
    nodeList = nodeMapper.selectHealthyByClusterId(rmNode.getClusterId());
    if (rmNode.isMaster()) {

    }
    rmNode.setUpdateTime(new Date());
    nodeMapper.updateNode(rmNode);
    return true;
  }

  private boolean removeMasterNode() {
    for (Node node : nodeList) {
//      if (!node.isMaster()) {
//        masterNodeName = node.getNodeName();
//        break;
//      }
//      if (masterNodeName == null) {
//        return false;
//      }
//      rmNode.setMaster(false);
    }
    return true;
  }
}