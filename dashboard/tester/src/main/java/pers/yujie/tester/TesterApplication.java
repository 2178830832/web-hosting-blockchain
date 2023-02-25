package pers.yujie.tester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * The entrance of the tester application. It has excluded the data source configuration.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 02/01/2023
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TesterApplication {

  public static void main(String[] args) {
    SpringApplication.run(TesterApplication.class, args);
  }

}
