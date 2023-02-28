package pers.yujie.dashboard.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.yujie.dashboard.common.Constants;
import pers.yujie.dashboard.dao.ClusterDao;
import pers.yujie.dashboard.dao.NodeDao;
import pers.yujie.dashboard.entity.Block;
import pers.yujie.dashboard.service.NodeService;
import pers.yujie.dashboard.utils.DockerUtil;
import pers.yujie.dashboard.utils.IPFSUtil;

/**
 * This class is responsible for providing node services.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see NodeDao
 * @see ClusterDao
 * @since 20/01/2023
 */
@Service
@Slf4j
public class NodeServiceImpl implements NodeService {

  @Resource
  private NodeDao nodeDao;
  @Resource
  private ClusterDao clusterDao;

  private List<JSONObject> nodeList = new ArrayList<>();
  private List<Block> blockList = new ArrayList<>();
  private BigInteger blockListSize = BigInteger.ZERO;

  /**
   * Retrieve all nodes as a list.
   *
   * @return {@link List} of {@link JSONObject} representing the nodes
   */
  @Override
  public List<JSONObject> selectAllNode() {
    return nodeDao.selectAllNode();
  }

  /**
   * Update an existing node.
   *
   * @param node {@link JSONObject} of the node
   * @return a blank string if succeeded, an error message otherwise
   */
  @Override
  public String updateNode(JSONObject node) {
    BigInteger id = node.getBigInteger("id");
    BigInteger totalSpace = node.getBigInteger("totalSpace");

    // check if the node info is valid
    if (id == null || totalSpace == null) {
      return "Unexpected Json format";
    }
    if (totalSpace.compareTo(BigInteger.ONE) < 0) {
      return "Node space must be a positive number";
    }
    JSONObject upNode = nodeDao.selectNodeById(id);
    JSONObject upCluster = clusterDao.selectClusterById(upNode.getBigInteger("clusterId"));

    if (!upCluster.get("status").equals("healthy")) {
      return "Cluster not healthy";
    }

    BigInteger relocateSize = upNode.getBigInteger("usedSpace").subtract(totalSpace);
    BigInteger originalSize = upNode.getBigInteger("totalSpace");
    // if used space is greater than the updated space, then relocate files to other nodes
    if (relocateSize.compareTo(BigInteger.ZERO) > 0) {
      if (upCluster.getBigInteger("usedSpace").compareTo(relocateSize) < 0) {
        // cluster becomes overwhelmed
        return "Cluster space not enough";
      }

      List<Block> originList = upNode.getJSONArray("blockList").toList(Block.class);
      List<Block> relocateList = new ArrayList<>();
      for (Block block : originList) {
        relocateList.add(block);
        relocateSize = relocateSize.subtract(block.getSize());
        if (relocateSize.compareTo(BigInteger.ZERO) <= 0) {
          break;
        }
      }
      nodeList = new ArrayList<>();
      nodeList.add(upNode);
      blockList = relocateList;
      removeStorageBlocks();
      redistributeNode(upNode, relocateList);
      upNode.set("blockList", originList.removeAll(relocateList));
    }

    upCluster.set("totalSpace",
        upCluster.getBigInteger("totalSpace").subtract(originalSize).add(totalSpace));
    upNode.set("totalSpace", totalSpace);
    if (nodeDao.updateNode(upNode)) {
      clusterDao.updateCluster(upCluster);
      return "";
    } else {
      return "Unable to commit changes to the database";
    }
  }

  /**
   * Insert a new node.
   *
   * @param node {@link JSONObject} containing necessary information
   * @return a blank string if succeeded, an error message otherwise
   */
  @Override
  public String insertNode(JSONObject node) {
    String name = node.getStr("name");
    BigInteger totalSpace = node.getBigInteger("totalSpace");

    if (name == null || totalSpace == null) {
      return "Unexpected Json format";
    }
    if (nodeDao.selectNodeByName(name) != null) {
      return "Node name must be unique";
    }

    if (totalSpace.intValue() < 1) {
      return "Node space must be a positive number";
    }

    JSONObject minCluster = clusterDao.selectMinCluster();

    node.set("clusterId", minCluster.getBigInteger("id"));
    minCluster.set("totalSpace", minCluster.getBigInteger("totalSpace").add(totalSpace));

    if (DockerUtil.getAddress() != null) {
      DockerUtil.startContainer(node.getStr("name"));
    }

    if (nodeDao.insertNode(node)) {
      clusterDao.updateCluster(minCluster);
      return "";
    } else {
      return "Unable to commit changes to the database";
    }
  }

