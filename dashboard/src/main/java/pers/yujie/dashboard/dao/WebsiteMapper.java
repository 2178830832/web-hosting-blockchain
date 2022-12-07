package pers.yujie.dashboard.dao;

import java.math.BigInteger;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import pers.yujie.dashboard.entity.Website;

@Mapper
public interface WebsiteMapper {

  int insertWebsite(Website website);

  int deleteByWebsiteId(BigInteger website_id);

  int updateWebsite(Website website);

  Website selectByWebsiteId(BigInteger website_id);

  List<Website> selectAllWebsite();

}
