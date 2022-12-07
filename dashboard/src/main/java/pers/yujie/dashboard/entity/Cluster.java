package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Cluster {

  public Cluster(BigInteger clusterId) {
    this.clusterId = clusterId;
    createTime = new Date();
    updateTime = new Date();
  }

  private BigInteger clusterId;

  private boolean isHealthy;

  private int usedSpace;

  private int totalSpace;

  private Date createTime;

  private Date updateTime;
}
