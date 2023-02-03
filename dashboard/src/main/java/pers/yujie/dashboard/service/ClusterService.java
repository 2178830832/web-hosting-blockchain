package pers.yujie.dashboard.service;

import cn.hutool.json.JSONObject;
import java.math.BigInteger;
import java.util.List;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.entity.Website;

public interface ClusterService {

  BigInteger assignToMinCluster(BigInteger nodeSpace);

  void deleteNodeSpace(Node node);

  void distributeWebsite(JSONObject website);

  void removeWebsite(Website website);

//  String distributeWebsite(String cid);
//
//  List<BigInteger> removeWebsite(String cid);
//
//  void updateClusterContent(Cluster cluster);

}
