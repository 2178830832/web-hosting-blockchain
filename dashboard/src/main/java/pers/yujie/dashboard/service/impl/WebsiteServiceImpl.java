package pers.yujie.dashboard.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
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
import pers.yujie.dashboard.entity.Block;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.service.ClusterService;
import pers.yujie.dashboard.service.WebsiteService;
import pers.yujie.dashboard.utils.IPFSUtil;

@Service
@Slf4j
public class WebsiteServiceImpl implements WebsiteService {

  @Resource
  private ClusterService clusterService;

  @Resource
  private WebsiteDao websiteDao;

  @Override
  public List<JSONObject> selectAllWebsite() {
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
    JSONObject upWebsite = websiteDao.selectWebsiteById(id);

    List<JSONObject> clusters = clusterService.selectAllCluster();
    for (JSONObject cluster : clusters) {
      if (!cluster.get("status").equals("healthy")) {
        return "Cluster " + cluster.get("id") + " is not available";
      }
    }
    clusterService.removeWebsite(upWebsite);
    String message = uploadHelper(website);
    if (message.equals("")) {
      if (websiteDao.updateWebsite(website)) {
        return "";
      } else {
        return "Unable to commit changes to the database";
      }
    } else {
      return message;
    }
  }

  @Override
  public String uploadWebsite(JSONObject website) {
    String name = website.getStr("name");
    String path = website.getStr("path");

    if (name == null || path == null) {
      return "Unexpected Json format";
    }
    List<JSONObject> clusters = clusterService.selectAllCluster();
    for (JSONObject cluster : clusters) {
      if (!cluster.get("status").equals("healthy")) {
        return "Cluster " + cluster.get("id") + " is not available";
      }
    }

    String message = uploadHelper(website);

    if (message.equals("")) {
      if (websiteDao.insertWebsite(website)) {
        return "";
      } else {
        return "Unable to commit changes to the database";
      }
    } else {
      return message;
    }
  }

  private String uploadHelper(JSONObject website) {
    File file = new File(website.getStr("path"));
    if (IPFSUtil.getAddress() == null) {
      return "IPFS not connected";
    }
    if (file.exists()) {
      try {
        List<MerkleNode> resultList = IPFSUtil.uploadIPFS(file);
        website.set("cid", resultList.get(resultList.size() - 1).hash.toString());

        List<Block> blockList = IPFSUtil.getBlockList(website.getStr("cid"));
        BigInteger listSize = BigInteger.ZERO;
        for (Block block : blockList) {
          listSize = listSize.add(block.getSize());
        }

        website.set("size", listSize);
        log.info("Uploaded website CID: " + website.get("cid"));
        log.info("Website size: " + website.get("size"));
        return clusterService.distributeWebsite(website, blockList);
      } catch (IOException e) {
        return "Unable to upload website to IPFS";
      }
    } else {
      return "The file or directory does not exist";
    }
  }

  @Override
  public String deleteWebsite(BigInteger id) {
    JSONObject rmWebsite = websiteDao.selectWebsiteById(id);
    clusterService.removeWebsite(rmWebsite);
    if (websiteDao.deleteWebsite(id)) {
      return "";
    } else {
      return "Unable to commit changes to the database";
    }
  }

}
