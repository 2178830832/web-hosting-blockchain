package pers.yujie.dashboard.service;

import cn.hutool.json.JSONObject;
import io.ipfs.multihash.Multihash;
import java.math.BigInteger;
import java.util.List;
import pers.yujie.dashboard.entity.Node;

public interface NodeService {

  List<Node> selectAllNode();

  String updateNode(JSONObject node);

  String insertNode(JSONObject node);

  String deleteNode(BigInteger id);

//  void distributeBlockList(String clusterName, List<Multihash> blockList, String cid);

//  void distributeWebsite(BigInteger cluster_id, String cid);
//
//  void removeWebsite(BigInteger cluster_id, String cid);
//
//  void offlineNode(Node offNode);
}
