package pers.yujie.dashboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Register Thymeleaf view with corresponding html files. This way removes all trivial controllers
 * that set model views.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see WebMvcConfigurer
 * @since 07/12/2022
 */
@Configuration
public class WebManagerConfig implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("").setViewName("index");
    registry.addViewController("/").setViewName("index");
    registry.addViewController("/index").setViewName("index");
    registry.addViewController("/index.html").setViewName("index");
    registry.addViewController("website").setViewName("website");
    registry.addViewController("cluster").setViewName("cluster");
    registry.addViewController("node").setViewName("node");
    registry.addViewController("test").setViewName("test");
  }
}
