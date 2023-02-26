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
import pers.yujie.dashboard.common.Encrypted;
import pers.yujie.dashboard.service.ClusterService;

/**
 * Contains the {@link Controller} related to the controller page. The business logic is in {@link
 * ClusterService}.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 15/01/2023
 */
@Controller
@RequestMapping("/cluster")
public class ClusterController {

  @Resource
  private ClusterService clusterService;

  /**
   * List available clusters.
   *
   * @return {@link ResponseEntity} containing the cluster information
   */
  @Encrypted
  @GetMapping("/list")
  public ResponseEntity<String> listClusters() {
    List<JSONObject> clusters = clusterService.selectAllCluster();

    return new ResponseEntity<>(JSONUtil.toJsonStr(clusters), HttpStatus.OK);
  }
}
