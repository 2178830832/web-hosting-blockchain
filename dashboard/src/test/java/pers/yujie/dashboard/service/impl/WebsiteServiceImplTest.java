package pers.yujie.dashboard.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import pers.yujie.dashboard.service.ConfigService;
import pers.yujie.dashboard.service.WebsiteService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebsiteServiceImplTest {

  @Autowired
  WebsiteService websiteService;

  @Test
  @Order(1)
  void testUploadWebsite() {
    JSONObject website = JSONUtil.createObj();
    website.set("name", "website");
    assertDoesNotThrow(() -> website.set("path",
        new ClassPathResource("application.properties").getFile().getAbsolutePath()));

    assertEquals(websiteService.uploadWebsite(website), "");
  }
}