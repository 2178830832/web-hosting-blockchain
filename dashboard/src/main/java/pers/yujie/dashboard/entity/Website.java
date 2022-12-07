package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Website {

  public Website(String cid, Date time, String clusterStatus) {
    this.cid = cid;
    this.clusterStatus = clusterStatus;
    this.createTime = time;
    this.updateTime = time;
  }

  private BigInteger website_id;

  private String cid;

  private String clusterStatus;

  private boolean isOnline;

  private Date createTime;

  private Date updateTime;

}
