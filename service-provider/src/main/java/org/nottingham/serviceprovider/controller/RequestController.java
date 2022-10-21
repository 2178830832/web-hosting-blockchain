package org.nottingham.serviceprovider.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class RequestController {
  @PostMapping("/index")
  public void getRequest(@RequestBody JSONObject matrix){
    System.out.println(matrix.get("value"));
  }
}
