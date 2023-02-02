package pers.yujie.tester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TesterApplication {

  public static void main(String[] args) {
    SpringApplication.run(TesterApplication.class, args);
  }

}
