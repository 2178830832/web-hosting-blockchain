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
import pers.yujie.dashboard.service.WebsiteService;

/**
 * Contains the {@link Controller} related to the controller page. The business logic is in {@link
 * WebsiteService}.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 13/12/2022
 */
@Controller
@RequestMapping("/website")
public class WebsiteController {

  @Resource
  private WebsiteService websiteService;

  /**
   * List available websites.
   *
   * @return {@link ResponseEntity} containing website information
   */
  @Encrypted
  @GetMapping("/list")
  public ResponseEntity<String> listWebsite() {
    List<JSONObject> websites = websiteService.selectAllWebsite();

    return new ResponseEntity<>(JSONUtil.toJsonStr(websites), HttpStatus.OK);
  }

  /**
   * Update a specific website.
   *
   * @param website {@link JSONObject} of the website to be updated
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} otherwise.
   */
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

  /**
   * Upload a new website.
   *
   * @param website {@link JSONObject} of the website to be uploaded
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} otherwise.
   */
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

  /**
   * Delete an existing website.
   *
   * @param website {@link JSONObject} of the website to be deleted
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} otherwise.
   */
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
