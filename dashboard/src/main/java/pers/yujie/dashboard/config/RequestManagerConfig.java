package pers.yujie.dashboard.config;

import cn.hutool.core.date.DateUtil;
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

/**
 * Utilises Spring Boot aspect-oriented programming (AOP) to validate requests and sign responses.
 * The requests are intercepted in {@link FilterConfig}. <br> For some unknown reasons, the {@link
 * RateLimiter} API is still marked as unstable, and this class has disabled this warning.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see Filter
 * @since 19/01/2023
 */
@Aspect
@Component
@SuppressWarnings("UnstableApiUsage")
public class RequestManagerConfig implements Filter {

  /**
   * Digital signature for verification of responses
   */
  private static final String SIGNATURE = "WEB_CHAIN";

  /**
   * Allows 10 requests per second to prevent DDoS or DoS attacks
   */
  private final RateLimiter rateLimiter = RateLimiter.create(10.0);

  /**
   * Interception of all annotated {@link org.springframework.web.bind.annotation.GetMapping} and
   * {@link org.springframework.web.bind.annotation.PostMapping} requests.
   */
  @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping) || "
      + "@annotation(org.springframework.web.bind.annotation.PostMapping)")
  private void allGetAndPostMappingMethods() {
  }


  /**
   * Apply rate limit to the pre-defined {@link Pointcut}. It will reject the requests, provided
   * that they have exceeded the limitation.
   *
   * @param joinPoint AOP {@link ProceedingJoinPoint}
   * @return proceed the original requests to corresponding controllers.
   * @throws Throwable exception raised by the {@link ProceedingJoinPoint#proceed()}.
   */
  @Around("allGetAndPostMappingMethods()")
  private Object applyRateLimiting(ProceedingJoinPoint joinPoint) throws Throwable {
    if (!rateLimiter.tryAcquire()) {
      return new ResponseEntity<>("Too many requests, please try again later.",
          HttpStatus.TOO_MANY_REQUESTS);
    }
    return joinPoint.proceed();
  }

  /**
   * Check whether the requests come from trusted sources. It will reject the requests, provided
   * that they fail to pass the RSA encryption. The encryption data should be contained in the
   * 'Authorization' header and encrypted by the given public key.
   *
   * @param joinPoint AOP {@link ProceedingJoinPoint}
   * @return proceed the original requests to corresponding controllers.
   * @throws Throwable exception raised by the {@link ProceedingJoinPoint#proceed()}.
   * @see EncryptUtil
   */
  @Around("allGetAndPostMappingMethods() && @annotation(pers.yujie.dashboard.common.Encrypted)")
  public Object applyEncryptionCheck(ProceedingJoinPoint joinPoint) throws Throwable {

    // retrieve the request attributes
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes();
    if (requestAttributes == null) {
      return new ResponseEntity<>("Unknown request attributes",
          HttpStatus.UNAUTHORIZED);
    }

    // get the encrypted string from the request header
    HttpServletRequest request = requestAttributes.getRequest();
    String encryptedStr = request.getHeader("Authorization");
    if (encryptedStr == null) {
      return new ResponseEntity<>("Encrypted data not found in request header",
          HttpStatus.UNAUTHORIZED);
    }

    try {
      // decrypt the encrypted string using the RSA public key
      String decryptedStr = EncryptUtil.decryptPrivateRSA(encryptedStr);

      // check the correctness of time and URL
      JSONObject authObj = JSONUtil.parseObj(decryptedStr);
      DateUtil.parse(authObj.getStr("time"));
      String requestedUrl = request.getRequestURI();
      if (!authObj.getStr("url").equals(requestedUrl)) {
        return new ResponseEntity<>("Encrypted data not found in request header",
            HttpStatus.UNAUTHORIZED);
      }
    } catch (Exception e) {
      // the string cannot be deprecated or is not in the correct format
      return new ResponseEntity<>("Encrypted data not found in request header",
          HttpStatus.UNAUTHORIZED);
    }

    return joinPoint.proceed();
  }

  /**
   * Sign the responses and set Content Security Policy (CSP) headers for modern browsers.
   *
   * @param servletRequest  http request
   * @param servletResponse http response
   * @param filterChain     filter all controllers
   * @throws ServletException exception raised by {@link FilterChain#doFilter(ServletRequest,
   *                          ServletResponse)}
   * @throws IOException      exception raised by {@link FilterChain#doFilter(ServletRequest,
   *                          ServletResponse)}
   * @see FilterConfig
   */
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws ServletException, IOException {
    HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

    // sign the responses using SHA256
    String encryptedSign = EncryptUtil.signSHA256withRSA(SIGNATURE);
    httpResponse.setHeader("Authorization", encryptedSign);
    httpResponse.setHeader("Content-Security-Policy", "default-src 'self';"
        + "frame-ancestors 'none'; form-action 'self';"
        + "style-src 'self' 'unsafe-inline';"
        + "img-src 'self' data:");
    httpResponse.setHeader("X-Frame-Options", "deny");
    httpResponse.setHeader("X-Content-Type-Options", "nosniff");
    filterChain.doFilter(servletRequest, servletResponse);
  }
}
