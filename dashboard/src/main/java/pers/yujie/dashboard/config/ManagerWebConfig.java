package pers.yujie.dashboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ManagerWebConfig implements WebMvcConfigurer {
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("").setViewName("index");
    registry.addViewController("/").setViewName("index");
    registry.addViewController("index").setViewName("index");
    registry.addViewController("/index.html").setViewName("index");
    registry.addViewController("website").setViewName("website");
  }
}
