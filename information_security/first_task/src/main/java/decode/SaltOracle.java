package decode;

import java.nio.charset.StandardCharsets;

import encode.EncoderWithSalt;

public class SaltOracle {

  private final EncoderWithSalt encoderWithSalt;

  public SaltOracle(EncoderWithSalt encoderWithSalt) {
    this.encoderWithSalt = encoderWithSalt;
  }

  public String guessSalt(int blockSize) {
    int i = 1;
    StringBuilder salt = new StringBuilder();
    char currentChar = getNextChar("", blockSize);
    while (currentChar != '!') {
      i++;
      salt.append(currentChar);
      currentChar = getNextChar(salt.toString(), blockSize);
    }
    return salt.toString();
  }

  private char getNextChar(String currentSalt, int blockSize) {
    int saltLength = currentSalt.getBytes(StandardCharsets.UTF_8).length;
    int repeatForFullBlocks = blockSize - (saltLength % blockSize) - 1;
    String newMessage = "A".repeat(repeatForFullBlocks);
    byte[] rightMessage = encoderWithSalt.encode(newMessage);
    String ackBlock = "A".repeat(repeatForFullBlocks).concat(currentSalt);
    for (int i = 0; i < 27; i++) {
      char currentChar = (char) ('A' + i);
      String currentMessage = ackBlock  + currentChar;
      byte[] currentBytes = encoderWithSalt.encode(currentMessage);
      int fullBlocks = saltLength / blockSize;
      if (isEqualsParts(rightMessage, currentBytes, fullBlocks * blockSize, blockSize)) {
        return currentChar;
      }
    }
    return '!';
  }

  private boolean isEqualsParts(byte[] first, byte[] second, int start, int length) {
    for (int i = start; i < start + length; i++) {
      if (first[i] != second[i]) {
        return false;
      }
    }
    return true;
  }
}
