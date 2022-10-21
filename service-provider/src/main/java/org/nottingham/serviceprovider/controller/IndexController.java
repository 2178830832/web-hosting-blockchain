package org.nottingham.serviceprovider.controller;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
  @RequestMapping("/template")
  public String hello() {
    return "template";
  }
}
