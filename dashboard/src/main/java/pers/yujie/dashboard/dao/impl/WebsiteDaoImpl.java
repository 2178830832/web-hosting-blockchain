package pers.yujie.dashboard.dao.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import pers.yujie.dashboard.dao.WebsiteDao;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.utils.EncryptUtil;
import pers.yujie.dashboard.utils.Web3JUtil;

/**
 * This is the Data Access Object (DAO) class for {@link Website}.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see BaseDaoImpl
 * @since 29/12/2022
 */
@Repository
@Slf4j
public class WebsiteDaoImpl extends BaseDaoImpl implements WebsiteDao {

  private List<Website> websites = new ArrayList<>();

  /**
   * The deleted websites are not dropped.
   */
  private List<Website> delWebsites = new ArrayList<>();

  /**
   * Select all websites as a list.
   *
   * @return a {@link List} of {@link JSONObject} representing the websites
   */
  @Override
  public List<JSONObject> selectAllWebsite() {
    List<JSONObject> websiteList = new ArrayList<>();
    for (Website website : websites) {
      websiteList.add(JSONUtil.parseObj(website));
    }
    return websiteList;
  }

  /**
   * Select a website given its ID.
   *
   * @param id {@link BigInteger} of the specified website ID
   * @return a {@link JSONObject} of the selected website
   */
  @Override
  public JSONObject selectWebsiteById(BigInteger id) {
    return JSONUtil.parseObj(websites.get(id.intValue()));
  }

  /**
   * Initialise this class by reading from the Ganache and decrypting with {@link EncryptUtil}.
   */
  @Override
  public void initWebsiteDao() {
    try {
      String websiteEncodedStr = (String) Web3JUtil.sendQuery("getWebsites",
          Collections.emptyList(),
          Collections.singletonList(new TypeReference<Utf8String>() {
          })).get(0).getValue();
      if (!StrUtil.isEmptyOrUndefined(websiteEncodedStr)) {
        websiteEncodedStr = EncryptUtil.decryptAES(websiteEncodedStr);
        List<Website> websiteList = JSONUtil.toList(
            JSONUtil.parseArray(websiteEncodedStr), Website.class);
        websiteList = ListUtil.sortByProperty(websiteList, "id");
        for (Website website : websiteList) {
          if (!website.getId().equals(new BigInteger("-1"))) {
            delWebsites = websiteList.subList(0, websiteList.indexOf(website));
            websites = websiteList.subList(websiteList.indexOf(website), websiteList.size());
            break;
          }
        }
      }
    } catch (IndexOutOfBoundsException | ExecutionException | InterruptedException e) {
      log.error("Unable to retrieve website list from the data source");
    }
  }

  /**
   * Insert a new website.
   *
   * @param website {@link JSONObject} of the website to be uploaded
   * @return true if succeeded, false otherwise
   */
  @Override
  public boolean insertWebsite(JSONObject website) {
    List<Website> updatedWebsites = new ArrayList<>(websites);
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
    updatedWebsites.addAll(delWebsites);

    return commitChange(updatedWebsites);
  }

  /**
   * Encrypt the websites with symmetric algorithm and commit it to the data source.
   *
   * @param updatedWebsites a {@link List} of {@link Website}.
   * @return true if succeeded, false otherwise
   */
  private boolean commitChange(List<Website> updatedWebsites) {
    String websiteDecodedStr = JSONUtil.parseArray(updatedWebsites).toString();
    websiteDecodedStr = EncryptUtil.encryptAES(websiteDecodedStr);

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
      log.error("Unable to commit changes to the data source");
      return false;
    }
  }

  /**
   * Update a specific website.
   *
   * @param website {@link JSONObject} representing the website to be updated
   * @return true if succeeded, false otherwise
   */
  @Override
  public boolean updateWebsite(JSONObject website) {
    List<Website> updatedWebsites = new ArrayList<>(websites);
    for (Website upWebsite : websites) {
      if (upWebsite.getId().equals(website.getBigInteger("id"))) {
        int index = updatedWebsites.indexOf(upWebsite);
        setUpHelper(upWebsite, website);
        upWebsite.setUpdateTime(DateTime.now().toString());
        updatedWebsites.set(index, upWebsite);
        updatedWebsites.addAll(delWebsites);
        return commitChange(updatedWebsites);
      }
    }
    return false;
  }

  /**
   * Delete a website.
   *
   * @param id {@link BigInteger} of the specified website ID
   * @return true if succeeded, false otherwise
   */
  @Override
  public boolean deleteWebsite(BigInteger id) {
    List<Website> updatedWebsites = new ArrayList<>(websites);
    List<Website> updatedDelWebsites = new ArrayList<>(delWebsites);

    for (Website website : updatedWebsites) {
      if (website.getId().equals(id)) {
        website.setStatus("deleted");
        updatedDelWebsites.add(website);
        updatedWebsites.remove(website);
        for (Website websiteIter : updatedWebsites) {
          if (websiteIter.getId().compareTo(id) > 0) {
            websiteIter.setId(websiteIter.getId().subtract(BigInteger.ONE));
          }
        }
        updatedWebsites.addAll(updatedDelWebsites);

        return commitChange(updatedWebsites);
      }
    }
    return false;
  }
}
