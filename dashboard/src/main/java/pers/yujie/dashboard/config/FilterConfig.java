package pers.yujie.dashboard.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Utilises Spring Boot aspect-oriented programming (AOP) to intercept and sign responses. <br> The
 * actual processing occurs in {@link RequestManagerConfig}.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 19/01/2023
 */
@Configuration
public class FilterConfig {

  @Bean
  public FilterRegistrationBean<RequestManagerConfig> encryptionFilter() {
    FilterRegistrationBean<RequestManagerConfig> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new RequestManagerConfig());
    // filter all URL requests
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }
}