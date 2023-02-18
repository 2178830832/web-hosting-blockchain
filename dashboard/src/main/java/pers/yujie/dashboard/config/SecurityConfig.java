//package pers.yujie.dashboard.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer.JwtConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.header.writers.StaticHeadersWriter;
//
//@Configuration
//public class SecurityConfig implements WebSecurityConfigurer {
//
//  @Override
//  public void customize(HttpSecurity http) throws Exception {
//    http
//        .csrf().disable()
//        .authorizeRequests()
//        .anyRequest().authenticated()
//        .and()
//        .apply(new JwtConfigurer<>(jwtDecoder()))
//        .and()
//        .headers()
//        .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy", "default-src 'self'"))
//        .addHeaderWriter(new StaticHeadersWriter("X-Content-Security-Policy", "default-src 'self'"))
//        .addHeaderWriter(new StaticHeadersWriter("X-WebKit-CSP", "default-src 'self'"))
//        .and()
//        .sessionManagement()
//        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//  }
//
//  private JwtDecoder jwtDecoder() {
//    // Define your JWT decoder here
//  }
//
//  @Override
//  public void customize(WebSecurity web) {
//
//  }
//}