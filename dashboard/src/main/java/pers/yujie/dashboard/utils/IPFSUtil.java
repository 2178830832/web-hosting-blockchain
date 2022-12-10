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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pers.yujie.dashboard.common.Constants;

@Component
public class IPFSUtil {

  @Value("${local.ipfs.port}")
  private String ipfs_port;

  private volatile static IPFS ipfs;
  private static List<Multihash> blockHashList;

  @PostConstruct
  private void initIPFS() {
//    String ipfsPort = "/ip4/10.176.32.128/tcp/5001";
//    ipfs = new IPFS(ipfsPort);
  }

  private static IPFS getClient() {
    if (ipfs == null) {
      synchronized (IPFSUtil.class) {
        if (ipfs == null) {
          ipfs = new IPFS(Constants.IPFS_PORT);
        }
      }
    }
    return ipfs;
  }

  public List<MerkleNode> uploadIPFS(File file) throws IOException {

    NamedStreamable.FileWrapper fileWrapper = new NamedStreamable.FileWrapper(file);
    return getClient().add(fileWrapper);

  }

  public List<Multihash> getPointerHashList(List<Multihash> blockHashList, String cid)
      throws IOException {
    List<Multihash> pointerHashList = new ArrayList<>(getRefList(cid));
    pointerHashList.removeAll(blockHashList);

    return pointerHashList;
  }

  public List<Multihash> getBlockHashList(String cid) throws IOException {
    blockHashList = new ArrayList<>();
    blockHashList.add(Multihash.fromBase58(cid));
    getRecursiveBlockList(Multihash.fromBase58(cid));
    return blockHashList;
  }

  private static List<Multihash> getRefList(String cid) throws IOException {
    List<Multihash> refList = getClient().refs(Multihash.fromBase58(cid), true);
    refList.add(Multihash.fromBase58(cid));
    return refList;
  }

  private static void getRecursiveBlockList(Multihash cid) throws IOException {
    List<MerkleNode> lsResponse = getClient().ls(cid);
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