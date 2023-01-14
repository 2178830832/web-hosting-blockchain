package pers.yujie.dashboard.controller;

import com.alibaba.fastjson2.JSONObject;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    model.addAttribute("test", status.getString("web3"));
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

}
