package pers.yujie.dashboard.utils;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;

public class Web3JUtil {
    private static String ip = "http://127.0.0.1:8545";

    private Web3JUtil() {
    }

    private volatile static Web3j web3j;
    private volatile static Admin admin;

    public static Web3j getClient() {
        if (web3j == null) {
            synchronized (Web3JUtil.class) {
                if (web3j == null) {

                    web3j = Web3j.build(new HttpService(ip));
                }
            }
        }
        return web3j;
    }

    public static Admin getAdmin() {
        if (admin == null) {
            synchronized (Web3JUtil.class) {
                if (admin == null) {


                    admin = Admin.build(new HttpService(ip));
                }
            }
        }
        return admin;
    }
}

