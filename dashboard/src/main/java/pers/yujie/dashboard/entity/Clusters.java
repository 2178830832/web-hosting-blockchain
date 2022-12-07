package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.utils.Convert.Unit;

@EqualsAndHashCode(callSuper = true)
@Data
public class Clusters extends StaticStruct {

  public Clusters(Uint id, Bool isHealthy, Uint usedSpace, Uint totalSpace) {
    super(id, isHealthy, usedSpace, totalSpace);
    this.id = id.getValue();
    this.isHealthy = isHealthy.getValue();
    this.usedSpace = usedSpace.getValue();
    this.totalSpace = totalSpace.getValue();
  }

  private BigInteger id;

  private boolean isHealthy;

  private BigInteger usedSpace;

  private BigInteger totalSpace;

  private boolean isUsed;
}
