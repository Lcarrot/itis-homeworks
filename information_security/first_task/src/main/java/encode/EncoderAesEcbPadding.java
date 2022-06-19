package encode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import encode.api.Encoder;
import encode.util.EncoderUtils;
import lombok.SneakyThrows;

public class EncoderAesEcbPadding implements Encoder {

  private final static String AES_128_ECB = "AES/ECB/PKCS5Padding";
  private final Cipher cipher;

  @SneakyThrows
  public EncoderAesEcbPadding() {
    SecretKey secretKey = EncoderUtils.generateSecretKey();
    cipher = Cipher.getInstance(AES_128_ECB);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
  }

  @SneakyThrows
  @Override
  public byte[] encode(byte[] source) {
    return cipher.doFinal(source);
  }
}
