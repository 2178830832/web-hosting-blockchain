package pers.yujie.dashboard.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DockerUtilTest {

  static final String DOCKER_ADDRESS = "tcp://124.223.10.94:2375";

  @BeforeAll
  static void setUp() {
    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost(DOCKER_ADDRESS)
        .build();
    // create a Docker client
    DockerUtil.setDocker(DockerClientBuilder.getInstance(config).build());
  }

  @AfterAll
  static void tearDown() {
    assertDoesNotThrow(() -> DockerUtil.getDocker().close());
  }

  @Test
  void testConnectDocker() {
    DockerUtil.getDocker().infoCmd().exec();
  }

  @Test
  void testImage() {
    String imageName = "ipfs/kubo:latest";
    List<Image> images = DockerUtil.getDocker().listImagesCmd()
        .withImageNameFilter(imageName).exec();
    assertFalse(images.isEmpty());
  }

  @Test
  void testContainerNameList() {
    List<String> nameList = DockerUtil.getContainerNameList();
    assertFalse(nameList.isEmpty());
  }

  @Test
  @SuppressWarnings("deprecation")
  void testContainerAndExec() {
    String containerName = "test";
    List<PortBinding> portBindings = new ArrayList<>();
    portBindings.add(new PortBinding(
        new Binding(null, "5001"), ExposedPort.tcp(5002)));

    // create a test container
    DockerUtil.getDocker().createContainerCmd("ipfs/kubo")
        .withName(containerName)
        .withPortBindings(portBindings)
        .exec();

    List<Container> containers = DockerUtil.getDocker().listContainersCmd()
        .withNameFilter(Collections.singleton(containerName))
        .withShowAll(true)
        .exec();
    assertFalse(containers.isEmpty());

    // start the test container
    DockerUtil.getDocker().startContainerCmd(containers.get(0).getId()).exec();

    // execute a command inside the container
    assertDoesNotThrow(() -> DockerUtil.execDockerCmd(containerName, "ipfs", "id"));

    // stop and delete the test container
    DockerUtil.getDocker().stopContainerCmd(containers.get(0).getId()).exec();
    DockerUtil.getDocker().removeContainerCmd(containers.get(0).getId()).exec();
  }
}