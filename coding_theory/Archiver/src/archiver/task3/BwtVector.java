package archiver.task3;

public class BwtVector {

  private final Character sourceSymbol;
  private int nextIndex;

  public BwtVector(Character sourceSymbol, int nextIndex) {
    this.sourceSymbol = sourceSymbol;
    this.nextIndex = nextIndex;

  }

  public Character getSourceSymbol() {
    return sourceSymbol;
  }

  public int getNextIndex() {
    return nextIndex;
  }

  public void setNextIndex(int nextIndex) {
    this.nextIndex = nextIndex;
  }
}
