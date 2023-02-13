package pers.yujie.dashboard.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
  @Bean
  public FilterRegistrationBean<RequestManagerConfig> encryptionFilter() {
    FilterRegistrationBean<RequestManagerConfig> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new RequestManagerConfig());
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }
}