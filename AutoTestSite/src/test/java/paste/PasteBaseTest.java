package paste;

import java.util.Comparator;
import java.util.List;

import common.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import paste.util.DateComparator;
import pastebin.tests.dao.Paste;

public class PasteBaseTest extends TestBase {

  protected Paste paste;

  protected void createPastePattern() {
    prepareCreateForm();
    fillPasteForm();
  }

  protected void fillPasteForm() {
    driver.findElement(By.id("writer")).sendKeys(paste.getText());
    driver.findElement(By.id("publish")).click();
  }

  public void prepareCreateForm() {
    driver.findElement(By.linkText("Start writing")).click();
    driver.findElement(By.id("writer")).clear();
  }

  public String getLastPasteText() {
    return getLastPaste().findElement(By.className("e-content")).getText();
  }

  private WebElement getLastPaste() {
    Comparator<WebElement> comparator = new DateComparator();
    return getAllPaste().stream().max(comparator).get();
  }

  private List<WebElement> getAllPaste() {
      return driver.findElement(By.id("wrapper")).findElements(By.className("h-entry"));
  }
}
