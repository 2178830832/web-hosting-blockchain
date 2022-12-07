package pers.yujie.dashboard.utils;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pers.yujie.dashboard.entity.Node;

@Component
public class IPFSUtil {

  @Value("${local.ipfs.port}")
  private String ipfsPort;

  private static IPFS ipfs;
  private static List<Multihash> blockHashList;

  @PostConstruct
  private void initIPFS() {
//    ipfs = new IPFS(ipfsPort);
  }

  public List<MerkleNode> uploadIPFS(File file) throws IOException {

    NamedStreamable.FileWrapper fileWrapper = new NamedStreamable.FileWrapper(file);
    return ipfs.add(fileWrapper);

  }

  public List<Multihash> getMainHashList(List<Multihash> blockHashList, String cid)
      throws IOException {
    List<Multihash> mainHashList = new ArrayList<>(getRefList(cid));

    mainHashList.removeAll(blockHashList);

    return mainHashList;
  }

  public List<Multihash> getBlockList(String cid) throws IOException {
    blockHashList = new ArrayList<>();
    blockHashList.add(Multihash.fromBase58(cid));
    getRecursiveBlockList(Multihash.fromBase58(cid));
    return blockHashList;
  }

  public static List<Multihash> getRefList(String cid) throws IOException {
    List<Multihash> refList = ipfs.refs(Multihash.fromBase58(cid), true);
    refList.add(Multihash.fromBase58(cid));
    return refList;
  }

  private static void getRecursiveBlockList(Multihash cid) throws IOException {
    List<MerkleNode> lsResponse = ipfs.ls(cid);
    if (lsResponse.size() < 1) {
      return;
    }
    for (MerkleNode node : lsResponse) {
      List<Multihash> refList = getRefList(node.hash.toString());
      if (refList.size() < 1) {
        blockHashList.add(node.hash);
        continue;
      }
      getRecursiveBlockList(node.hash);
    }

  }

}
