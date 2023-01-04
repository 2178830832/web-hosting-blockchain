package pers.yujie.dashboard.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

  @ResponseBody
  @PostMapping("/metric")
  public String getRequest(@RequestBody JSONObject matrix){
    log.info(String.valueOf(matrix));
    return "success";
  }
}
