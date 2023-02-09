package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Block {

  private String cid;

  private BigInteger size;
}
