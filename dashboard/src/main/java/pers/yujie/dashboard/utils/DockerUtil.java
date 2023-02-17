package pers.yujie.dashboard.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pers.yujie.dashboard.common.Constants;

@Component
@Slf4j
@SuppressWarnings("deprecation")
public class DockerUtil {

  @Getter
  @Setter
  private static DockerClient docker;

  @Getter
  @Setter
  private static String address;


  public List<String> getNameContainerList() {
    List<Container> containers = docker.listContainersCmd().withNameFilter(
        Collections.singleton(Constants.IPFS_PREFIX)).exec();

    List<String> containerNameList = new ArrayList<>();
    for (Container container : containers) {
      containerNameList.add(container.getNames()[0]);
    }
    return containerNameList;
  }

  public static void removeContainer(String containerName) {
    List<Container> containers = docker.listContainersCmd()
        .withNameFilter(Collections.singleton(containerName))
        .withShowAll(true)
        .exec();

    if (containers.size() > 0) {
      docker.stopContainerCmd(containers.get(0).getId()).exec();
      docker.removeContainerCmd(containers.get(0).getId()).exec();
    }
  }

  public static void startContainer(String containerName) {
    List<Container> containers = docker.listContainersCmd()
        .withNameFilter(Collections.singleton(containerName))
        .withShowAll(true)
        .exec();

    if (containers.size() > 0) {
      return;
    }
    CreateContainerResponse container = docker.createContainerCmd("ipfs/kubo:latest")
        .withName(containerName)
        .withBinds(new Bind(Constants.DOCKER_VOLUME + containerName,
            new Volume("/data/ipfs")))
        .exec();
    docker.startContainerCmd(container.getId()).exec();

//    if (containers.size() < 1) {
//      CreateContainerResponse container = docker.createContainerCmd("ipfs/kubo:latest")
//          .withName(containerName)
////          .withVolumes(Volume.parse(volumes + containerName + ":/data/ipfs"))
//          .exec();
//      docker.startContainerCmd(container.getId()).exec();
//      return;
//    }
//    String status = containers.get(0).getStatus().toLowerCase(Locale.ROOT);
//    if (status.contains("created")) {
//      docker.startContainerCmd(containers.get(0).getId()).exec();
//    } else if (status.contains("exited")) {
//      docker.restartContainerCmd(containers.get(0).getId()).exec();
//    }
  }

  public static void execDockerCmd(String containerName, String... cmd)
      throws InterruptedException {
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
//        .awaitCompletion();
    log.info(output.toString());
  }
}
