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

/**
 * This is a utility class responsible for controlling IPFS API.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 21/10/2022
 */
@Component
@Slf4j
public class IPFSUtil {

  @Getter
  @Setter
  private static IPFS ipfs;

  @Getter
  @Setter
  private static String address;

  /**
   * Upload a file or directory to IPFS.
   *
   * @param file {@link File} object
   * @return {@link List} or the uploaded CIDs
   * @throws IOException thrown by {@link IPFS#add(NamedStreamable)}
   */
  public static List<MerkleNode> uploadIPFS(File file) throws IOException {
    NamedStreamable.FileWrapper fileWrapper = new NamedStreamable.FileWrapper(file);
    return ipfs.add(fileWrapper);
  }

  /**
   * Given a Merkle node, get the block list under this node
   *
   * @param cid the Merkle node
   * @return {@link List} of {@link Block}
   * @throws IOException thrown by {@link IPFS#stats}
   */
  public static List<Block> getBlockList(String cid) throws IOException {
    List<Multihash> refList = ipfs.refs(Multihash.fromBase58(cid), true);
    refList.add(Multihash.fromBase58(cid));

    List<Block> blockList = new ArrayList<>();
    for (Multihash hash : refList) {
      blockList.add(new Block(hash.toString(), (BigInteger) ipfs.block.stat(hash).get("Size")));
    }

    return blockList;
  }


}
