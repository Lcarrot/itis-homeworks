package decode;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import decode.api.Decoder;
import util.EncoderUtils;
import lombok.SneakyThrows;

public class AesCbcDecoder implements Decoder {

  private final static String AES_CBC = "AES/CBC/NoPadding";
  private final String secretKeyWord = "YELLOW SUBMARINE";
  private final Cipher cipher;

  @SneakyThrows
  public AesCbcDecoder() {
    SecretKey secretKey = EncoderUtils.getSecretKey(secretKeyWord);
    IvParameterSpec iv = EncoderUtils.getZeroIv();
    cipher = Cipher.getInstance(AES_CBC);
    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
  }

  @SneakyThrows
  @Override
  public byte[] decode(byte[] encoded) {
    return cipher.doFinal(encoded);
  }
}
