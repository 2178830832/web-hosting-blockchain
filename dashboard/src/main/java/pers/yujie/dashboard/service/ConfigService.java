package pers.yujie.dashboard.service;

public interface ConfigService {

  void connectIPFS(String port);

  void connectDocker(String port);

  void connectWeb3(String port);

}
