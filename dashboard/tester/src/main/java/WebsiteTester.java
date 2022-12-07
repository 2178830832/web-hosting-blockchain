import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class WebsiteTester {

  public static void main(String[] args) {
    Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
    System.setProperty("webdriver.edge.silentOutput", "true");
    System.setProperty("webdriver.chrome.silentOutput", "true");
    File driverFile = new File("tester/src/main/resources/drivers/msedgedriver.exe");
    if (!driverFile.exists()) {
      System.out.println("Webdriver file does not exist");
      System.exit(1);
    }
    System.setProperty("webdriver.edge.driver", driverFile.getAbsolutePath());
    EdgeOptions edgeOptions = new EdgeOptions();
    EdgeDriver driver = new EdgeDriver(edgeOptions);
    try {
      StopWatch pageLoad = new StopWatch();
      pageLoad.start();
      //Open your web app
      driver.get("https://www.baidu.com/");

      pageLoad.stop();
      //Get the time
      long pageLoadTime_ms = pageLoad.getTime();
      long pageLoadTime_Seconds = pageLoadTime_ms / 1000;
      System.out.println("Total Page Load Time: " + pageLoadTime_ms + " milliseconds");
      System.out.println("Total Page Load Time: " + pageLoadTime_Seconds + " seconds");
      driver.close();
    } finally {
      driver.quit();
    }
  }
}
