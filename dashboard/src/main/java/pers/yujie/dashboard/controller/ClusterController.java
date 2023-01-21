package pers.yujie.dashboard.controller;

import cn.hutool.json.JSONUtil;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.yujie.dashboard.dao.ClusterDao;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Website;

@Controller
@RequestMapping("/cluster")
public class ClusterController {
  @Resource
  private ClusterDao clusterDao;

  @GetMapping("/list")
  public ResponseEntity<String> listWebsites() {
    List<Cluster> clusters = clusterDao.selectAllCluster();

    return new ResponseEntity<>(JSONUtil.toJsonStr(clusters), HttpStatus.OK);
  }
}
