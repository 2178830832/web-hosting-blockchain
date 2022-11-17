package org.nottingham.dashboard.controller;

import javax.annotation.Resource;
import org.nottingham.dashboard.service.WebsiteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebsiteController {
  @Resource
  private WebsiteService websiteService;

  @RequestMapping("/home")
  public String getHomePage() {
    return "index";
  }

  @RequestMapping("/submit")
  public String submitForm(@RequestParam("folderPath") String path) {
    System.out.println(path);
    websiteService.uploadWebsite(path);
    return "index";
  }

}
