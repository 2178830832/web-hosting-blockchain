package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This class represents clusters which contain multiple {@link Node}. <br> When using the {@link
 * Data}, it auto generates {@link #equals(Object)} and {@link #hashCode()}, which collapses with
 * the inherited methods. Therefore, the {@link EqualsAndHashCode} is set to true to avoid this
 * problem.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see Data
 * @since 01/12/2022
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Cluster extends BaseEntity {

  public Cluster(BigInteger id, String name, String status, BigInteger usedSpace,
      BigInteger totalSpace) {
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
