package paste;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pastebin.tests.dao.Paste;

public class PasteTest extends PasteBaseTest {

  @Before
  public void setUp() {
    paste = Paste.builder().text(" Здарова бандиты").build();
  }

  @Test
  public void createPaste() {
    driver.get(baseUrl);
    createPastePattern();
  }

  @Test
  public void checkLastPaste() {
    String expected = "fgfdgfg";
    driver.get(baseUrl + "/lcarrot");
    String actual = getLastPasteText();
    Assert.assertEquals(expected, actual);
  }

  @After
  public void tearDown() throws Exception {
    closeDriver();
  }


}
