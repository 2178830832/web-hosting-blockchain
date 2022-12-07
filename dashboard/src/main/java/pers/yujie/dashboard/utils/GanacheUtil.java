package pers.yujie.dashboard.utils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

@Component
public class GanacheUtil {

  @Resource
  public Web3j web3j;

  @PostConstruct
  void init() {
    System.out.println(web3j);
  }
}
