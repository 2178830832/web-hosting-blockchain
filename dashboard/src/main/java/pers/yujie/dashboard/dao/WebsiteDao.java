package pers.yujie.dashboard.dao;

import cn.hutool.json.JSONObject;
import java.math.BigInteger;
import java.util.List;

/**
 * This is the interface providing website data access for outer usages.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see pers.yujie.dashboard.dao.impl.WebsiteDaoImpl
 * @since 29/12/2022
 */
public interface WebsiteDao {

  void initWebsiteDao();

  boolean insertWebsite(JSONObject website);

  boolean updateWebsite(JSONObject website);

  boolean deleteWebsite(BigInteger id);

  JSONObject selectWebsiteById(BigInteger id);

  List<JSONObject> selectAllWebsite();
}
