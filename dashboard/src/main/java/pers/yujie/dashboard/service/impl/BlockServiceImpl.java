package pers.yujie.dashboard.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import pers.yujie.dashboard.dao.BlockDao;
import pers.yujie.dashboard.entity.Block;
import pers.yujie.dashboard.service.BlockService;

public class BlockServiceImpl implements BlockService {

  @Resource
  private BlockDao blockDao;

  @Override
  public void redistributeNodeSize(BigInteger nodeId, BigInteger size) {
    List<Block> blockList = blockDao.selectBlockByNode(nodeId);
    List<Block> reBlockList = new ArrayList<>();

    for (Block block : blockList) {
      if (size.compareTo(BigInteger.ONE) < 0) {
        break;
      }
      size = size.subtract(block.getSize());
      reBlockList.add(block);
    }

    // then distribute the blocks
  }
}
