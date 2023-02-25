package pers.yujie.dashboard.dao;

import cn.hutool.json.JSONObject;
import java.math.BigInteger;
import java.util.List;

/**
 * This is the interface providing node data access for outer usages.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see pers.yujie.dashboard.dao.impl.NodeDaoImpl
 * @since 29/12/2022
 */
public interface NodeDao {

  void initNodeDao();

  List<JSONObject> selectAllNode();

  List<JSONObject> selectNodeByCluster(BigInteger clusterId);

  JSONObject selectNodeById(BigInteger id);

  JSONObject selectNodeByName(String name);

  boolean insertNode(JSONObject node);

  boolean updateNode(JSONObject node);

  boolean updateNodeBatch(List<JSONObject> nodeList);

  boolean deleteNode(BigInteger id);
}
