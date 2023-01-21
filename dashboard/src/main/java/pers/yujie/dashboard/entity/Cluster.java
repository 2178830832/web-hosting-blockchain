package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Cluster extends BaseEntity{

  public Cluster(BigInteger id, String name, String status,BigInteger usedSpace, BigInteger totalSpace) {
    super(id);
    this.name = name;
    this.status = status;
    this.usedSpace = usedSpace;
    this.totalSpace = totalSpace;

  }

  private String name;

  private String status;

  private BigInteger usedSpace;

  private BigInteger totalSpace;
}
