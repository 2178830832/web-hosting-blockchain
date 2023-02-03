package pers.yujie.dashboard.dao.impl;

import cn.hutool.core.util.StrUtil;
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
import pers.yujie.dashboard.dao.BlockDao;
import pers.yujie.dashboard.entity.Block;
import pers.yujie.dashboard.utils.EncryptUtil;
import pers.yujie.dashboard.utils.Web3JUtil;

@Repository
@Slf4j
public class BlockDaoImpl implements BlockDao {

  private List<Block> blocks = new ArrayList<>();

  @Override
  public void initBlockDao() {
    try {
      String blockEncodedStr = (String) Web3JUtil.sendQuery("getBlocks",
          Collections.emptyList(),
          Collections.singletonList(new TypeReference<Utf8String>() {
          })).get(0).getValue();
      if (!StrUtil.isEmptyOrUndefined(blockEncodedStr)) {
        blockEncodedStr = EncryptUtil.aesDecrypt(blockEncodedStr);
        blocks = JSONUtil.toList(JSONUtil.parseArray(blockEncodedStr), Block.class);
      }
    } catch (ExecutionException | InterruptedException e) {
      log.error("Unable to retrieve block list from the database");
    }
  }

  @Override
  public List<Block> selectAllBlock() {
    return blocks;
  }

  @Override
  public List<Block> selectBlockByNode(BigInteger nodeId) {
    List<Block> blockList = new ArrayList<>();
    for (Block block : blocks) {
      if (block.getNodeId().equals(nodeId)) {
        blockList.add(block);
      }
    }
    return blockList;
  }

  private boolean commitChange(List<Block> updatedBlocks) {
    String blockDecodedStr = JSONUtil.parseArray(updatedBlocks).toString();
    blockDecodedStr = EncryptUtil.aesEncrypt(blockDecodedStr);

    try {
      EthSendTransaction response = Web3JUtil.sendTransaction("setBlocks",
          Collections.singletonList(new Utf8String(blockDecodedStr)));
      if (response.getError() == null) {
        log.info("Transaction succeeded: " + response.getResult());
        blocks = updatedBlocks;
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
}
