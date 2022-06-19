package encode;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import encode.api.Encoder;
import util.EncoderUtils;
import lombok.SneakyThrows;

public class AesCbcEncoder implements Encoder {

  private final static String AES_CBC = "AES/CBC/NoPadding";
  private final String secretKeyWord = "YELLOW SUBMARINE";
  private final Cipher cipher;

  @SneakyThrows
  public AesCbcEncoder() {
    SecretKey secretKey = EncoderUtils.getSecretKey(secretKeyWord);
    IvParameterSpec iv = EncoderUtils.getZeroIv();
    cipher = Cipher.getInstance(AES_CBC);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
  }

  @SneakyThrows
  @Override
  public byte[] encode(String message) {
    String encodeMessage = message + " ".repeat(16 - (message.length() % 16));
    return cipher.doFinal(encodeMessage.getBytes(StandardCharsets.UTF_8));
  }
}
