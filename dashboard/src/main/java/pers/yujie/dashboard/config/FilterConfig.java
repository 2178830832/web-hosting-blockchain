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

//  @Bean
//  public FilterRegistrationBean<ContentSecurityPolicyFilter> contentSecurityPolicyFilter() {
//    FilterRegistrationBean<ContentSecurityPolicyFilter> registrationBean = new FilterRegistrationBean<>();
//    ContentSecurityPolicyFilter filter = new ContentSecurityPolicyFilter("frame-ancestors 'none'; form-action 'self'");
//    registrationBean.setFilter(filter);
//    registrationBean.setOrder(1);
//    return registrationBean;
//  }
}