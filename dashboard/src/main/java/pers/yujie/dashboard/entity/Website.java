package pers.yujie.dashboard.entity;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Website extends BaseEntity {

  public Website(BigInteger id, String cid, String name, BigInteger size, String status,
      JSONArray location) {
    super(id);
    this.cid = cid;
    this.status = status;
    this.name = name;
    this.size = size;
    this.location = location;
  }

  public Website(BigInteger id) {
    super(id);
    this.status = "online";
    this.location = JSONUtil.createArray();
  }

  private String cid;

  private String status;

  private String name;

  private BigInteger size;

  private JSONArray location;

}
