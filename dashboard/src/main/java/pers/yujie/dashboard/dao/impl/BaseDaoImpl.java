package pers.yujie.dashboard.dao.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.entity.Website;

/**
 * This is an abstract class to be inherited by the concrete Dao classes.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 29/12/2022
 */
public abstract class BaseDaoImpl {

  private Node node;
  private Cluster cluster;

  /**
   * A helper method to set cluster information.
   *
   * @param cluster    {@link Cluster} to be updated
   * @param clusterObj {@link JSONObject} containing essential information
   */
  protected void setUpHelper(Cluster cluster, JSONObject clusterObj) {
    cluster.setUpdateTime(DateTime.now().toString());
    this.cluster = cluster;
    innerHelper(clusterObj, false);
  }

  /**
   * A helper method to set node information.
   *
   * @param node    {@link Node} to be updated
   * @param nodeObj {@link JSONObject} containing essential information
   */
  protected void setUpHelper(Node node, JSONObject nodeObj) {
    node.setUpdateTime(DateTime.now().toString());
    this.node = node;
    if (!StrUtil.isEmptyOrUndefined(nodeObj.getStr("clusterId"))) {
      node.setClusterId(nodeObj.getBigInteger("clusterId"));
    }
    if (!StrUtil.isEmptyOrUndefined(nodeObj.getStr("name"))) {
      node.setName(nodeObj.getStr("name"));
    }
    if (!StrUtil.isEmptyOrUndefined(nodeObj.getStr("blockList"))) {
      node.setBlockList(nodeObj.getJSONArray("blockList"));
    }
    innerHelper(nodeObj, true);
  }

  /**
   * A helper method to set information for a {@link Cluster} or {@link Node}.
   *
   * @param object {@link JSONObject} containing essential information
   * @param isNode true if the entity is a node, false if it is a cluster
   */
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

  /**
   * A helper method to set website information.
   *
   * @param website    {@link Website} to be updated
   * @param websiteObj {@link JSONObject} containing essential information
   */
  protected void setUpHelper(Website website, JSONObject websiteObj) {
    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("name"))) {
      website.setName(websiteObj.getStr("name"));
    }
    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("location"))) {
      website.setLocation(websiteObj.getJSONArray("location"));
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
