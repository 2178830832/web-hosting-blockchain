package pers.yujie.dashboard.service;

import java.math.BigInteger;

public interface BlockService {

  void redistributeNodeSize(BigInteger nodeId, BigInteger size);

  //void distribute blocks

}
