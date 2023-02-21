package pers.yujie.dashboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Resolves the CORS issue when interacting between frontend and backend. Alternatively, the
 * security checks of Chrome or Firefox may be turned off, or one could handle the CORS in the
 * frontend.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see WebMvcConfigurer
 * @since 09/11/2022
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("*")
        // allow common Restful methods to bypass the CORS registry
        .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
        .allowCredentials(true)
        .maxAge(3600)
        .allowedHeaders("*");
  }
}
