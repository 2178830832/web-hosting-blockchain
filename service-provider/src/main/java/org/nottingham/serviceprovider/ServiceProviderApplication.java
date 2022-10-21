package org.nottingham.serviceprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ServiceProviderApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceProviderApplication.class, args);
  }

}
