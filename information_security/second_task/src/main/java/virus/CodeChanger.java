package virus;

import java.nio.charset.StandardCharsets;

import encode.AesCbcEncoder;

public class CodeChanger {

  public byte[] changeCode(byte[] source) {
    AesCbcEncoder encoder = new AesCbcEncoder();
    String neededMessage = "alert(\"You are pwned!\"); //";
    int lastIndexBlock = source.length - 16;
    byte[] lastBlock = new byte[16];
    System.arraycopy(source, source.length - 16, lastBlock, 0, 16);
    byte[] hacked = encoder.encode(neededMessage);
    return mergeArrays(hacked, lastBlock);
  }

  public byte[] mergeArrays(byte[] firstArray, byte[] secondArray) {
    byte[] newArray = new byte[firstArray.length + secondArray.length];
    System.arraycopy(firstArray, 0, newArray, 0, firstArray.length);
    System.arraycopy(secondArray, 0, newArray, firstArray.length, secondArray.length);
    return newArray;
  }
}
