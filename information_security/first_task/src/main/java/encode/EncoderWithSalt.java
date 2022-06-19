package encode;

import java.nio.charset.StandardCharsets;

import encode.api.Encoder;
import encode.util.EncoderUtils;

public class EncoderWithSalt {

  private final Encoder encoder;
  private String salt;

  public EncoderWithSalt(Encoder encoder) {
    this.encoder = encoder;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public void generateSalt() {
    this.salt = EncoderUtils.generateSalt();
  }

  public String getSalt() {
    return salt;
  }

  public byte[] encode(String source) {
    String message = source + salt;
    return encoder.encode(message.getBytes(StandardCharsets.UTF_8));
  }

}
