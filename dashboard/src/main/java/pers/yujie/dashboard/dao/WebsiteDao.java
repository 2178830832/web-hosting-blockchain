package pers.yujie.dashboard.dao;


import cn.hutool.json.JSONObject;
import java.math.BigInteger;
import java.util.List;
import pers.yujie.dashboard.entity.Website;

public interface WebsiteDao {

  void initWebsiteDao();
  boolean insertWebsite(JSONObject website);

  boolean updateWebsite(JSONObject website);

  boolean deleteWebsite(BigInteger id);
//  int deleteByWebsiteId(BigInteger website_id);
//
//  int updateWebsite(Website website);
//
//  Website selectByWebsiteId(BigInteger website_id);
//
  List<Website> selectAllWebsite();
}
