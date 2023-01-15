package pers.yujie.dashboard.service;

import com.alibaba.fastjson2.JSONObject;

public interface ConfigService {

  String connectIPFS(String address);

  String connectDocker(String address);

  String connectWeb3(String address);

  JSONObject getIPFSStatus();

  JSONObject getDockerStatus();

  JSONObject getWeb3Status();

}
