package org.nottingham.dashboard.service.impl;

import io.ipfs.api.IPFS;
import java.io.File;
import java.io.IOException;
import javax.annotation.Resource;
import org.nottingham.dashboard.service.WebsiteService;
import org.nottingham.dashboard.utils.IPFSUtil;
import org.nottingham.dashboard.utils.LinuxUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebsiteServiceImpl implements WebsiteService {

  @Resource
  private IPFSUtil ipfsUtil;

  @Resource
  private LinuxUtil linuxUtil;

  //    private final IPFS ipfs = new IPFS("/ip4/10.176.32.128/tcp/5001");
//  private IPFS ipfs = new IPFS(@Value("${ipfs.port}"));

  @Override
  public void uploadWebsite(String folderPath) {
    try {
      String cid = IPFSUtil.uploadIPFS(ipfsUtil.ipfs, folderPath);
      System.out.println(cid);
      String response = linuxUtil.executeCmd("ipfs get -o " + ipfsUtil.ipfsLinuxPath + " " + cid);
      System.out.println(response);
      response = linuxUtil
          .executeCmd("ipfs-cluster-ctl add " + ipfsUtil.ipfsLinuxPath + File.separator + cid);
      System.out.println(response);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
