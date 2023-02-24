package pers.yujie.dashboard.entity;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This class represents websites.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see Data
 * @since 01/12/2022
 */
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

  /**
   * When creating a new instance, initialise the {@link #status} to online.
   *
   * @param id {@link BigInteger} representing the new website ID.
   */
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
