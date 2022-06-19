package common;

import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.Assert.fail;

public class AppManager {

  private final ChromeDriver chromeDriver;
  protected StringBuffer verificationErrors = new StringBuffer();

  public AppManager() {
    chromeDriver = new ChromeDriver();
  }

  public ChromeDriver getChromeDriver() {
    return chromeDriver;
  }

  public void destroy() {
    chromeDriver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }
}
