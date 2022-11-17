package org.nottingham.dashboard.entity;

import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

@Data
public class Cluster {

  public BigInteger clusterId;

  public boolean isHealthy;

  public Date createTime;

  public Date updateTime;
}
