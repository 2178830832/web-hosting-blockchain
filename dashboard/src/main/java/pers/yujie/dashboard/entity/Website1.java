package pers.yujie.dashboard.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Utf8String;

@EqualsAndHashCode(callSuper = true)
@Data
public class Website1 extends DynamicStruct {

  public Website1(Utf8String cid, Utf8String location, Bool isOnline) {
    super(cid, location, isOnline);
    this.cid = cid.getValue();
    this.location = location.getValue();
    this.isOnline = isOnline.getValue();
  }

  public Website1(String cid, String location, boolean isOnline) {
    super(new Utf8String(cid), new Utf8String(location), new Bool(isOnline));
    this.cid = cid;
    this.location = location;
    this.isOnline = isOnline;
  }

  private String cid;

  private String location;

  private boolean isOnline;


}
