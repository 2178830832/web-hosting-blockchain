package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Node {

  public Node(BigInteger nodeId, BigInteger clusterId) {
    this.nodeId = nodeId;
    this.clusterId = clusterId;
    this.setMaster(true);
    nodeName = "ipfs" + nodeId;
    createTime = new Date();
    updateTime = new Date();
  }

  private BigInteger nodeId;

  private BigInteger clusterId;

  private String nodeName;

  private boolean isHealthy;

  private boolean isMaster;

  private int usedSpace;

  private int totalSpace;

  private Date createTime;

  private Date updateTime;
}
