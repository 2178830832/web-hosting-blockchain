package pers.yujie.dashboard.dao.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.yujie.dashboard.common.Constants;
import pers.yujie.dashboard.dao.WebsiteDao;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.service.ConfigService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WebsiteDaoImplTest {

  @Autowired
  ConfigService configService;
  @Autowired
  WebsiteDao websiteDao;

  @BeforeEach
  void setUp() {
    configService
        .connectWeb3(Constants.WEB3_ADDRESS, Constants.WEB3_ACCOUNT, Constants.WEB3_CONTRACT);
    websiteDao.initWebsiteDao();
  }

  @Test
  @Order(1)
  void testInsertWebsite() {
    Website website = new Website(BigInteger.ZERO, "test", "CID",
        BigInteger.ZERO, "created", JSONUtil.createArray());
    assertTrue(websiteDao.insertWebsite(JSONUtil.parseObj(website)));
  }

  @Test
  @Order(2)
  void testUpdateWebsite() {
    Website website = new Website(BigInteger.ZERO, "test", "CID",
        BigInteger.ZERO, "updated", JSONUtil.createArray());
    assertTrue(websiteDao.updateWebsite(JSONUtil.parseObj(website)));
  }

  @Test
  @Order(3)
  void testDeleteWebsite() {
    assertTrue(websiteDao.deleteWebsite(BigInteger.ZERO));
  }
}