package pers.yujie.dashboard.entity;

import cn.hutool.core.date.DateTime;
import java.io.Serializable;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {

  public BaseEntity(BigInteger id) {
    this.id = id;
    this.createTime = DateTime.now().toString();
    this.updateTime = DateTime.now().toString();
  }

  private BigInteger id;

  private String createTime;

  private String updateTime;
}
