package archiver.task2;

public class ArithmeticValue {

  private final Character symbol;
  private final Interval interval;

  public ArithmeticValue(Character symbol, Interval interval) {
    this.symbol = symbol;
    this.interval = interval;
  }

  public Character getSymbol() {
    return symbol;
  }

  public Interval getInterval() {
    return interval;
  }
}
