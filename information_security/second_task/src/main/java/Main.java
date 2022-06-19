import java.nio.charset.StandardCharsets;

import decode.AesCbcDecoder;
import encode.AesCbcEncoder;
import mac.MessageChecker;
import virus.CodeChanger;

public class Main {

  private static AesCbcEncoder encoder = new AesCbcEncoder();
  private static AesCbcDecoder decoder = new AesCbcDecoder();
  private static CodeChanger codeChanger = new CodeChanger();
  private static MessageChecker messageChecker = new MessageChecker();
  private static String sourceMessage = "alert(\"Hello world!\");";


  public static void main(String[] args) {
    byte[] encodedSourceMessage = encoder.encode(sourceMessage);
    messageChecker.setMark(encodedSourceMessage);
    byte[] hackedMessage = codeChanger.changeCode(encodedSourceMessage);
    System.out.println(messageChecker.checkMark(hackedMessage));
    byte[] newMessage = decoder.decode(hackedMessage);
    System.out.println(new String(newMessage));
  }
}
