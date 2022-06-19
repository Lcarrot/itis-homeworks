package archiver.task4;

import java.util.ArrayList;
import java.util.List;

public class BitUtils {

  public char[] hammingEncode(byte b) {
    char[] wBits = new char[14];
    byte[] sBits = getBits(b);
    int index = 0;
    for (int i = 0; i < 2; i++) {
      byte[] forChecking = new byte[4];
      for (int l = 0; l < 4; l++) {
        wBits[index++] = getBitAsChar(sBits[i * 4 + l]);
        forChecking[l] = sBits[i * 4 + l];
      }
      byte[] checkingBits = getCheckingBits(forChecking);
      for (int k = 0; k < 3; k++) {
        wBits[index++] = getBitAsChar(checkingBits[k]);
      }
    }
    return wBits;
  }

  private char getBitAsChar(byte bit) {
    return (bit == 0) ? '0' : '1';
  }

  private byte getCharAsByte(char bit) {
    return (byte) ((bit == '0') ? 0 : 1);
  }

  private byte[] getBits(byte sourceByte) {
    int start_value = 128;
    byte[] bytes = new byte[8];
    for (int i = 0; i < 8; i++) {
      bytes[i] = (byte) ((sourceByte & start_value) >> (7 - i));
      start_value /= 2;
    }
    return bytes;
  }

  private byte[] getCheckingBits(byte[] forChecking) {
    byte[] bytes = new byte[3];
    bytes[0] = (byte) ((forChecking[0] + forChecking[1] + forChecking[2]) % 2);
    bytes[1] = (byte) ((forChecking[0] + forChecking[1] + forChecking[3]) % 2);
    bytes[2] = (byte) ((forChecking[0] + forChecking[2] + forChecking[3]) % 2);
    return bytes;
  }

  public byte hammingDecode(char[] code) {
    byte sourceByte = 0;
    List<Byte> list = new ArrayList<>(14);
    for (char coded : code) {
        list.add(getCharAsByte(coded));
    }
    int index = 0;
      for (int k = 0; k < 2; k++) {
        byte[] bits = new byte[7];
        for (int i = 0; i < 7; i++) {
          bits[i] = list.get(index++);
        }
        int errorPlace = getErrorPlace(bits);
        if (errorPlace != -1) {
          bits[errorPlace] = changeBit(bits[errorPlace]);
        }
        for (int t = 0; t < 4; t++) {
          sourceByte += (byte) (bits[t] << (7 - (k * 4 + t)));
        }
    }
    return sourceByte;
  }

  private int getErrorPlace(byte[] bytes) {
    int index = 0;
    index += (bytes[0] + bytes[1] + bytes[2] + bytes[4]) % 2 << 2; // 4
    index += (bytes[0] + bytes[1] + bytes[3] + bytes[5]) % 2 << 1; // 2
    index += (bytes[0] + bytes[2] + bytes[3] + bytes[6]) % 2; // 1
    if (index == 0) return -1;
    return 7 - index;
  }

  private byte changeBit(byte errorBit) {
    if (errorBit == 1) return 0;
    else return 1;
  }
}
