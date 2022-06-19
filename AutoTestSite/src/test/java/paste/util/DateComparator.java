package paste.util;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DateComparator implements Comparator<WebElement> {

  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
  @Override
  public int compare(WebElement o1, WebElement o2) {
    return getDate(o1).compareTo(getDate(o2));
  }

  @SneakyThrows
  private Date getDate(WebElement element) {
    return format.parse(element.findElement(By.className("dt-published")).getAttribute("datetime"));
  }
}
