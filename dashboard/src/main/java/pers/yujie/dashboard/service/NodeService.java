package pers.yujie.dashboard.service;

import cn.hutool.json.JSONObject;
import io.ipfs.multihash.Multihash;
import java.math.BigInteger;
import java.util.List;
import pers.yujie.dashboard.entity.Block;
import pers.yujie.dashboard.entity.Node;

public interface NodeService {

  List<JSONObject> selectAllNode();

  String updateNode(JSONObject node);

  String insertNode(JSONObject node);

  String deleteNode(BigInteger id);

  String changeNodeStatus(BigInteger id);

  void distribute(JSONObject cluster, JSONObject website, List<Block> blockList);

  void releaseWebsiteSpace(JSONObject cluster, JSONObject website);

//  void distributeBlockList(String clusterName, List<Multihash> blockList, String cid);

//  void distributeWebsite(BigInteger cluster_id, String cid);
//
//  void removeWebsite(BigInteger cluster_id, String cid);
//
//  void offlineNode(Node offNode);
}
