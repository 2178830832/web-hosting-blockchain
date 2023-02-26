package pers.yujie.dashboard.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.multihash.Multihash;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import pers.yujie.dashboard.entity.Block;

class IPFSUtilTest {

  static final String IPFS_ADDRESS = "/ip4/124.223.10.94/tcp/5001";

  @BeforeAll
  static void setUp() {
    IPFSUtil.setIpfs(new IPFS(IPFS_ADDRESS));
    IPFSUtil.setAddress(IPFS_ADDRESS);
  }

  @Test
  void testConnectIPFS() {
    assertDoesNotThrow(() -> IPFSUtil.getIpfs().id());
  }

  @Test
  void testUploadIPFSAndBlock() {
    assertDoesNotThrow(() -> {
      // upload a file to IPFS
      List<MerkleNode> merkleList = IPFSUtil
          .uploadIPFS(new ClassPathResource("application.properties").getFile());
      Multihash hash = merkleList.get(0).hash;
      assertNotNull(hash);

      List<Block> blockList = IPFSUtil.getBlockList(hash.toString());
      assertEquals(blockList.get(0).getSize().toString(), merkleList.get(0).largeSize.orElse(null));

      // unpin the file and recycle the repo
      IPFSUtil.getIpfs().pin.rm(hash);
      IPFSUtil.getIpfs().repo.gc();
    });

  }
}