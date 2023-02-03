package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Node extends BaseEntity {

  public Node(BigInteger id, BigInteger clusterId, String name, String status, BigInteger usedSpace,
      BigInteger totalSpace) {
    super(id);
    this.clusterId = clusterId;
    this.name = name;
    this.status = status;
    this.usedSpace = usedSpace;
    this.totalSpace = totalSpace;
  }

  public Node(BigInteger id) {
    super(id);
    this.status = "online";
  }

  private BigInteger clusterId;

  private String name;

  private String status;

  private BigInteger usedSpace;

  private BigInteger totalSpace;
}
