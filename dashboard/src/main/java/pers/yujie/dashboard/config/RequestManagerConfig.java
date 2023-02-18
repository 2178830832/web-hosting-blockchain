package pers.yujie.dashboard.config;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.util.concurrent.RateLimiter;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pers.yujie.dashboard.utils.EncryptUtil;

@Aspect
@Component
@SuppressWarnings("UnstableApiUsage")
public class RequestManagerConfig implements Filter {

  private static final String SIGNATURE = "WEB_CHAIN";

  private final RateLimiter rateLimiter = RateLimiter.create(10.0); // allows 10 requests per second

  @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) || "
      + "@annotation(org.springframework.web.bind.annotation.PostMapping)")
  private void allGetAndPostMappingMethods() {
  }


  @Around("allGetAndPostMappingMethods()")
  private Object applyRateLimiting(ProceedingJoinPoint joinPoint) throws Throwable {
    if (!rateLimiter.tryAcquire()) {
      return new ResponseEntity<>("Too many requests, please try again later.",
          HttpStatus.TOO_MANY_REQUESTS);
    }

    return joinPoint.proceed();
  }

  @Around("allGetAndPostMappingMethods() && @annotation(pers.yujie.dashboard.common.Encrypted)")
  public Object applyEncryptionCheck(ProceedingJoinPoint joinPoint) throws Throwable {

    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes();
    if (requestAttributes == null) {
      return new ResponseEntity<>("Unknown request attributes",
          HttpStatus.UNAUTHORIZED);
    }

    HttpServletRequest request = requestAttributes.getRequest();
    String encryptedStr = request.getHeader("Authorization");
    if (encryptedStr == null) {
      return new ResponseEntity<>("Encrypted data not found in request header",
          HttpStatus.UNAUTHORIZED);
    }

    try {
      // Decrypt the encrypted URL using the RSA public key
      String decryptedStr = EncryptUtil.decryptPrivateRSA(encryptedStr);
      JSONObject authObj = JSONUtil.parseObj(decryptedStr);
      DateUtil.parse(authObj.getStr("time"));

      // Compare the decrypted URL with the actual requested URL
      String requestedUrl = request.getRequestURI();
      if (!authObj.getStr("url").equals(requestedUrl)) {
        return new ResponseEntity<>("Encrypted data not found in request header",
            HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception e) {
      return new ResponseEntity<>("Encrypted data not found in request header",
          HttpStatus.UNAUTHORIZED);
    }

    return joinPoint.proceed();
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws ServletException, IOException {
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

    String encryptedSign = EncryptUtil.signSHA256withRSA(SIGNATURE);
    httpResponse.setHeader("Authorization", encryptedSign);
    httpResponse.setHeader("Content-Security-Policy",
        "default-src 'self' 'unsafe-inline';frame-ancestors 'none'; form-action 'self'");
    httpResponse.setHeader("X-Frame-Options", "deny");
    httpResponse.setHeader("X-Content-Type-Options", "nosniff");
    filterChain.doFilter(servletRequest, servletResponse);
  }
}
