package encode.util;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncoderUtils {

  public static SecretKey generateSecretKey() {
    KeyGenerator generator;
    try {
      generator = KeyGenerator.getInstance(EncryptConstants.AES);
    }
    catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    generator.init(EncryptConstants.KEY_SIZE);
    return generator.generateKey();
  }

  public static String generateSalt() {
    StringBuilder builder = new StringBuilder();
    int size = ThreadLocalRandom.current().nextInt(17, 32);
    for (int i = 0; i < size; i++) {
      builder.append((char) ThreadLocalRandom.current().nextInt('A', 'Z'));
    }
    return builder.toString();
  }
}
