package pers.yujie.dashboard.service;

import cn.hutool.json.JSONObject;
import java.io.File;
import java.math.BigInteger;
import java.util.List;
import pers.yujie.dashboard.entity.Website;

public interface WebsiteService {

  List<Website> selectAllWebsite();

  String updateWebsite(JSONObject website);

  String uploadWebsite(JSONObject website);

  String deleteWebsite(BigInteger id);

}
