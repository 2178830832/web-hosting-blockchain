package pers.yujie.dashboard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.SerializeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import io.ipfs.api.IPFS;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Website1ServiceImplTest {
  IPFS ipfs;

  @BeforeEach
  void setUp() {
//    ipfs = new IPFS("/ip4/10.176.32.128/tcp/5001");
//    ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
  }

  @Test
  void testConnect() {
    System.out.println(ipfs.toString());
  }

  @Test
  void testCre() {
    JSONObject object = new JSONObject();
    object.set("test", "test");
    ArrayList<JSONObject> list = new ArrayList<>();
    list.add(object);
    ArrayList<JSONObject> newList = SerializeUtil.clone(list);
    newList.get(0).set("test", "equeal");
    System.out.println(object.getStr("test"));
  }

  @Test
  void testAr() {
    File file = new File("C:\\Users\\Guillered\\Desktop\\Y4\\web-hosting-blockchain\\dashboard\\src\\main\\resources\\META-INF");
    System.out.println(FileUtil.size(file, true));
  }
}