package org.nottingham.dashboard.utils;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IPFSUtil {

  @Value("${ipfs.linux.path}")
  public String ipfsLinuxPath;

  public IPFS ipfs;

  public IPFSUtil(@Value("${ipfs.port}") String ipfsPort) {
    this.ipfs = new IPFS(ipfsPort);
  }

  public static String uploadIPFS(IPFS ipfs, String fileName) throws IOException {
    NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(fileName));
    MerkleNode addResult = ipfs.add(file).get(0);
    return addResult.hash.toString();
  }

}
