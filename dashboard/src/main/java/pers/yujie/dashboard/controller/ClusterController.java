package pers.yujie.dashboard.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.yujie.dashboard.service.ClusterService;

@Controller
@RequestMapping("/cluster")
public class ClusterController {

  @Resource
  private ClusterService clusterService;

  @GetMapping("/list")
  public ResponseEntity<String> listWebsites() {
    List<JSONObject> clusters = clusterService.selectAllCluster();

    return new ResponseEntity<>(JSONUtil.toJsonStr(clusters), HttpStatus.OK);
  }
}
