package pers.yujie.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Entrance of the Dashboard Application. Since this app does not use traditional databases, the
 * {@link DataSourceAutoConfiguration} has been removed. The other three excluded classes are Spring
 * Security's auto configuration. To allow Spring AOP, the {@link EnableAspectJAutoProxy} annotation
 * is included.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @see SpringBootApplication
 * @since 19/11/2022
 */
@EnableAspectJAutoProxy
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
    SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class,
    UserDetailsServiceAutoConfiguration.class})
public class DashboardApplication {

  public static void main(String[] args) {
    SpringApplication.run(DashboardApplication.class, args);
  }

}
