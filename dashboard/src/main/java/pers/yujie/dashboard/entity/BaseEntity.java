package pers.yujie.dashboard.entity;

import cn.hutool.core.date.DateTime;
import java.io.Serializable;
import java.math.BigInteger;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is an abstract entity class to be inherited by the concrete classes. It implements {@link
 * Serializable} to allow ordering.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see Data
 * @since 13/11/2022
 */
@Data
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {

  /**
   * When creating a new instance, initialise the create and update time with the current
   * timestamp.
   *
   * @param id {@link BigInteger} of the entity to be created
   */
  public BaseEntity(BigInteger id) {
    this.id = id;
    this.createTime = DateTime.now().toString();
    this.updateTime = DateTime.now().toString();
  }

  private BigInteger id;

  private String createTime;

  private String updateTime;
}
