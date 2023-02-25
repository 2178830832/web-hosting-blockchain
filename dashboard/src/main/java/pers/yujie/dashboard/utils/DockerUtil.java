package pers.yujie.dashboard.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pers.yujie.dashboard.common.Constants;

/**
 * This is a utility class responsible for manipulating Docker.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 19/10/2022
 */
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

  /**
   * Get a list of names of all containers that have the IPFS prefix.
   *
   * @return {@link List} of names
   */
  public List<String> getContainerNameList() {
    List<Container> containers = docker.listContainersCmd().withNameFilter(
        Collections.singleton(Constants.IPFS_PREFIX)).exec();

    List<String> containerNameList = new ArrayList<>();
    for (Container container : containers) {
      containerNameList.add(container.getNames()[0]);
    }
    return containerNameList;
  }

  /**
   * Remove a container.
   *
   * @param containerName the name of the container to be removed
   */
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

  /**
   * Start a container.
   *
   * @param containerName the name of the container to be started
   */
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
  }

  /**
   * Execute commands inside a container.
   *
   * @param containerName the name of the container
   * @param cmd           a list of commands
   * @throws InterruptedException exception thrown by {@link ResultCallbackTemplate#awaitCompletion()}
   */
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
    // add the following line to wait for completion
//        .awaitCompletion();
    log.info(output.toString());
  }
}
