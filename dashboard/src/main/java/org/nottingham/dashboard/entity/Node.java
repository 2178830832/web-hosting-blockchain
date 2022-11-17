package org.nottingham.dashboard.entity;

import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

@Data
public class Node {

  public BigInteger nodeId;

  public BigInteger clusterId;

  public boolean isHealthy;

  public Date createTime;

  public Date updateTime;
}
