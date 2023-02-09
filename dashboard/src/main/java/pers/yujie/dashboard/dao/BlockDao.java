package pers.yujie.dashboard.dao;

import java.math.BigInteger;
import java.util.List;
import pers.yujie.dashboard.entity.Block;

@Deprecated
public interface BlockDao {

  void initBlockDao();

  List<Block> selectAllBlock();

  List<Block> selectBlockByNode(BigInteger nodeId);
}
