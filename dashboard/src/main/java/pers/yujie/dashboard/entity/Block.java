package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents individual blocks of a Merkel tree.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see Data
 * @since 07/01/2023
 */
@Data
@AllArgsConstructor
public class Block {

  private String cid;

  private BigInteger size;
}
