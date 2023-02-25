package pers.yujie.dashboard.service;

import cn.hutool.json.JSONObject;

/**
 * This is the interface providing config services for outer usages.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see pers.yujie.dashboard.service.impl.ConfigServiceImpl
 * @since 05/01/2023
 */
public interface ConfigService {

  String connectIPFS(String address);

  String connectDocker(String address);

  String connectWeb3(String address, String account, String contract);

  JSONObject getIPFSStatus();

  JSONObject getDockerStatus();

  JSONObject getWeb3Status();

}
