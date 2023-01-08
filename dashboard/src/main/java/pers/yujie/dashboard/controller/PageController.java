package pers.yujie.dashboard.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.yujie.dashboard.entity.Cluster;
import pers.yujie.dashboard.entity.Website;

@Controller
public class PageController {

//  @GetMapping({"", "/", "/index", "/index.html"})
//  public String getHomePage() {
//    return "index";
//  }
//
//  @GetMapping({ "/website"})
//  public String getWebsitePage() {
//    return "website";
//  }

  @GetMapping({"/test"})
  public ResponseEntity<String> test() {
    List<Website> list = new ArrayList<>();
    Website cluster = new Website("test", "location", true);
    Website cluster1 = new Website("tes2t", "location", true);
    list.add(cluster);
    list.add(cluster1);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("data", list);
    jsonObject.put("draw", 1);
    jsonObject.put("recordsTotal", 20);
    jsonObject.put("recordsFiltered", 20);
    return new ResponseEntity<>(jsonObject.toJSONString(), HttpStatus.OK);
  }
}
