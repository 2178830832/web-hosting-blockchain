package pers.yujie.dashboard.service;

import io.ipfs.multihash.Multihash;
import java.util.List;

public interface NodeService {

  void distributeBlockList(String clusterName, List<Multihash> blockList, String cid);

//  void distributeWebsite(BigInteger cluster_id, String cid);
//
//  void removeWebsite(BigInteger cluster_id, String cid);
//
//  void offlineNode(Node offNode);
}
