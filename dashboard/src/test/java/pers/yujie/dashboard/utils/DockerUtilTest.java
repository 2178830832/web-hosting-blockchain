package pers.yujie.dashboard.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Binds;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.transport.DockerHttpClient;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DockerUtilTest {

  static DockerClient docker;
  DockerClientConfig custom;
  DockerHttpClient httpClient;

  @BeforeAll
  static void setUp() {
    DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("tcp://124.223.10.94:2375")
//        .withDockerHost("tcp://192.168.80.128:2375")
        .build();

    // Create a Docker client
    docker = DockerClientBuilder.getInstance(config).build();
  }

  @AfterAll
  static void clear() throws IOException {
    docker.close();
  }

  @Test
  void testConnect() {
    Info info = docker.infoCmd().exec();
    System.out.println(info.toString());
  }

  @Test
  void start() {
    for (int i = 0; i<100;i++) {
      CreateContainerResponse container = docker.createContainerCmd("ipfs/kubo")
          .withName("test" + i)
          .withBinds(new Bind("/home/yujie/Desktop/FYP/compose/test" + i, new Volume("/data/ipfs")))
          .exec();

      // Start the container
      docker.startContainerCmd(container.getId()).exec();
    }
  }

  void execCmd() {
    //
  }

  @Test
  void testExec() throws InterruptedException {
    List<PortBinding> portBindings = new ArrayList<>();

// Bind container port 443 to host port 8443
    portBindings.add(new PortBinding(new Binding(null, "5001"), ExposedPort.tcp(5001)));
    portBindings.add(new PortBinding(new Binding(null, "8080"), ExposedPort.tcp(8080)));
    portBindings.add(new PortBinding(new Binding(null, "4001"), ExposedPort.tcp(4001)));

    // Create a container
    CreateContainerResponse container = docker.createContainerCmd("ipfs/kubo")
        .withName("test1")
//        .withPortBindings(portBindings)
        .withBinds(new Bind("/home/lighthouse/fyp/compose/test1", new Volume("/data/ipfs")))
        .exec();

    // Start the container
    docker.startContainerCmd(container.getId()).exec();


  }

  @Test
  void testImage() throws InterruptedException {
//    docker.pullImageCmd("hello-world").exec(new PullImageResultCallback()).aw);
    docker.pullImageCmd("hello-world")
        .exec(new PullImageResultCallback() {
          public void onNext(PullResponseItem item) {
            // Display progress updates
            System.out.println("Received progress update: " + item.getStatus());
          }

          public void onError(Throwable throwable) {
            // Handle errors
            System.out.println("Error pulling image: " + throwable.getMessage());
          }

          public void onComplete() {
            // Image pull complete
            System.out.println("Image pull complete");
          }
        }).awaitCompletion();
  }


  @Test
  void testContainer() {

//    List<Network> networks = docker.listNetworksCmd()
//        .withNameFilter("java-docker-mssql")
//        .exec();
//    Network network = null;
//
//    while (networks.size() > 0) {
//      docker.removeNetworkCmd("java-docker-mssql");
//      networks.remove(0);
//    }
//    docker.createNetworkCmd()
//        .withName("java-docker-mssql")
//        .withIpam(new Ipam().withConfig(new Config().withSubnet("10.176.32.0/20")))
//        .withDriver("bridge").exec();

    String containerName = "ipfs-net";
    List<Container> containers = docker.listContainersCmd()
        .withNameFilter(Collections.singleton(containerName))
        .withShowAll(true)
        .exec();

    if (containers.size() > 0) {
      docker.stopContainerCmd(containers.get(0).getId()).exec();
      docker.removeContainerCmd(containers.get(0).getId()).exec();
    }
    CreateContainerResponse container = docker.createContainerCmd("ipfs/kubo:latest")
        .withName(containerName)
        .withPortBindings(PortBinding.parse("5001:5001"),
            PortBinding.parse("8080:8080"), PortBinding.parse("4001:4001"))
        .exec();
    docker.startContainerCmd(container.getId()).exec();
  }
}