package pers.yujie.dashboard.service;

import cn.hutool.json.JSONObject;
import java.math.BigInteger;
import java.util.List;
import pers.yujie.dashboard.entity.Block;

/**
 * This is the interface providing node services for outer usages.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see pers.yujie.dashboard.service.impl.NodeServiceImpl
 * @since 20/01/2023
 */
public interface NodeService {

  List<JSONObject> selectAllNode();

  String updateNode(JSONObject node);

  String insertNode(JSONObject node);

  String deleteNode(BigInteger id);

  String changeNodeStatus(BigInteger id);

  void distribute(JSONObject cluster, JSONObject website, List<Block> blockList);

  void releaseWebsiteSpace(JSONObject cluster, JSONObject website);
}
