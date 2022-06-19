package archiver.task3;

public class SortVector {

  private Character symbol;
  private boolean isAdded;

  public SortVector(Character symbol) {
    this.symbol = symbol;
    this.isAdded = false;
  }

  public Character getSymbol() {
    return symbol;
  }

  public boolean isAdded() {
    return isAdded;
  }

  public void setAdded(boolean added) {
    isAdded = added;
  }
}
