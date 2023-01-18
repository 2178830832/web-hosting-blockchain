package pers.yujie.dashboard.service;

import cn.hutool.json.JSONObject;

public interface ConfigService {

  String connectIPFS(String address);

  String connectDocker(String address);

  String connectWeb3(String address, String account, String contract);

  JSONObject getIPFSStatus();

  JSONObject getDockerStatus();

  JSONObject getWeb3Status();

}
