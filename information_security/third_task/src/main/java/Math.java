public class Math {

  private Math(){
    // can't create instance
  }

  public static int powByRemainder(int base, int exponent, int remainder) {
    long result = 1;
    int i = 0;
    while (i < exponent) {
      if (i == 0 || i*2 > exponent || i*2 < 0) {
        result *= (base % remainder);
        i++;
      }
      else {
        result *= result;
        i *= 2;
      }
      result %= remainder;
    }
    return (int) result;
  }
}
