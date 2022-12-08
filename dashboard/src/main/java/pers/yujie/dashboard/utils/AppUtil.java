package pers.yujie.dashboard.utils;

import javafx.application.Application;
import javax.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import pers.yujie.dashboard.DashboardApplication;

@Component
public class AppUtil {

  public static void exitApplication(ApplicationContext ctx, int exitCode) {
    ((ConfigurableApplicationContext) ctx).close();
    System.exit(exitCode);
  }

}
