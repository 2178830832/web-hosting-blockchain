package pers.yujie.dashboard.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.utils.Convert.Unit;

//@EqualsAndHashCode(callSuper = true)
@Data
public class Cluster extends DynamicStruct {

  public Cluster(Utf8String name, Bool isHealthy, Uint usedSpace, Uint totalSpace) {
    super(name, isHealthy, usedSpace, totalSpace);
    this.name = name.getValue();
    this.isHealthy = isHealthy.getValue();
    this.usedSpace = usedSpace.getValue();
    this.totalSpace = totalSpace.getValue();
  }

  public Cluster(String name, boolean isHealthy, BigInteger usedSpace, BigInteger totalSpace) {
    super(new Utf8String(name), new Bool(isHealthy), new Uint(usedSpace), new Uint(totalSpace));
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
