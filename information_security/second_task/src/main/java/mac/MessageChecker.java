package mac;

public class MessageChecker {

  private byte[] mark = new byte[16];

  public void setMark(byte[] message) {
    System.arraycopy(message, message.length - 16, mark, 0, 16);
  }

  public boolean checkMark(byte[] message) {
    byte[] timeBuffer = new byte[16];
    System.arraycopy(message, message.length - 16, timeBuffer, 0, 16);
    for (int i = 0; i < 16; i++) {
      if (mark[i] != timeBuffer[i]) {
        return false;
      }
    }
    return true;
  }
}
