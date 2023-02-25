package pers.yujie.dashboard.service;

import cn.hutool.json.JSONObject;
import java.util.List;
import pers.yujie.dashboard.entity.Block;

/**
 * This is the interface providing cluster services for outer usages.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see pers.yujie.dashboard.service.impl.ClusterServiceImpl
 * @since 20/01/2023
 */
public interface ClusterService {

  String distributeWebsite(JSONObject website, List<Block> blockList);

  void removeWebsite(JSONObject website);

  List<JSONObject> selectAllCluster();
}
