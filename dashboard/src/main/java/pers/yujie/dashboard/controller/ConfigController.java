package pers.yujie.dashboard.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pers.yujie.dashboard.common.Constants;
import pers.yujie.dashboard.common.Encrypted;
import pers.yujie.dashboard.config.RequestManagerConfig;
import pers.yujie.dashboard.service.ConfigService;
import pers.yujie.dashboard.utils.EncryptUtil;
import pers.yujie.dashboard.utils.Web3JUtil;

/**
 * Contains the {@link Controller} related to the config (index) page. The business logic is in
 * {@link ConfigService}.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 02/02/2023
 */
@Controller
public class ConfigController {

  @Resource
  private ConfigService configService;

  /**
   * Get the IPFS, Docker and Ethereum status.
   *
   * @return {@link ResponseEntity} of {@link HttpStatus#OK}.
   */
  @Encrypted
  @GetMapping("/config")
  public ResponseEntity<String> getConfigStatus() {
    JSONObject status = new JSONObject();
    status.set("ipfs", configService.getIPFSStatus());
    status.set("docker", configService.getDockerStatus());
    status.set("web3", configService.getWeb3Status());
    return new ResponseEntity<>(status.toString(), HttpStatus.OK);
  }

  /**
   * Set custom configuration for IPFS.
   *
   * @param request a {@link JSONObject} containing address.
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} otherwise.
   */
  @Encrypted
  @PostMapping("/config/ipfs")
  public ResponseEntity<String> setCustomIPFS(@RequestBody JSONObject request) {
    String message = configService.connectIPFS(request.getStr("address"));

    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

  }

  /**
   * Set custom configuration for Ethereum.
   *
   * @param request a {@link JSONObject} containing address, account and contract.
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} otherwise.
   */
  @Encrypted
  @PostMapping("/config/web3")
  public ResponseEntity<String> setCustomWeb3(@RequestBody JSONObject request) {
    String address = (request.getStr("address") == null) ?
        Web3JUtil.getAddress() : request.getStr("address");
    String account = (request.getStr("account") == null) ?
        Web3JUtil.getAccount() : request.getStr("account");
    String contract = (request.getStr("contract") == null) ?
        Web3JUtil.getContract() : request.getStr("contract");

    String message = configService.connectWeb3(address, account, contract);
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Set custom configuration for Docker.
   *
   * @param request a {@link JSONObject} containing address.
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} otherwise.
   */
  @Encrypted
  @PostMapping("/config/docker")
  public ResponseEntity<String> setCustomDocker(@RequestBody JSONObject request) {
    String message = configService.connectDocker(request.getStr("address"));
    if (message.equals("")) {
      return new ResponseEntity<>("success", HttpStatus.OK);
    } else {
      return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
  }

}
