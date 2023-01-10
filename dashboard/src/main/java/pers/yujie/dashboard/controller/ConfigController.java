package pers.yujie.dashboard.controller;

import com.alibaba.fastjson2.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.yujie.dashboard.common.Constants;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.service.ConfigService;
import pers.yujie.dashboard.utils.IPFSUtil;

@Controller
@RequestMapping("/config")
@Slf4j
public class ConfigController {

  @GetMapping({"/ipfs"})
  public ResponseEntity<String> getIPFSStatus() {
    try {
      JSONObject status = JSONObject.parseObject(JSONObject.toJSONString(IPFSUtil.getIpfs().id()));
      status.put("Port", Constants.IPFS_PORT);
      return new ResponseEntity<>(status.toJSONString(), HttpStatus.OK);
    } catch (IOException e) {
      log.error("Error when requesting ipfs id");
    }
    return new ResponseEntity<>(null, HttpStatus.OK);
  }
}
