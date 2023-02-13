package pers.yujie.dashboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebManagerConfig implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("website").setViewName("website");
    registry.addViewController("cluster").setViewName("cluster");
    registry.addViewController("node").setViewName("node");
    registry.addViewController("test").setViewName("test");
  }
}
