package util;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.SneakyThrows;

public class EncoderUtils {

  @SneakyThrows
  public static SecretKey getSecretKey(String keyWord) {
    return new SecretKeySpec(keyWord.getBytes(StandardCharsets.UTF_8), EncryptConstants.AES);
  }

  public static IvParameterSpec getZeroIv() {
    byte[] bytes = new byte[16];
    return new IvParameterSpec(bytes);
  }
}
