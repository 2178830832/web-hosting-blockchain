package pers.yujie.dashboard.service;

import com.alibaba.fastjson2.JSONObject;

public interface ConfigService {

  void connectIPFS(String port);

  void connectDocker(String port);

  void connectWeb3(String port);

  JSONObject getIPFSStatus();

  JSONObject getDockerStatus();

  JSONObject getWeb3Status();

}
