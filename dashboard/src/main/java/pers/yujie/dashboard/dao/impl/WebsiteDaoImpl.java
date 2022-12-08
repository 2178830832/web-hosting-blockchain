package pers.yujie.dashboard.dao.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Repository;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import pers.yujie.dashboard.dao.WebsiteDao;
import pers.yujie.dashboard.entity.Website;
import pers.yujie.dashboard.utils.AppUtil;
import pers.yujie.dashboard.utils.Web3JUtil;

@Repository
@Slf4j
public class WebsiteDaoImpl implements WebsiteDao {

  private List<Website> websites;

  @Resource
  private ApplicationContext ctx;

  @PostConstruct
  @SuppressWarnings({"unchecked", "rawtypes"})
  private void initWebsiteDao() {
    try {
      websites = (List) Web3JUtil.sendQuery("selectAllWebsites",
          Collections.emptyList(),
          Collections.singletonList(new TypeReference<DynamicArray<Website>>() {
          })).get(0).getValue();
    } catch (ExecutionException | InterruptedException e) {
      log.info("Unable to request local ganache server");
      AppUtil.exitApplication(ctx, 1);
    }
  }

  @Override
  public boolean insertWebsite(Website website) {
    try {
      EthSendTransaction response = Web3JUtil.sendTransaction("insertWebsite",
          Collections.singletonList(website));
      if (response.getError() == null) {
        log.info("Transaction receipt: " + response.getResult());
        websites.add(website);
        return true;
      } else {
        log.warn("Transaction encountered error: " + response.getError().toString());
        return false;
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
      return false;
    }
  }
}
