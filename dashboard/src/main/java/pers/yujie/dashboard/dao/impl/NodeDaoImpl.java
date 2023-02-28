package pers.yujie.dashboard.dao.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
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
import pers.yujie.dashboard.dao.NodeDao;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.utils.EncryptUtil;
import pers.yujie.dashboard.utils.Web3JUtil;

/**
 * This is the Data Access Object (DAO) class for {@link Cluster}.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see BaseDaoImpl
 * @since 29/12/2022
 */
@Repository
@Slf4j
public class NodeDaoImpl extends BaseDaoImpl implements NodeDao {

  private List<Node> nodes = new ArrayList<>();

  /**
   * Initialise this class by reading from the Ganache and decrypting with {@link EncryptUtil}.
   */
  @Override
  public void initNodeDao() {
    try {
      String nodeEncodedStr = (String) Web3JUtil.sendQuery("getNodes",
          Collections.emptyList(),
          Collections.singletonList(new TypeReference<Utf8String>() {
          })).get(0).getValue();
      if (!StrUtil.isEmptyOrUndefined(nodeEncodedStr)) {
        nodeEncodedStr = EncryptUtil.decryptAES(nodeEncodedStr);
        nodes = JSONUtil.toList(JSONUtil.parseArray(nodeEncodedStr), Node.class);
      }
    } catch (IndexOutOfBoundsException | ExecutionException | InterruptedException e) {
      log.error("Unable to retrieve node list from the data source");
    }
  }

  /**
   * Select all nodes as a list.
   *
   * @return a {@link List} of {@link JSONObject} representing the nodes
   */
  @Override
  public List<JSONObject> selectAllNode() {
    List<JSONObject> nodeList = new ArrayList<>();
    for (Node node : nodes) {
      nodeList.add(JSONUtil.parseObj(node));
    }
    return nodeList;
  }

  /**
   * Select a list of nodes given a specific cluster.
   *
   * @param clusterId {@link BigInteger} of the cluster ID
   * @return a {@link List} of {@link JSONObject} representing the selected nodes
   */
  @Override
  public List<JSONObject> selectNodeByCluster(BigInteger clusterId) {
    List<JSONObject> nodeList = new ArrayList<>();
    for (Node node : nodes) {
      if (node.getClusterId().equals(clusterId)) {
        nodeList.add(JSONUtil.parseObj(node));
      }
    }
    return nodeList;
  }

  /**
   * Select a specific node given its ID.
   *
   * @param id {@link BigInteger} of the specified node ID
   * @return a {@link JSONObject} of the selected node
   */
  @Override
  public JSONObject selectNodeById(BigInteger id) {
    return JSONUtil.parseObj(nodes.get(id.intValue()));
  }

  /**
   * Select a specific node given its name.
   *
   * @param name {@link String} of the specified node name
   * @return a {@link JSONObject} of the selected node
   */
  @Override
  public JSONObject selectNodeByName(String name) {
    for (Node node : nodes) {
      if (node.getName().equals(name)) {
        return JSONUtil.parseObj(node);
      }
    }
    return null;
  }

  /**
   * Insert a new node.
   *
   * @param node {@link JSONObject} of the node to be inserted
   * @return true if succeeded, false otherwise
   */
  @Override
  public boolean insertNode(JSONObject node) {
    List<Node> updatedNodes = new ArrayList<>(nodes);
    updatedNodes = ListUtil.sortByProperty(updatedNodes, "id");
    BigInteger id;
    if (updatedNodes.size() < 1) {
      id = BigInteger.ZERO;
    } else {
      id = updatedNodes.get(updatedNodes.size() - 1).getId().add(BigInteger.ONE);
    }
    Node addNode = new Node(id);
    setUpHelper(addNode, node);
    addNode.setUsedSpace(BigInteger.ZERO);
    updatedNodes.add(addNode);
    return commitChange(updatedNodes);
  }

  /**
   * Encrypt the nodes with symmetric algorithm and commit it to the data source.
   *
   * @param updatedNodes a {@link List} of {@link Node}
   * @return true if succeeded, false otherwise
   */
  private boolean commitChange(List<Node> updatedNodes) {
    String nodeDecodedStr = JSONUtil.parseArray(updatedNodes).toString();
    nodeDecodedStr = EncryptUtil.encryptAES(nodeDecodedStr);

    try {
      EthSendTransaction response = Web3JUtil.sendTransaction("setNodes",
          Collections.singletonList(new Utf8String(nodeDecodedStr)));
      if (response.getError() == null) {
        log.info("Transaction succeeded: " + response.getResult());
        nodes = updatedNodes;
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

  /**
   * Update a specific node.
   *
   * @param node {@link JSONObject} representing the node to be updated
   * @return true if succeeded, false otherwise
   */
  @Override
  public boolean updateNode(JSONObject node) {
    List<Node> updatedNodes = new ArrayList<>(nodes);
    for (Node upNode : updatedNodes) {
      if (upNode.getId().equals(node.getBigInteger("id"))) {
        int index = updatedNodes.indexOf(upNode);
        setUpHelper(upNode, node);
        updatedNodes.set(index, upNode);
        return commitChange(updatedNodes);
      }
    }
    return false;
  }

  /**
   * Batch-update the nodes.
   *
   * @param nodeList a {@link List} of {@link JSONObject} representing the nodes
   * @return true if succeeded, false otherwise
   */
  @Override
  public boolean updateNodeBatch(List<JSONObject> nodeList) {
    List<Node> updatedNodes = new ArrayList<>(nodes);
    for (JSONObject node : nodeList) {
      if (updatedNodes.contains(node.toBean(Node.class))) {
        node.set("updateTime", DateTime.now().toString());
        updatedNodes.set(node.getInt("id"), node.toBean(Node.class));
      }
    }
    return commitChange(updatedNodes);
  }

  /**
   * Delete an existing node.
   *
   * @param id {@link BigInteger} of the specified node ID.
   * @return true if succeeded, false otherwise
   */
  @Override
  public boolean deleteNode(BigInteger id) {
    List<Node> updatedNodes = new ArrayList<>(nodes);

    for (Node node : updatedNodes) {
      if (node.getId().equals(id)) {
        node.setStatus("deleted");
        updatedNodes.remove(node);
        for (Node nodeIter : updatedNodes) {
          if (nodeIter.getId().compareTo(id) > 0) {
            nodeIter.setId(nodeIter.getId().subtract(BigInteger.ONE));
          }
        }
        return commitChange(updatedNodes);
      }
    }

    return false;
  }
}
