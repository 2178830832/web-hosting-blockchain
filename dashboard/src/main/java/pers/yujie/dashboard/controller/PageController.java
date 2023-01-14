package pers.yujie.dashboard.controller;

import com.alibaba.fastjson2.JSONObject;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    Website cluster2 = new Website("tes2t", "location", true);
    Website cluster3 = new Website("tes2t", "location", true);
    Website cluster4 = new Website("tes2t", "location", true);
    Website cluster5 = new Website("tes2t", "location", true);
    Website cluster6 = new Website("tes2t", "location", true);
    Website cluster7 = new Website("tes2t", "location", true);
    Website cluster8 = new Website("tes2t", "location", true);
    Website cluster9 = new Website("tes2t", "location", true);
    Website cluster10 = new Website("tes2t", "location", true);

    list.add(cluster);
    list.add(cluster1);
    list.add(cluster2);
    list.add(cluster3);
    list.add(cluster4);
    list.add(cluster5);
    list.add(cluster6);
    list.add(cluster7);
    list.add(cluster8);
    list.add(cluster9);
    list.add(cluster10);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("data", list);
    jsonObject.put("draw", 1);
    jsonObject.put("recordsTotal", 11);
    jsonObject.put("recordsFiltered", 11);
    return new ResponseEntity<>(jsonObject.toJSONString(), HttpStatus.OK);
  }
}