  /**
   * Delete an existing node.
   *
   * @param id {@link BigInteger} of the specified node ID
   * @return a blank string if succeeded, an error message otherwise
   */
  @Override
  public String deleteNode(BigInteger id) {
    JSONObject rmNode = nodeDao.selectNodeById(id);
    JSONObject rmCluster = clusterDao.selectClusterById(rmNode.getBigInteger("clusterId"));

    if (!rmCluster.get("status").equals("healthy")) {
      return "Cluster not healthy";
    }

    BigInteger clusterSize = rmCluster.getBigInteger("totalSpace")
        .subtract(rmNode.getBigInteger("totalSpace"));
    if (rmCluster.getBigInteger("usedSpace").compareTo(clusterSize) > 0) {
      // cluster becomes overwhelmed
      return "Cluster space not enough";
    }

    if (DockerUtil.getAddress() != null) {
      DockerUtil.removeContainer(rmNode.getStr("name"));
    }
    rmCluster.set("totalSpace", clusterSize);
    redistributeNode(rmNode, rmNode.getJSONArray("blockList").toList(Block.class));

    if (nodeDao.deleteNode(id)) {
      clusterDao.updateCluster(rmCluster);
      return "";
    } else {
      return "Unable to commit changes to the database";
    }
  }

  /**
   * Redistribute a list of blocks to other nodes when deleting a node
   *
   * @param node      {@link JSONObject} representing the node to be deleted
   * @param blockList {@link List} of {@link Block} contained in the deleted node
   */
  private void redistributeNode(JSONObject node, List<Block> blockList) {
    nodeList = nodeDao.selectNodeByCluster(node.getBigInteger("clusterId"));
    for (JSONObject nodeObj : nodeList) {
      if (nodeObj.get("id").equals(node.get("id"))) {
        nodeList.remove(nodeObj);
        break;
      }
    }
    this.blockList = blockList;
    BigInteger listSize = BigInteger.ZERO;
    for (Block block : blockList) {
      listSize = listSize.add(block.getSize());
    }
    blockListSize = listSize;
    distributeStorageBlocks();
    nodeDao.updateNodeBatch(nodeList);
  }

  /**
   * Distribute a website to nodes in a cluster
   *
   * @param cluster   the specified cluster
   * @param website   the website to be distributed
   * @param blockList {@link List} of Merkel nodes obtained from the website Merkel tree
   */
  @Override
  public void distribute(JSONObject cluster, JSONObject website, List<Block> blockList) {
    nodeList = nodeDao.selectNodeByCluster(cluster.getBigInteger("id"));
    this.blockList = blockList;
    blockListSize = website.getBigInteger("size");
    distributeStorageBlocks();
    nodeDao.updateNodeBatch(nodeList);
  }

  /**
   * Free occupied space in a cluster when deleting a website
   *
   * @param cluster the specified cluster
   * @param website the deleted website
   */
  @Override
  public void releaseWebsiteSpace(JSONObject cluster, JSONObject website) {
    try {
      nodeList = nodeDao.selectNodeByCluster(cluster.getBigInteger("id"));
      blockList = IPFSUtil.getBlockList(website.getStr("cid"));
      removeStorageBlocks();
      nodeDao.updateNodeBatch(nodeList);
    } catch (IOException e) {
      log.error("Unable to connect to IPFS");
    }
  }

