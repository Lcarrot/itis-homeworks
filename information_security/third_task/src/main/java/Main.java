public class Main {

  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    Math.powByRemainder(192, Integer.MAX_VALUE, 193);
    long end = System.currentTimeMillis();
    System.out.println(end - start);
    start = System.currentTimeMillis();
    java.lang.Math.pow(123, Integer.MAX_VALUE);
    end = System.currentTimeMillis();
    System.out.println(end - start);
  }
}
