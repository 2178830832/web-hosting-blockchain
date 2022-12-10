package pers.yujie.dashboard.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppUtil {

  public static void exitApplication(ApplicationContext ctx, int exitCode) {
    ((ConfigurableApplicationContext) ctx).close();
    System.exit(exitCode);
  }

}
