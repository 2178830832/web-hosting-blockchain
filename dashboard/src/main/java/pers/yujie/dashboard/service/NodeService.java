package pers.yujie.dashboard.service;

import java.math.BigInteger;
import pers.yujie.dashboard.entity.Node;

public interface NodeService {

  void distributeWebsite(BigInteger cluster_id, String cid);

  void removeWebsite(BigInteger cluster_id, String cid);

  void offlineNode(Node offNode);

  void createInitNode(BigInteger cluster_id);
}
