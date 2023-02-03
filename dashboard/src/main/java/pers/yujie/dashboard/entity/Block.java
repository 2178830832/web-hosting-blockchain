package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import lombok.Data;

@Data
public class Block {
    private String cid;

    private BigInteger nodeId;

    private BigInteger size;
}
