package pers.yujie.dashboard.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import io.ipfs.api.MerkleNode;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.yujie.dashboard.dao.WebsiteDao;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.service.WebsiteService;
import pers.yujie.dashboard.utils.IPFSUtil;

@Service
@Slf4j
public class WebsiteServiceImpl implements WebsiteService {

//  @Resource
//  private ClusterService clusterService;

  @Resource
  private WebsiteDao websiteDao;

  @Override
  public List<Website> selectAllWebsite() {
    return websiteDao.selectAllWebsite();
  }

  @Override
  public String updateWebsite(JSONObject website) {
    BigInteger id = website.getBigInteger("id");
    String name = website.getStr("name");
    String path = website.getStr("path");

    if (id == null || name == null || path == null) {
      return "Unexpected Json format";
    }
    File file = new File(path);
    if (file.isDirectory()) {
      //cluster update, also update the size
    } else {
      return "The path is not a directory";
    }

    if (websiteDao.updateWebsite(website)) {
      return "";
    } else {
      return "Unable to commit changes to the database";
    }
  }

  @Override
  public String uploadWebsite(JSONObject website) {
    String name = website.getStr("name");
    String path = website.getStr("path");

    if (name == null || path == null) {
      return "Unexpected Json format";
    }
    // set size, cid and other clusters
    File file = new File(path);
    if (file.isDirectory()) {
      //cluster update, also update the size
    } else {
      return "The path is not a directory";
    }

    if (websiteDao.insertWebsite(website)) {
      return "";
    } else {
      return "Unable to commit changes to the database";
    }

//    Website addWebsite;
//    try {
//      List<MerkleNode> addResponse = ipfsUtil.uploadIPFS(websiteFile);
//      String cid = addResponse.get(addResponse.size() - 1).hash.toString();
//      log.info("Uploaded website CID: " + cid);
//
//      String location = clusterService.distributeWebsite(cid);
//      addWebsite = new Website(cid, location, true);
//    } catch (IOException e) {
//      e.printStackTrace();
//      return false;
//    }
//    return websiteDao.insertWebsite(addWebsite);
  }

  @Override
  public String deleteWebsite(BigInteger id) {
    // change size and cluster info
    if (websiteDao.deleteWebsite(id)) {
      return "";
    } else {
      return "Unable to commit changes to the database";
    }
  }

//  @Override
//  public boolean deleteWebsite(BigInteger website_id) {
////    Website rmWebsite = websiteMapper.selectByWebsiteId(website_id);
////    String cid = rmWebsite.getCid();
////
////    List<BigInteger> rmClusters = clusterService.removeWebsite(cid);
////    List<BigInteger> preClusters = JSON.parseArray(rmWebsite.getClusterStatus(), BigInteger.class);
////    String clusterStatus = JSON.toJSONString(preClusters.removeAll(rmClusters));
////
////    rmWebsite.setOnline(false);
////    rmWebsite.setUpdateTime(new Date());
////    rmWebsite.setClusterStatus(clusterStatus);
////
////    return websiteMapper.updateWebsite(rmWebsite) > 0;
//    return true;
//  }


}
