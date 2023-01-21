package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

@EqualsAndHashCode(callSuper = true)
@Data
public class Cluster1 extends DynamicStruct {

  public Cluster1(Utf8String name, Bool isHealthy, Uint256 usedSpace, Uint256 totalSpace) {
    super(name, isHealthy, usedSpace, totalSpace);
    this.name = name.getValue();
    this.isHealthy = isHealthy.getValue();
    this.usedSpace = usedSpace.getValue();
    this.totalSpace = totalSpace.getValue();
  }

  public Cluster1(String name, boolean isHealthy, BigInteger usedSpace, BigInteger totalSpace) {
    super(new Utf8String(name), new Bool(isHealthy), new Uint256(usedSpace),
        new Uint256(totalSpace));
    this.name = name;
    this.isHealthy = isHealthy;
    this.usedSpace = usedSpace;
    this.totalSpace = totalSpace;
  }

  private String name;

  private boolean isHealthy;

  private BigInteger usedSpace;

  private BigInteger totalSpace;
}
