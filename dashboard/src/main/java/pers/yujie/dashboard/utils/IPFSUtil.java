package pers.yujie.dashboard.utils;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pers.yujie.dashboard.entity.Block;

@Component
@Slf4j
public class IPFSUtil {

  @Getter
  @Setter
  private static IPFS ipfs;

  @Getter
  @Setter
  private static String address;
  private static List<Multihash> blockHashList;

//  private static IPFS getClient() {
//    if (ipfs == null) {
//      synchronized (IPFSUtil.class) {
//        if (ipfs == null) {
//          ipfs = new IPFS(Constants.IPFS_ADDRESS);
//        }
//      }
//    }
//    return ipfs;
//  }

  public static List<MerkleNode> uploadIPFS(File file) throws IOException {
    NamedStreamable.FileWrapper fileWrapper = new NamedStreamable.FileWrapper(file);
    return ipfs.add(fileWrapper);
  }

//  public List<Multihash> getPointerHashList(List<Multihash> blockHashList, String cid)
//      throws IOException {
//    List<Multihash> pointerHashList = new ArrayList<>(getBlockList(cid));
//    pointerHashList.removeAll(blockHashList);
//
//    return pointerHashList;
//  }
//
//  public List<Multihash> getBlockHashList(String cid) throws IOException {
//    blockHashList = new ArrayList<>();
//    blockHashList.add(Multihash.fromBase58(cid));
//    getRecursiveBlockList(Multihash.fromBase58(cid));
//    return blockHashList;
//  }

  public static BigInteger getBlockListSize(String cid) throws IOException {
    List<Block> blockList = getBlockList(cid);
    BigInteger result = BigInteger.ZERO;

    for (Block block : blockList) {
      result = result.add((BigInteger) ipfs.block.stat(Multihash.fromBase58("hash")).get("Size"));
    }
    return result;
  }

  public static BigInteger getBlockListSize(List<String> blockList) throws IOException {
    BigInteger result = BigInteger.ZERO;

    for (String hash : blockList) {
      result = result.add((BigInteger) ipfs.block.stat(Multihash.fromBase58(hash)).get("Size"));
    }
    return result;
  }

  public static List<Block> getBlockList(String cid) throws IOException {
    List<Multihash> refList = ipfs.refs(Multihash.fromBase58(cid), true);
    refList.add(Multihash.fromBase58(cid));

    List<Block> blockList = new ArrayList<>();
    for (Multihash hash : refList) {
      blockList.add(new Block(hash.toString(), (BigInteger) ipfs.block.stat(hash).get("Size")));
    }

    return blockList;
  }

//  private static void getRecursiveBlockList(Multihash cid) throws IOException {
//    List<MerkleNode> lsResponse = ipfs.ls(cid);
//    if (lsResponse.size() < 1) {
//      return;
//    }
//    for (MerkleNode node : lsResponse) {
//      List<Multihash> refList = getBlockList(node.hash.toString());
//      if (refList.size() < 1) {
//        blockHashList.add(node.hash);
//        continue;
//      }
//      getRecursiveBlockList(node.hash);
//    }

//  }

}
