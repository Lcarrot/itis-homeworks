import java.util.concurrent.ThreadLocalRandom;

public class KeyGenerator {

  private final int remainder;
  private final int base;
  private final int secretKey = ThreadLocalRandom.current().nextInt(0, 10_000);

  public KeyGenerator(int base, int remainder) {
    this.remainder = remainder;
    this.base = base;
  }

  public int generateHelloKey() {
    return Math.powByRemainder(base, secretKey, remainder);
  }

  public int generateGeneralKey(int neighbourKey) {
    return Math.powByRemainder(neighbourKey, secretKey, remainder);
  }
}