  /**
   * Change a node to online or offline
   *
   * @param id {@link BigInteger} of the node ID
   * @return a blank string if succeeded, false otherwise
   */
  @Override
  public String changeNodeStatus(BigInteger id) {
    JSONObject stNode = nodeDao.selectNodeById(id);
    JSONObject stCluster = clusterDao.selectClusterById(stNode.getBigInteger("clusterId"));

    String status = stNode.getStr("status");
    if (status.equals("online")) {
      stNode.set("status", "offline");
      stCluster.set("status", "unhealthy");
    } else if (status.equals("offline")) {
      stNode.set("status", "online");
      if (stCluster.get("status").equals("unhealthy")) {
        List<JSONObject> nodeList = nodeDao.selectNodeByCluster(stCluster.getBigInteger("id"));
        boolean unhealthy = false;
        for (JSONObject node : nodeList) {
          if (node.get("status").equals("offline") && !node.get("id").equals(id)) {
            unhealthy = true;
            break;
          }
        }
        if (!unhealthy) {
          stCluster.set("status", "healthy");
        }
      }
    }
    if (nodeDao.updateNode(stNode)) {
      clusterDao.updateCluster(stCluster);
      return "";
    } else {
      return "Unable to commit changes to the database";
    }
  }

  /**
   * Remove a list of blocks from each node (container).
   */
  private void removeStorageBlocks() {
    StringBuilder builder = new StringBuilder();
    for (Block block : blockList) {
      builder.append(block.getCid());
      builder.append(" ");
    }
    String blockSpaceList = builder.toString();

    for (JSONObject node : nodeList) {
      String nodeName = node.getStr("name");

      try {
        DockerUtil.execDockerCmd(nodeName, Constants.IPFS_PREFIX,
            "pin", "rm", "-r=false", blockSpaceList);
        DockerUtil.execDockerCmd(nodeName, Constants.IPFS_PREFIX, "repo", "gc");
      } catch (InterruptedException e) {
        log.error(e.getMessage());
      }

      List<Block> nodeBlockList = node.getJSONArray("blockList").toList(Block.class);

      BigInteger listSize = BigInteger.ZERO;
      for (Block block : blockList) {
        if (nodeBlockList.contains(block)) {
          listSize = listSize.add(block.getSize());
        }
      }
      node.set("usedSpace", node.getBigInteger("usedSpace").subtract(listSize));

      node.set("blockList", nodeBlockList.removeAll(blockList));
    }
  }

  /**
   * Distribute blocks to each node (container).
   *
   * @see DockerUtil
   */
  private void distributeStorageBlocks() {
    List<List<Block>> partitions = partitionBlockList();
    int i = 0;
    for (List<Block> blockList : partitions) {
      JSONObject node = nodeList.get(i++);
      String nodeName = node.getStr("name");
      BigInteger listSize = BigInteger.ZERO;
      for (Block block : blockList) {
        try {
          DockerUtil.execDockerCmd(nodeName, Constants.IPFS_PREFIX,
              "pin", "add", "-r=false", block.getCid());
          // clear cache
          DockerUtil.execDockerCmd(nodeName, Constants.IPFS_PREFIX, "repo", "gc");
        } catch (InterruptedException e) {
          log.error(e.getMessage());
        }

        listSize = listSize.add(block.getSize());
      }
      node.set("usedSpace", node.getBigInteger("usedSpace").add(listSize));
      node.set("blockList", JSONUtil.parseArray(blockList));
    }
  }

  /**
   * Proportionally divide a list according to capacity of each node in {@link #nodeList}.
   *
   * @return {@link List} of {@link List} of {@link Block}, ordered by the original node list
   */
  private List<List<Block>> partitionBlockList() {
    List<List<Block>> partitionList = new ArrayList<>();
    int maxStorage = 0;

    for (JSONObject node : nodeList) {
      maxStorage += (node.getBigInteger("totalSpace").subtract(
          node.getBigInteger("usedSpace"))).intValue();
    }
    List<Block> copyBlockList = new ArrayList<>(blockList);
    DecimalFormat df = new DecimalFormat("0.00");
    for (JSONObject node : nodeList) {
      if (nodeList.indexOf(node) == node.size() - 1) {
        partitionList.add(copyBlockList);
        break;
      }
      double weight = Double.parseDouble(df.format((
          node.getBigInteger("totalSpace").subtract(
              node.getBigInteger("usedSpace"))).intValue() / maxStorage));
      int pivot = Math.round((float) (blockListSize.intValue() * weight));
      List<Block> singleList = copyBlockList.subList(0, pivot);
      copyBlockList = copyBlockList.subList(pivot, copyBlockList.size());
      partitionList.add(singleList);
    }

    return partitionList;
  }
}