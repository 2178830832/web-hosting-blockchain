package pers.yujie.dashboard.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LinuxUtil {

  @Value("${linux.ip}")
  private String linuxIp;

  @Value("${linux.userName}")
  private String linuxUserName;

  @Value("${linux.userPassword}")
  private String linuxUserPassword;

  private SSHClient ssh;

  private Session session;

  @PostConstruct
  private void initLinux() {
//    DockerClient docker;
//    DockerClientConfig custom;
//    DockerHttpClient httpClient;
//    custom = DefaultDockerClientConfig.createDefaultConfigBuilder()
//        .withDockerHost("tcp://10.176.32.128:2375")
//        .build();
//    httpClient = new ApacheDockerHttpClient.Builder()
//        .dockerHost(custom.getDockerHost())
//        .build();
//    docker = DockerClientImpl.getInstance(custom, httpClient);
//    Info info = docker.infoCmd().exec();
//    System.out.println(info.toString());
  }

  public String executeCmd(String cmd) {
    try {
      ssh = new SSHClient();
      ssh.loadKnownHosts();
      ssh.addHostKeyVerifier(new PromiscuousVerifier());
      ssh.connect(linuxIp);
      ssh.authPassword(linuxUserName, linuxUserPassword);
      session = ssh.startSession();

      log.info(cmd);
      String response = IOUtils.readFully(session.exec(cmd).getInputStream()).toString();
      log.info(response);
      session.close();
      ssh.disconnect();
      return response;
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (session != null) {
          session.close();
        }
        ssh.disconnect();
      } catch (IOException e) {
        // Do Nothing
      }
    }
    return null;
  }
}