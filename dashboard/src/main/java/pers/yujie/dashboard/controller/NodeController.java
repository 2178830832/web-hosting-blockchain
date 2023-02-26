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
import pers.yujie.dashboard.common.Encrypted;
import pers.yujie.dashboard.service.NodeService;

/**
 * Contains the {@link Controller} related to the controller page. The business logic is in {@link
 * NodeService}.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 15/01/2023
 */
@Controller
@RequestMapping("/node")
public class NodeController {

  @Resource
  private NodeService nodeService;

  /**
   * List available nodes.
   *
   * @return {@link ResponseEntity} containing the node information
   */
  @Encrypted
  @GetMapping("/list")
  public ResponseEntity<String> listNode() {
    List<JSONObject> nodes = nodeService.selectAllNode();

    return new ResponseEntity<>(JSONUtil.toJsonStr(nodes), HttpStatus.OK);
  }

  /**
   * Update an existing node.
   *
   * @param node {@link JSONObject} representing the node to be updated
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} otherwise
   */
  @Encrypted
  @PostMapping("/update")
  public ResponseEntity<String> updateNode(@RequestBody JSONObject node) {
    String message = nodeService.updateNode(node);
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Insert a new node.
   *
   * @param node {@link JSONObject} representing the node to be inserted
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} otherwise
   */
  @Encrypted
  @PostMapping("/insert")
  public ResponseEntity<String> insertNode(@RequestBody JSONObject node) {
    String message = nodeService.insertNode(node);
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Delete an existing node.
   *
   * @param node {@link JSONObject} representing the node to be deleted
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} otherwise
   */
  @Encrypted
  @PostMapping("/delete")
  public ResponseEntity<String> deleteNode(@RequestBody JSONObject node) {
    String message = nodeService.deleteNode(node.getBigInteger("id"));
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Change the status of a node to online or offline.
   *
   * @param node {@link JSONObject} representing the node to be changed
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} otherwise
   */
  @Encrypted
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
