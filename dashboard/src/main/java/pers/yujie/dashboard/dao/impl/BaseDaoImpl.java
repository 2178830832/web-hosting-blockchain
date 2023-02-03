package pers.yujie.dashboard.dao.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.entity.Website;

public class BaseDaoImpl {

  private Node node;
  private Cluster cluster;

  protected void setUpHelper(Cluster cluster, JSONObject clusterObj) {
    cluster.setUpdateTime(DateTime.now().toString());
    this.cluster = cluster;
    innerHelper(clusterObj, false);
  }

  protected void setUpHelper(Node node, JSONObject nodeObj) {
    node.setUpdateTime(DateTime.now().toString());
    this.node = node;
    if (!StrUtil.isEmptyOrUndefined(nodeObj.getStr("name"))) {
      node.setName(nodeObj.getStr("name"));
    }
    innerHelper(nodeObj, true);
  }

  private void innerHelper(JSONObject object, boolean isNode) {
    if (!StrUtil.isEmptyOrUndefined(object.getStr("totalSpace"))) {
      if (isNode) {
        node.setTotalSpace(object.getBigInteger("totalSpace"));
      } else {
        cluster.setTotalSpace(object.getBigInteger("totalSpace"));
      }
    }
    if (!StrUtil.isEmptyOrUndefined(object.getStr("usedSpace"))) {
      if (isNode) {
        node.setUsedSpace(object.getBigInteger("usedSpace"));
      } else {
        cluster.setUsedSpace(object.getBigInteger("usedSpace"));
      }
    }

    if (!StrUtil.isEmptyOrUndefined(object.getStr("status"))) {
      if (isNode) {
        node.setStatus(object.getStr("status"));
      } else {
        cluster.setStatus(object.getStr("status"));
      }
    }
  }

  protected void setUpHelper(Website website, JSONObject websiteObj) {
    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("name"))) {
      website.setName(websiteObj.getStr("name"));
    }
    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("location"))) {
      website.setLocation(websiteObj.getStr("location"));
    }

    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("size"))) {
      website.setSize(websiteObj.getBigInteger("size"));
    }

    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("cid"))) {
      website.setCid(websiteObj.getStr("cid"));
    }

    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("status"))) {
      website.setStatus(websiteObj.getStr("status"));
    }
  }
}
