package pastebin.tests.dao;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Paste {

    private String text;
}
