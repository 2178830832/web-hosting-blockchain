package pers.yujie.dashboard.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import pers.yujie.dashboard.Common.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@SuppressWarnings("deprecation")
public class DockerUtil {

  @Value("${linux.ip}")
  private String linuxIp;

  @Value("${linux.docker.port}")
  private String linuxDockerPort;

  @Value("${linux.docker.volumes}")
  private String volumes;

  private DockerClient docker;

  @PostConstruct
  private void initDocker() {
//    connectDocker();
//    checkIPFSImage();
  }

  @PreDestroy
  private void exitDocker() {
    if (docker != null) {
      docker.disconnectFromNetworkCmd().exec();
    }
  }

  private void connectDocker() {
    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("tcp://" + linuxIp + ":" + linuxDockerPort)
        .build();
    DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
        .dockerHost(config.getDockerHost())
        .connectionTimeout(Duration.ofSeconds(10))
        .build();
    docker = DockerClientImpl.getInstance(config, httpClient);
    if (docker == null) {
      log.info("Unable to connect to docker at: " + config.getDockerHost());
      AppUtil.exitApplication();
    }
    log.info("Connected to docker at: " + config.getDockerHost());
  }

  private void checkIPFSImage() {
    List<Image> images = docker.listImagesCmd().withImageNameFilter("ipfs/kubo").exec();
    if (images.isEmpty()) {
      log.info("Linux does not contain IPFS docker image, start pulling from the hub");
      docker.pullImageCmd("busybox:latest").exec(new PullImageResultCallback()).awaitSuccess();
    }
  }

  public List<String> getNameContainerList() {
    List<Container> containers = docker.listContainersCmd().withNameFilter(
        Collections.singleton(Constants.IPFS_PREFIX)).exec();

    List<String> containerNameList = new ArrayList<>();
    for (Container container : containers) {
      containerNameList.add(container.getNames()[0]);
    }
    return containerNameList;
  }

  public void startContainer(String containerName) {
    List<Container> containers = docker.listContainersCmd()
        .withNameFilter(Collections.singleton(containerName))
        .withShowAll(true)
        .exec();

    if (containers.size() < 1) {
      docker.createContainerCmd("ipfs/kubo:latest")
          .withName(containerName)
          .withVolumes(Volume.parse(volumes + containerName + ":/data/ipfs"))
          .exec();
      docker.startContainerCmd(containerName).exec();
      return;
    }
    String status = containers.get(0).getStatus().toLowerCase(Locale.ROOT);
    if (status.contains("created")) {
      docker.startContainerCmd(containerName).exec();
    } else if (status.contains("exited")) {
      System.out.println(containerName.getClass());
      docker.restartContainerCmd(containerName).exec();
    }
  }

  public void execDockerCmd(String containerName, String... cmd) {
    ExecCreateCmdResponse execCreateCmdResponse = docker.execCreateCmd(containerName)
        .withAttachStdout(true)
        .withAttachStderr(true)
        .withCmd(cmd)
        .exec();
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ByteArrayOutputStream errors = new ByteArrayOutputStream();
    log.info("Executing cmd: " + Arrays.toString(cmd));
    docker.execStartCmd(execCreateCmdResponse.getId())
        .exec(new ExecStartResultCallback(output, errors));
    //awaitCompletion
    log.info(output.toString());
  }
}
