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
      if (!StrUtil.isEmptyOrUndefined(websiteEncodedStr)) {
        websites = JSONUtil.toList(JSONUtil.parseArray(websiteEncodedStr), Website.class);
      }
    } catch (ExecutionException | InterruptedException e) {
      log.error("Unable to retrieve website list from the database");
    }
  }


  @Override
  public boolean insertWebsite(JSONObject website) {
    List<Website> updatedWebsites = SerializeUtil.clone(websites);
    updatedWebsites = ListUtil.sortByProperty(updatedWebsites, "id");

    BigInteger id;
    if (updatedWebsites.size() < 1) {
      id = BigInteger.ZERO;
    } else {
      id = updatedWebsites.get(updatedWebsites.size() - 1).getId().add(BigInteger.ONE);
    }
    Website addWebsite = new Website(id);

    setUpHelper(addWebsite, website);
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
    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("location"))) {
      website.setLocation(websiteObj.getStr("location"));
    }

    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("size"))) {
      website.setSize(websiteObj.getBigInteger("size"));
    }

    if (!StrUtil.isEmptyOrUndefined(websiteObj.getStr("cid"))) {
      website.setCid(websiteObj.getStr("cid"));
    }
  }

  @Override
  public boolean updateWebsite(JSONObject website) {
    List<Website> updatedWebsites = SerializeUtil.clone(websites);
    for (Website upWebsite : websites) {
      if (upWebsite.getId().equals(website.getBigInteger("id"))) {
        setUpHelper(upWebsite, website);
        upWebsite.setUpdateTime(DateTime.now().toString());
        updatedWebsites.set(updatedWebsites.indexOf(upWebsite), upWebsite);
        return commitChange(updatedWebsites);
      }
    }
    return false;
  }

  @Override
  public boolean deleteWebsite(BigInteger id) {
    List<Website> updatedWebsites = SerializeUtil.clone(websites);

    updatedWebsites.removeIf(website -> website.getId().equals(id));
    for (Website website : updatedWebsites) {
      if (website.getId().compareTo(id) > 0) {
        website.setId(website.getId().subtract(BigInteger.ONE));
      }
    }

    return commitChange(updatedWebsites);
  }
}
