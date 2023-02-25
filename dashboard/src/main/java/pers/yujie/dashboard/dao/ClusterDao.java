package pers.yujie.dashboard.dao;

import cn.hutool.json.JSONObject;
import java.math.BigInteger;
import java.util.List;

/**
 * This is the interface providing cluster data access for outer usages.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see pers.yujie.dashboard.dao.impl.ClusterDaoImpl
 * @since 29/12/2022
 */
public interface ClusterDao {

  List<JSONObject> selectAllCluster();

  void initClusterDao();

  boolean updateCluster(JSONObject cluster);

  boolean updateClusterBatch(List<JSONObject> clusterList);

  JSONObject selectClusterById(BigInteger id);

  JSONObject selectMinCluster();

  JSONObject selectMinHealthyCluster();
}
