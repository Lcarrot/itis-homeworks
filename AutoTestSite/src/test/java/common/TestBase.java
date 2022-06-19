package common;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;

public abstract class TestBase {

    protected WebDriver driver;
    protected AppManager appManager;
    JavascriptExecutor js;
    protected String baseUrl;

    protected TestBase() {
        System.setProperty("webdriver.chrome.driver", "D:\\projects\\PastebinTest\\src\\main\\resources\\chromedriver.exe");
        appManager = (AppManager) IocContainer.getInstance(AppManager.class);
        driver = appManager.getChromeDriver();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        baseUrl = "https://write.as";
        Dimension dimension = new Dimension(1920, 1080);
        driver.manage().window().setSize(dimension);
        js = (JavascriptExecutor) driver;
    }

    public void closeDriver() {
        appManager.destroy();
    }
}
