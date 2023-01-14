package pers.yujie.dashboard.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import pers.yujie.dashboard.dao.NodeDao;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.utils.Web3JUtil;

@Repository
@Slf4j
public class NodeDaoImpl implements NodeDao {

  private List<Node> nodes;

  @Resource
  private ApplicationContext ctx;

  @PostConstruct
  @SuppressWarnings({"unchecked", "rawtypes"})
  private void initWebsiteDao() {
//    try {
//      nodes = (List) Web3JUtil.sendQuery("selectAllNodes",
//          Collections.emptyList(),
//          Collections.singletonList(new TypeReference<DynamicArray<Node>>() {
//          })).get(0).getValue();
//    } catch (InterruptedException | ExecutionException e) {
//      log.info("Unable to request local ganache server");
//      AppUtil.exitApplication(ctx, 1);
//    }
  }

  @Override
  public List<Node> selectOnlineByCluster(String clusterName) {
    List<Node> onlineNodes = new ArrayList<>();

    for (Node node : nodes) {
      if (node.isOnline() && node.getClusterName().equals(clusterName)) {
        onlineNodes.add(node);
      }
    }
    return onlineNodes;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void updateNodeBatch(List<Node> nodes) {
    try {
      EthSendTransaction response = Web3JUtil.sendTransaction("updateNodeBatch",
          Collections.singletonList(new DynamicArray(DynamicStruct.class, nodes)));
      if (response.getError() == null) {
        log.info("Transaction succeeded: " + response.getResult());
        this.nodes = nodes;
      } else {
        log.error("Transaction encountered error: " + response.getError().getMessage());
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void updateNodeBatchByCluster(List<Node> nodes) {
    try {
      EthSendTransaction response = Web3JUtil.sendTransaction("updateNodeBatchByCluster",
          Collections.singletonList(new DynamicArray(DynamicStruct.class, nodes)));
      if (response.getError() == null) {
        log.info("Transaction succeeded: " + response.getResult());
        String clusterName = nodes.get(0).getClusterName();
        int i = 0;
        for (Node node : this.nodes) {
          if (node.getClusterName().equals(clusterName)) {
            this.nodes.set(this.nodes.indexOf(node), nodes.get(i++));
            if (i >= nodes.size()) {
              return;
            }
          }
        }
      } else {
        log.error("Transaction encountered error: " + response.getError().getMessage());
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
