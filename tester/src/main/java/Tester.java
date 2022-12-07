import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class Tester {

  public static void main(String[] args) throws InterruptedException {
    System.setProperty("webdriver.edge.driver",
        "C:\\Users\\Guillered\\Desktop\\Y4\\web-hosting-blockchain\\tester\\src\\main\\resources\\msedgedriver.exe");
    EdgeOptions edgeOptions = new EdgeOptions();
    EdgeDriver driver = new EdgeDriver(edgeOptions);
    try {
      WebDriver driver;
      driver = new FirefoxDriver();
      StopWatch pageLoad = new StopWatch();
      pageLoad.start();
      //Open your web app (In my case, I opened facebook)
      driver.get("https://www.facebook.com/");
      // Wait for the required any element (I am waiting for Login button in fb)
      WebDriverWait wait = new WebDriverWait(driver, 10);
      wait.until(ExpectedConditions.presenceOfElementLocated(By.id("u_0_l")));

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
