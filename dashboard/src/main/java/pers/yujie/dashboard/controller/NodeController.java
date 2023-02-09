package pers.yujie.dashboard.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.yujie.dashboard.entity.Node;
import pers.yujie.dashboard.service.NodeService;

@Controller
@RequestMapping("/node")
public class NodeController {

  @Resource
  private NodeService nodeService;

  @GetMapping("/list")
  public ResponseEntity<String> listNode() {
    List<JSONObject> nodes = nodeService.selectAllNode();

    return new ResponseEntity<>(JSONUtil.toJsonStr(nodes), HttpStatus.OK);
  }

  @PostMapping("/update")
  public ResponseEntity<String> updateNode(@RequestBody JSONObject node) {
    String message = nodeService.updateNode(node);
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/insert")
  public ResponseEntity<String> insertNode(@RequestBody JSONObject node) {
    String message = nodeService.insertNode(node);
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/delete")
  public ResponseEntity<String> deleteNode(@RequestBody JSONObject node) {
    String message = nodeService.deleteNode(node.getBigInteger("id"));
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/status")
  public ResponseEntity<String> changeNodeStatus(@RequestBody JSONObject node) {
    System.out.println(node);
    String message = nodeService.changeNodeStatus(node.getBigInteger("id"));
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
    }
  }
}
