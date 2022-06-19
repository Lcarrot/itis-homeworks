package archiver.task1;

public class HuffmanValue {

  private final Character symbol;
  private final Integer count;
  private int code = 0;

  public HuffmanValue(Character symbol, Integer count) {
    this.symbol = symbol;
    this.count = count;
  }

  public Character getSymbol() {
    return symbol;
  }

  public Integer getCount() {
    return count;
  }

  public void addCode(int value) {
    if (code != 0) {
      code = code << 1;
    }
    code += value;
  }

  public int getCode() {
    return code;
  }
}
