import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class Tester {

  public static void main(String[] args) throws InterruptedException {
    System.setProperty("webdriver.edge.driver",
        "C:\\Users\\Guillered\\Desktop\\Y4\\web-hosting-blockchain\\tester\\src\\main\\resources\\msedgedriver.exe");
    EdgeOptions edgeOptions = new EdgeOptions();
    EdgeDriver driver = new EdgeDriver(edgeOptions);
    try {
      driver.navigate().to("https://www.google.com");
//      System.out.println("Success");
//      Thread.sleep(500);
    } finally {
      driver.quit();
    }
  }

}
