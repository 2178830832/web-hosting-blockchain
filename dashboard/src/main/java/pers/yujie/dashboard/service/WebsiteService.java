package pers.yujie.dashboard.service;

import cn.hutool.json.JSONObject;
import java.math.BigInteger;
import java.util.List;

/**
 * This is the interface providing website services for outer usages.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see pers.yujie.dashboard.service.impl.WebsiteServiceImpl
 * @since 18/11/2022
 */
public interface WebsiteService {

  List<JSONObject> selectAllWebsite();

  String updateWebsite(JSONObject website);

  String uploadWebsite(JSONObject website);

  String deleteWebsite(BigInteger id);

}
