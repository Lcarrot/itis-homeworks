package login;

import common.TestBase;
import org.openqa.selenium.By;
import pastebin.tests.dao.UserLogin;

public class LoginBaseTest extends TestBase {

  protected UserLogin userLogin;


  protected void loginPattern() {
    prepareLoginForm();
    fillLoginForm();
    driver.findElement(By.id("official-writing")).click();
    driver.findElement(By.id("btn-login")).click();
  }

  protected void prepareLoginForm() {
    driver.findElement(By.linkText("Log in")).click();
    driver.findElement(By.name("alias")).clear();
    driver.findElement(By.name("pass")).clear();
  }

  protected void fillLoginForm() {
    driver.findElement(By.name("alias")).sendKeys(userLogin.getLogin());
    driver.findElement(By.name("pass")).sendKeys(userLogin.getPassword());
  }
}
