package pers.yujie.dashboard.dao.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.SerializeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import pers.yujie.dashboard.dao.NodeDao;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.utils.Web3JUtil;

@Repository
@Slf4j
public class NodeDaoImpl implements NodeDao {

  private List<Node> nodes = new ArrayList<>();

  @Override
  public void initNodeDao() {
    try {
      String nodeEncodedStr = (String) Web3JUtil.sendQuery("getNodes",
          Collections.emptyList(),
          Collections.singletonList(new TypeReference<Utf8String>() {
          })).get(0).getValue();
      if (!StrUtil.isEmptyOrUndefined(nodeEncodedStr)) {
        nodes = JSONUtil.toList(JSONUtil.parseArray(nodeEncodedStr), Node.class);
      }
    } catch (ExecutionException | InterruptedException e) {
      log.error("Unable to retrieve node list from the database");
    }
  }

  @Override
  public List<Node> selectAllNode() {
    return nodes;
  }

  @Override
  public boolean insertNode(JSONObject node) {
    List<Node> updatedNodes = SerializeUtil.clone(nodes);
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

  private boolean commitChange(List<Node> updatedNodes) {
    String nodeDecodedStr = JSONUtil.parseArray(updatedNodes).toString();

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
      log.error("Unable to commit changes to the database");
      return false;
    }

  }

  private void setUpHelper(Node node, JSONObject nodeObj) {
    if (!StrUtil.isEmptyOrUndefined(nodeObj.getStr("name"))) {
      node.setName(nodeObj.getStr("name"));
    }
    if (!StrUtil.isEmptyOrUndefined(nodeObj.getStr("totalSpace"))) {
      node.setTotalSpace(nodeObj.getBigInteger("totalSpace"));
    }

    if (!StrUtil.isEmptyOrUndefined(nodeObj.getStr("usedSpace"))) {
      node.setUsedSpace(nodeObj.getBigInteger("usedSpace"));
    }
  }

  @Override
  public boolean updateNode(JSONObject node) {
    List<Node> updatedNodes = SerializeUtil.clone(nodes);
    for (Node upNode : updatedNodes) {
      if (upNode.getId().equals(node.getBigInteger("id"))) {
        setUpHelper(upNode, node);
        upNode.setUpdateTime(DateTime.now().toString());
        updatedNodes.set(updatedNodes.indexOf(upNode), upNode);
        return commitChange(updatedNodes);
      }
    }
    return false;
  }

  @Override
  public boolean deleteNode(BigInteger id) {
    List<Node> updatedNodes = SerializeUtil.clone(nodes);

    updatedNodes.removeIf(node -> node.getId().equals(id));

//    for (int i = 0; i < updatedNodes.size(); i++) {
//      if (updatedNodes.get(i).getId().equals(id)) {
//        updatedNodes.remove(i);
//        // do something
//        break;
//      }
//    }
    for (Node node : updatedNodes) {
      if (node.getId().compareTo(id) > 0) {
        node.setId(node.getId().subtract(BigInteger.ONE));
      }
    }

    return commitChange(updatedNodes);
  }

//
//
//  @Override
//  public List<Node> selectOnlineByCluster(String clusterName) {
//    List<Node> onlineNodes = new ArrayList<>();
//
//    for (Node node : nodes) {
//      if (node.isOnline() && node.getClusterName().equals(clusterName)) {
//        onlineNodes.add(node);
//      }
//    }
//    return onlineNodes;
//  }
//
//  @Override
//  @SuppressWarnings({"unchecked", "rawtypes"})
//  public void updateNodeBatch(List<Node> nodes) {
//    try {
//      EthSendTransaction response = Web3JUtil.sendTransaction("updateNodeBatch",
//          Collections.singletonList(new DynamicArray(DynamicStruct.class, nodes)));
//      if (response.getError() == null) {
//        log.info("Transaction succeeded: " + response.getResult());
//        this.nodes = nodes;
//      } else {
//        log.error("Transaction encountered error: " + response.getError().getMessage());
//      }
//    } catch (ExecutionException | InterruptedException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Override
//  @SuppressWarnings({"unchecked", "rawtypes"})
//  public void updateNodeBatchByCluster(List<Node> nodes) {
//    try {
//      EthSendTransaction response = Web3JUtil.sendTransaction("updateNodeBatchByCluster",
//          Collections.singletonList(new DynamicArray(DynamicStruct.class, nodes)));
//      if (response.getError() == null) {
//        log.info("Transaction succeeded: " + response.getResult());
//        String clusterName = nodes.get(0).getClusterName();
//        int i = 0;
//        for (Node node : this.nodes) {
//          if (node.getClusterName().equals(clusterName)) {
//            this.nodes.set(this.nodes.indexOf(node), nodes.get(i++));
//            if (i >= nodes.size()) {
//              return;
//            }
//          }
//        }
//      } else {
//        log.error("Transaction encountered error: " + response.getError().getMessage());
//      }
//    } catch (ExecutionException | InterruptedException e) {
//      e.printStackTrace();
//    }
//  }
}
