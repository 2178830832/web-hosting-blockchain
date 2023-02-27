package pers.yujie.dashboard.entity;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This class represents nodes contained by {@link Cluster}.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see Data
 * @since 07/01/2022
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Node extends BaseEntity {

  public Node(BigInteger id, BigInteger clusterId, String name, String status, BigInteger usedSpace,
      BigInteger totalSpace, JSONArray blockList) {
    super(id);
    this.clusterId = clusterId;
    this.name = name;
    this.status = status;
    this.usedSpace = usedSpace;
    this.totalSpace = totalSpace;
    this.blockList = blockList;
  }

  /**
   * When creating a new instance, initialise the {@link #status} to online.
   *
   * @param id {@link BigInteger} representing the new node ID.
   */
  public Node(BigInteger id) {
    super(id);
    this.status = "online";
    this.blockList = JSONUtil.createArray();
  }

  private BigInteger clusterId;

  private String name;

  private String status;

  private BigInteger usedSpace;

  private BigInteger totalSpace;

  private JSONArray blockList;
}
