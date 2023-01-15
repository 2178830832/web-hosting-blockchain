package pers.yujie.dashboard.controller;

import com.alibaba.fastjson2.JSONObject;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.yujie.dashboard.service.ConfigService;

@Controller
@Slf4j
public class ConfigController {

  @Resource
  private ConfigService configService;

  @GetMapping({"", "/", "/index", "/index.html"})
  public String index(Model model) {
    ResponseEntity<String> response = getConfigStatus();
    JSONObject status = JSONObject.parseObject(response.getBody());
    assert status != null;

    model.addAttribute("ipfsAddress", status.getJSONObject("ipfs")
        .getString("Address"));
    model.addAttribute("ipfsVersion", status.getJSONObject("ipfs")
        .getString("AgentVersion"));
    model.addAttribute("ipfsID", status.getJSONObject("ipfs")
        .getString("ID"));

    model.addAttribute("dockerAddress", status.getJSONObject("docker")
        .getString("Address"));
    model.addAttribute("dockerVersion", status.getJSONObject("docker")
        .getString("ServerVersion"));
    model.addAttribute("dockerName", status.getJSONObject("docker")
        .getString("Name"));
    model.addAttribute("dockerSystem", status.getJSONObject("docker")
        .getString("OperatingSystem"));
    model.addAttribute("dockerContainer", status.getJSONObject("docker")
        .getString("Containers"));
    model.addAttribute("dockerRunning", status.getJSONObject("docker")
        .getString("ContainersRunning"));

    model.addAttribute("web3Client", status.getJSONObject("web3")
        .getString("Client"));
    model.addAttribute("web3Address", status.getJSONObject("web3")
        .getString("Address"));
    model.addAttribute("web3Account", status.getJSONObject("web3")
        .getString("Account"));
    model.addAttribute("web3Balance", status.getJSONObject("web3")
        .getString("Balance"));
    model.addAttribute("web3Contract", status.getJSONObject("web3")
        .getString("Contract"));
    return "index";
  }

  @GetMapping("/config")
  public ResponseEntity<String> getConfigStatus() {
    JSONObject status = new JSONObject();
    status.put("ipfs", configService.getIPFSStatus());
    status.put("docker", configService.getDockerStatus());
    status.put("web3", configService.getWeb3Status());
    return new ResponseEntity<>(status.toJSONString(), HttpStatus.OK);
  }

  @PostMapping("/config/ipfs")
  @ResponseBody
  public ResponseEntity<String> setCustomIPFS(@RequestBody JSONObject request) {
    String message = configService.connectIPFS(request.getString("address"));

    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

  }

  @PostMapping("/config/web3")
  public ResponseEntity<String> setCustomWeb3(@RequestBody JSONObject request) {
    String message = configService.connectWeb3(request.getString("address"));
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/config/docker")
  public ResponseEntity<String> setCustomDocker(@RequestBody JSONObject request) {
    String message = configService.connectDocker(request.getString("address"));

    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

  }

}
