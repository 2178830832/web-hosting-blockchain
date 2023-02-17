package pers.yujie.tester;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import javax.annotation.Resource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TesterController {

  @Value("${chrome.version}")
  private String chromeVersion;

  @Value("${firefox.version}")
  private String firefoxVersion;

  private static final ChromeOptions chromeOptions = new ChromeOptions();
  private static final FirefoxOptions firefoxOptions = new FirefoxOptions();

  static {
    chromeOptions.addArguments("-headless");
    chromeOptions.addArguments("--disable-web-security");

    firefoxOptions.setHeadless(true);
  }

  @GetMapping("webdriver")
  public ResponseEntity<String> testChrome(@RequestParam(value = "mode") String mode,
      @RequestParam(value = "url") String url) {
    if (mode == null || mode.length() < 1 || url == null || url.length() < 1 || !validateURL(url)) {
      return new ResponseEntity<>("Invalid request params", HttpStatus.BAD_REQUEST);
    }

    WebDriver driver = null;
    if (mode.equals("chrome")) {
      driver = WebDriverManager.chromedriver().browserVersion(chromeVersion)
          .capabilities(chromeOptions).create();
    } else if (mode.equals("firefox")) {
      driver = WebDriverManager.firefoxdriver().browserVersion(firefoxVersion)
          .capabilities(firefoxOptions).create();
    }

    if (driver != null) {
      try {
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.findElement(By.id("test"));
      } catch (NoSuchElementException ignored) {
      } catch (TimeoutException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
      driver.quit();
    }
    return new ResponseEntity<>("Test completed", HttpStatus.OK);
  }

  private static boolean validateURL(String url) {

    try {
      URI uri = new URI(url);
      if (uri.getHost() == null) {
        return false;
      }
      if (uri.getScheme().equalsIgnoreCase("http")
          || uri.getScheme().equalsIgnoreCase("https")) {
        return true;
      }
    } catch (URISyntaxException e) {
      return false;
    }

    return false;
  }

}
