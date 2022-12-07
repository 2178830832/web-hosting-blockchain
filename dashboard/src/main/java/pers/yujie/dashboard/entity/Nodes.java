package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.utils.Convert.Unit;

@EqualsAndHashCode(callSuper = true)
@Data
public class Nodes extends StaticStruct {
  public Nodes(Uint clusterId, Utf8String name, Bool isMaster, Bool isOnline, Uint usedSpace, Uint totalSpace, Bool isUsed) {
    super(clusterId, name, isMaster, isOnline, usedSpace, totalSpace, isUsed);
    this.clusterId = clusterId.getValue();
    this.name = name.getValue();
    this.isMaster = isMaster.getValue();
    this.isOnline = isOnline.getValue();
    this.usedSpace = usedSpace.getValue();
    this.totalSpace = totalSpace.getValue();
    this.isUsed = isUsed.getValue();
  }

  public Nodes(BigInteger clusterId,String name, boolean isMaster, boolean isOnline, BigInteger usedSpace, BigInteger totalSpace, boolean isUsed) {
    super(new Uint(clusterId), new Utf8String(name), new Bool(isMaster),new Bool(isOnline),new Uint(usedSpace),new Uint(totalSpace), new Bool(isUsed));
    this.clusterId = clusterId;
    this.name = name;
    this.isMaster = isMaster;
    this.isOnline = isOnline;
    this.usedSpace = usedSpace;
    this.totalSpace = totalSpace;
    this.isUsed = isUsed;
  }

  private BigInteger clusterId;

  private String name;

  private boolean isMaster;

  private boolean isOnline;

  private BigInteger usedSpace;

  private BigInteger totalSpace;

  private boolean isUsed;
}
