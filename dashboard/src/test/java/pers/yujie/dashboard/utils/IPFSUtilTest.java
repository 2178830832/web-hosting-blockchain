package pers.yujie.dashboard.utils;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IPFSUtilTest {

  static IPFS ipfs;
  List<MerkleNode> blockList;
  List<Multihash> refList;

  @BeforeAll
  static void setUp() {
    ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
  }

  @Test
  void testConnect() throws IOException {
    System.out.println(ipfs.toString());
  }

  @Test
  void uploadIPFS() throws IOException {
//    File file = new File("D:\\Y4\\FYP\\info.txt");
//    NamedStreamable.FileWrapper fileWrapper = new NamedStreamable.FileWrapper(file);
//    List<MerkleNode> addResult = ipfs.add(fileWrapper);
//    System.out.println(addResult);
//    System.out.println(addResult.get(addResult.size() - 1).hash.toString());
    System.out.println(ipfs.block.stat(Multihash.fromBase58("Qmee9j9mf4LPepownEpDYdo8gwRMbx5LmSoXLKDBqTd64j")).get("Size"));

  }

  @Test
  void deleteIPFS() throws IOException {
    blockList = new ArrayList<>();
    Multihash cid = Multihash.fromBase58("QmevHYpfvatpQ7rYAGb5mCxHZDsCELk1seBUr3Hfu97H14");
    getBlockList(cid);
    List<Multihash> allList = ipfs.refs(cid, true);
    ipfs.pin.rm(cid);
    for (MerkleNode node : blockList) {
      ipfs.block.rm(node.hash);
      allList.remove(node.hash);
    }
    ipfs.repo.gc();
//    for (Multihash hash : allList) {
//      ipfs.pin.add(hash);
//    }

  }

  @Test
  void getBlock() throws IOException {
    Multihash cid = Multihash.fromBase58("QmevHYpfvatpQ7rYAGb5mCxHZDsCELk1seBUr3Hfu97H14");
    blockList = new ArrayList<>();
    getBlockList(cid);
    System.out.println(blockList);
  }

  private void getBlockList(Multihash cid) throws IOException {
    List<MerkleNode> addResult = ipfs.ls(cid);
    if (addResult.size() < 1) {
      return;
    }
    for (MerkleNode node : addResult) {
      refList = ipfs.refs(node.hash, true);
      if (refList.size() < 1) {
        blockList.add(node);
        continue;
      }
      getBlockList(node.hash);
    }

  }
}