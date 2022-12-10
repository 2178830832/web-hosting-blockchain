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
public class Node extends DynamicStruct {

  public Node(Utf8String clusterName, Utf8String name, Bool isOnline, Uint256 usedSpace,
      Uint256 totalSpace) {
    super(clusterName, name, isOnline, usedSpace, totalSpace);
    this.clusterName = clusterName.getValue();
    this.name = name.getValue();
    this.isOnline = isOnline.getValue();
    this.usedSpace = usedSpace.getValue();
    this.totalSpace = totalSpace.getValue();
  }

  public Node(String clusterName, String name, boolean isOnline, BigInteger usedSpace,
      BigInteger totalSpace) {
    super(new Utf8String(clusterName), new Utf8String(name), new Bool(isOnline),
        new Uint256(usedSpace), new Uint256(totalSpace));
    this.clusterName = clusterName;
    this.name = name;
    this.isOnline = isOnline;
    this.usedSpace = usedSpace;
    this.totalSpace = totalSpace;
  }

  private String clusterName;

  private String name;

  private boolean isOnline;

  private BigInteger usedSpace;

  private BigInteger totalSpace;
}
