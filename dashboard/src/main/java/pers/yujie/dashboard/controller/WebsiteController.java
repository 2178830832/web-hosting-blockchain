package pers.yujie.dashboard.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.io.File;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pers.yujie.dashboard.common.Encrypted;
import pers.yujie.dashboard.dao.WebsiteDao;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.service.WebsiteService;

@Controller
@RequestMapping("/website")
public class WebsiteController {

  @Resource
  private WebsiteService websiteService;

  @Encrypted
  @GetMapping("/list")
  public ResponseEntity<String> listWebsite() {
    List<JSONObject> websites = websiteService.selectAllWebsite();

    return new ResponseEntity<>(JSONUtil.toJsonStr(websites), HttpStatus.OK);
  }

  @Encrypted
  @PostMapping("/update")
  public ResponseEntity<String> updateWebsite(@RequestBody JSONObject website) {
    String message = websiteService.updateWebsite(website);
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  @Encrypted
  @PostMapping("/insert")
  public ResponseEntity<String> uploadWebsite(@RequestBody JSONObject website) {
    String message = websiteService.uploadWebsite(website);
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  @Encrypted
  @PostMapping("/delete")
  public ResponseEntity<String> deleteWebsite(@RequestBody JSONObject website) {
    String message = websiteService.deleteWebsite(website.getBigInteger("id"));
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
    }
  }

}
