package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Node extends BaseEntity {

  public Node(BigInteger id, String name, String status, String usedSpace, String totalSpace) {
    super(id);
    this.name = name;
    this.status = status;
    this.usedSpace = usedSpace;
    this.totalSpace = totalSpace;
  }

  private BigInteger id;

  private String name;

  private String status;

  private String usedSpace;

  private String totalSpace;
}
