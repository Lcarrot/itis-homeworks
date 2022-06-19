package archiver.task2;

public class Interval {

  private long leftBoard;
  private long rightBoard;

  public Interval(long leftBoard, long rightBoard) {
    this.leftBoard = leftBoard;
    this.rightBoard = rightBoard;
  }

  public void calculateNewBoards(DoubleInterval interval) {
    long time_board = leftBoard;
    leftBoard = (long) (time_board + (rightBoard - time_board) * interval.getLeftBord());
    rightBoard = (long) (time_board + (rightBoard - time_board) * interval.getRightBoard());
  }

  public boolean hasEqualPart(long start_position) {
    long left_board_start = leftBoard / start_position;
    long right_board_start = rightBoard / start_position;
    return (left_board_start == right_board_start);
  }

  public byte getEqualPart(long start_position) {
    byte current_code = (byte) (leftBoard / start_position);
    leftBoard = (leftBoard % start_position) * 10;
    rightBoard = (rightBoard % start_position) * 10 + 9;
    return current_code;
  }

  public void add(long start_position) {
    leftBoard = (leftBoard % start_position) * 10;
    rightBoard = (rightBoard % start_position) * 10 + 9;
  }

  public long getLeftBoard() {
    return leftBoard;
  }

  public long getRightBoard() {
    return rightBoard;
  }
}
