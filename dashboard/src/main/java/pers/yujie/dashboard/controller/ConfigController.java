package pers.yujie.dashboard.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.yujie.dashboard.common.Constants;
import pers.yujie.dashboard.service.ConfigService;
import pers.yujie.dashboard.utils.Web3JUtil;

@Controller
@Slf4j
public class ConfigController {

  @Resource
  private ConfigService configService;

  @GetMapping({"", "/", "/index", "/index.html"})
  public String renderIndexPage(Model model) {
    ResponseEntity<String> response = getConfigStatus();
    JSONObject status = JSONUtil.parseObj(response.getBody());

    model.addAttribute("status", status);
    return "index";
  }

  @GetMapping("/config")
  public ResponseEntity<String> getConfigStatus() {
    JSONObject status = new JSONObject();
    status.set("ipfs", configService.getIPFSStatus());
    status.set("docker", configService.getDockerStatus());
    status.set("web3", configService.getWeb3Status());
    return new ResponseEntity<>(status.toString(), HttpStatus.OK);
  }

  @PostMapping("/config/ipfs")
  public ResponseEntity<String> setCustomIPFS(@RequestBody JSONObject request) {
    String message = configService.connectIPFS(request.getStr("address"));

    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

  }

  @PostMapping("/config/web3")
  public ResponseEntity<String> setCustomWeb3(@RequestBody JSONObject request) {
    String address = (request.getStr("address") == null) ?
        Web3JUtil.getAddress() : request.getStr("address");
    String account = (request.getStr("account") == null) ?
        Web3JUtil.getAccount() : request.getStr("account");
    String contract = (request.getStr("contract") == null) ?
        Web3JUtil.getContract() : request.getStr("contract");

    String message = configService.connectWeb3(address, account, contract);
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/config/docker")
  public ResponseEntity<String> setCustomDocker(@RequestBody JSONObject request) {
    String message = configService.connectDocker(request.getStr("address"));

    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

  }

}
