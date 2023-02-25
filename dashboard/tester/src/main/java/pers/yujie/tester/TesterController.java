package pers.yujie.tester;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
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

/**
 * Main controller that is responsible for receiving test parameters and sending results.
 *
 * @author Yujie Chen
 * @version 1.0.2
 * @since 19/01/2023
 */
@Controller
public class TesterController {

  @Value("${chrome.version}")
  private String chromeVersion;

  @Value("${firefox.version}")
  private String firefoxVersion;

  private static final ChromeOptions chromeOptions = new ChromeOptions();
  private static final FirefoxOptions firefoxOptions = new FirefoxOptions();

  // Both types have been set to headless mode
  static {
    chromeOptions.addArguments("-headless");
    chromeOptions.addArguments("--disable-web-security"); // web security disabled to allow CORS

    firefoxOptions.setHeadless(true);
  }

  /**
   * Receive the mode and URL to conduct automated testing using Webdriver.
   *
   * @param mode the browser type, 'chrome' or 'firefox'
   * @param url  the URL of the website to be tested
   * @return {@link HttpStatus#OK} if succeeded, {@link HttpStatus#BAD_REQUEST} if failed
   */
  @GetMapping("webdriver")
  public ResponseEntity<String> testChrome(@RequestParam(value = "mode") String mode,
      @RequestParam(value = "url") String url) {
    // check the input parameters
    if (mode == null || mode.length() < 1 || url == null || url.length() < 1 || !validateURL(url)) {
      return new ResponseEntity<>("Invalid request params", HttpStatus.BAD_REQUEST);
    }

    // create an instance of webdriver
    WebDriver driver = null;
    if (mode.equals("chrome")) {
      driver = WebDriverManager.chromedriver().browserVersion(chromeVersion)
          .capabilities(chromeOptions).create();
    } else if (mode.equals("firefox")) {
      driver = WebDriverManager.firefoxdriver().browserVersion(firefoxVersion)
          .capabilities(firefoxOptions).create();
    }

    // request the URL and implicitly wait for three seconds
    if (driver != null) {
      try {
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.findElement(By.id("test"));
      } catch (NoSuchElementException ignored) { // ignore the implicitly waiting exception
      } catch (TimeoutException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
      }
      // close the driver
      driver.quit();
    }
    return new ResponseEntity<>("Test completed", HttpStatus.OK);
  }

  /**
   * Check if the input URL is valid, which only supports http or https.
   *
   * @param url the URL to be tested
   * @return true if valid, false otherwise
   */
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
