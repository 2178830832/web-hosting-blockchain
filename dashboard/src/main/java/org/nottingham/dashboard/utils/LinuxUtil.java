package org.nottingham.dashboard.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LinuxUtil {

  @Value("${linux.ip}")
  private String linuxIp;

  @Value("${linux.userName}")
  private String linuxUserName;

  @Value("${linux.userPassword}")
  private String linuxUserPassword;

  public String executeCmd(String cmd) throws IOException {
    SSHClient ssh = new SSHClient();
    ssh.loadKnownHosts();
    ssh.addHostKeyVerifier(new PromiscuousVerifier());
    ssh.connect(linuxIp);
    Session session = null;
    try {
      ssh.authPassword(linuxUserName, linuxUserPassword);
      session = ssh.startSession();
      return IOUtils.readFully(session.exec(cmd).getInputStream()).toString();
//      cmd.join(5, TimeUnit.SECONDS);
    } finally {
      try {
        if (session != null) {
          session.close();
        }
      } catch (IOException e) {
        // Do Nothing
      }

      ssh.disconnect();
    }

  }
}