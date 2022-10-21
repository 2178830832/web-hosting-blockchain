package org.nottingham.serviceprovider.utils;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.geth.Geth;
import org.web3j.protocol.http.HttpService;

public class Web3jClient {

  private static final String local_ip = "http://127.0.0.1:3000";

  private volatile static Web3j web3j;
  private volatile static Admin admin;
  private volatile static Geth geth;

  public static Web3j getClient() {
    if (web3j == null) {
      synchronized (Web3jClient.class) {
        if (web3j == null) {
          web3j = Web3j.build(new HttpService(local_ip));
        }
      }
    }
    return web3j;
  }

  public static Admin getAdmin() {
    if (admin == null) {
      synchronized (Web3jClient.class) {
        if (admin == null) {
          admin = Admin.build(new HttpService(local_ip));
        }
      }
    }
    return admin;
  }

  public static Geth getGeth() {
    if (geth == null) {
      synchronized (Web3jClient.class) {
        if (geth == null) {
          geth = Geth.build(new HttpService(local_ip));
        }
      }
    }
    return geth;
  }

}
