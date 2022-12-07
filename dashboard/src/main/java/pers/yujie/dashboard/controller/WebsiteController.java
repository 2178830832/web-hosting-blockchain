package pers.yujie.dashboard.controller;

import java.io.File;
import java.math.BigInteger;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import pers.yujie.dashboard.service.WebsiteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class WebsiteController {
  @Resource
  private WebsiteService websiteService;

  @RequestMapping("/home")
  public String getHomePage() {
    return "index";
  }

  @RequestMapping("/submit")
  public String submitForm(@RequestParam("websitePath") String websitePath) {
    File websiteFile = new File(websitePath);
    if (!websiteFile.exists()) {
      log.warn("\"" + websitePath + "\" is neither a file nor a directory");
      return "index";
    }
    websiteService.uploadWebsite(websiteFile);
    return "index";
  }

  public String deleteWebsite(@RequestParam("websiteId") int websiteId) {
    websiteService.deleteWebsite(BigInteger.valueOf(websiteId));
    return "index";
  }

}
