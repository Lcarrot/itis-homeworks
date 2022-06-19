package login;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pastebin.tests.dao.UserLogin;

public class LoginTest extends LoginBaseTest {

  @Before
  public void setUp() {
    userLogin = UserLogin.builder().login("Lcarrot").password("wznEF@zF6jfnxJ7").build();
  }

  @Test
  public void login() throws Exception {
    loginPattern();
  }

  @After
  public void tearDown() throws Exception {
    closeDriver();
  }
}
