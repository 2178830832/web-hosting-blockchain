package pers.yujie.dashboard.dao.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.SerializeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import pers.yujie.dashboard.dao.WebsiteDao;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.utils.Web3JUtil;

@Repository
@Slf4j
public class WebsiteDaoImpl implements WebsiteDao {

  private List<Website> websites = new ArrayList<>();

  @Override
  public List<Website> selectAllWebsite() {
    return websites;
  }

  @Override
  public void initWebsiteDao() {
    try {
      String websiteEncodedStr = (String) Web3JUtil.sendQuery("getWebsites",
          Collections.emptyList(),
          Collections.singletonList(new TypeReference<Utf8String>() {
          })).get(0).getValue();
      if (websiteEncodedStr == null || websiteEncodedStr.equals("")) {
        return;
      }
      websites = JSONUtil.toList(JSONUtil.parseArray(websiteEncodedStr), Website.class);
    } catch (ExecutionException | InterruptedException e) {
      log.error("Unable to retrieve website list from the database");
    }
  }


  @Override
  public boolean insertWebsite(JSONObject website) {
    List<Website> updatedWebsites = SerializeUtil.clone(websites);
    updatedWebsites = ListUtil.sortByProperty(updatedWebsites, "id");
    BigInteger id = updatedWebsites.get(updatedWebsites.size() - 1).getId().add(BigInteger.ONE);
    Website addWebsite = new Website(id);

    setUpHelper(addWebsite, website);
    if (!StrUtil.isEmptyOrUndefined(website.getStr("cid"))) {
      addWebsite.setSize(website.getBigInteger("cid"));
    }
    updatedWebsites.add(addWebsite);

    return commitChange(updatedWebsites);
  }

  private boolean commitChange(List<Website> updatedWebsites) {
    String websiteDecodedStr = JSONUtil.parseArray(updatedWebsites).toString();

    try {
      EthSendTransaction response = Web3JUtil.sendTransaction("setWebsites",
          Collections.singletonList(new Utf8String(websiteDecodedStr)));
      if (response.getError() == null) {
        log.info("Transaction succeeded: " + response.getResult());
        websites = updatedWebsites;
        return true;
      } else {
        log.error("Transaction encountered error: " + response.getError().getMessage());
        return false;
      }
    } catch (ExecutionException | InterruptedException e) {
      log.error("Unable to commit changes to the database");
      return false;
    }

  }

  private void setUpHelper(Website website, JSONObject websiteObj) {
    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("name"))) {
      website.setName(websiteObj.getStr("name"));
    }
    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("path"))) {
      website.setLocation(websiteObj.getStr("path"));
    }

    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("size"))) {
      website.setSize(websiteObj.getBigInteger("size"));
    }
  }

  @Override
  public boolean updateWebsite(JSONObject website) {
    Website upWebsite = websites.get(website.getInt("id"));
    if (!StrUtil.isEmptyOrUndefined(website.getStr("name"))) {
      upWebsite.setName(website.getStr("name"));
    }
    setUpHelper(upWebsite, website);
    upWebsite.setUpdateTime(DateTime.now().toString());
    List<Website> updatedWebsites = SerializeUtil.clone(websites);
    updatedWebsites.set(upWebsite.getId().intValue(), upWebsite);
    return commitChange(updatedWebsites);
  }

  @Override
  public boolean deleteWebsite(BigInteger id) {
    List<Website> updatedWebsites = SerializeUtil.clone(websites);

    for (int i = 0; i < updatedWebsites.size(); i++) {
      if (updatedWebsites.get(i).getId().equals(id)) {
        updatedWebsites.remove(i);
        // do something
        break;
      }
    }
    for (Website updatedWebsite : updatedWebsites) {
      if (updatedWebsite.getId().compareTo(id) > 0) {
        updatedWebsite.setId(updatedWebsite.getId().subtract(BigInteger.ONE));
      }
    }

    return commitChange(updatedWebsites);
  }
}
