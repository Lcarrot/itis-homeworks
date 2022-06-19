package pastebin.tests.dao;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Data
public class UserLogin {

    private String login;
    private String password;
}
