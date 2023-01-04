package pers.yujie.dashboard.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import io.ipfs.api.IPFS;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pers.yujie.dashboard.common.Constants;

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

  @Resource
  ApplicationContext ctx;

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
    try {
      docker.infoCmd().exec();
    } catch (RuntimeException e) {
      log.info("Unable to connect to docker at: " + config.getDockerHost());
      AppUtil.exitApplication(ctx, 2);
    }
    log.info("Connected to Docker at: " + config.getDockerHost());
  }

  private void checkIPFSImage() {
    try {
      List<Image> images = docker.listImagesCmd().withImageNameFilter("ipfs/kubo").exec();
      if (images.isEmpty()) {
        log.info("Linux does not contain IPFS docker image, start pulling from the hub");
        docker.pullImageCmd("ipfs/kubo:latest")
            .exec(new PullImageResultCallback()).awaitCompletion();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
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
      CreateContainerResponse container = docker.createContainerCmd("ipfs/kubo:latest")
          .withName(containerName)
          .withVolumes(Volume.parse(volumes + containerName + ":/data/ipfs"))
          .exec();
      docker.startContainerCmd(container.getId()).exec();
      return;
    }
    String status = containers.get(0).getStatus().toLowerCase(Locale.ROOT);
    if (status.contains("created")) {
      docker.startContainerCmd(containers.get(0).getId()).exec();
    } else if (status.contains("exited")) {
      docker.restartContainerCmd(containers.get(0).getId()).exec();
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
