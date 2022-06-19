package archiver.api;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Utils {

  public static final char delimiterSymbol = '$';

  public static Utils.MetaInfo readFileAndPutInMap(Path file) {
    try (BufferedReader reader = Files.newBufferedReader(file)) {
      long totalCount = 0;
      Map<Character, Integer> alphabet = new HashMap<>();
      totalCount += putCharactersInMap(alphabet, reader);
      return new MetaInfo(alphabet, totalCount);
    }
    catch (IOException e) {
      e.printStackTrace();
      throw  new RuntimeException();
    }
  }

  public static void writeAlphabet(Map<Character, Integer> writing, DataOutputStream outputStream) {
    writing.forEach((key, value1) -> {
      try {
        outputStream.writeChar(key);
        outputStream.writeInt(value1);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    });
    try {
      outputStream.writeChar(delimiterSymbol);
    }
    catch (IOException e) {
      throw new RuntimeException();
    }
  }

  public static Map<Integer, Character> readAlphabetByIntegerKey(DataInputStream reader) throws IOException {
    Map<Integer, Character> map = new HashMap<>();
    char character = reader.readChar();
    while (character != delimiterSymbol) {
      map.put(reader.readInt(), character);
      character = reader.readChar();
    }
    return map;
  }

  public static Map<Character, Integer> readAlphabetByCharacterKey(DataInputStream reader) throws IOException {
    Map<Character, Integer> map = new HashMap<>();
    char character = reader.readChar();
    while (character != delimiterSymbol) {
      map.put(character, reader.readInt());
      character = reader.readChar();
    }
    return map;
  }

  public static void writeCode(Map<Character, Integer> writing, DataOutputStream outputStream, BufferedReader reader)
      throws IOException {
    char symbol = (char) reader.read();
    while (symbol != (char) -1) {
        if (writing.containsKey(symbol)) {
          outputStream.writeInt(writing.get(symbol));
      }
        symbol = (char) reader.read();
    }
    outputStream.flush();
  }

  public static long putCharactersInMap(Map<Character, Integer> map, BufferedReader reader)
      throws IOException {
    long totalCount = 0;
    char character = (char) reader.read();
    while (character != (char) -1) {
      totalCount++;
      if (map.containsKey(character)) {
        map.put(character, map.get(character) + 1);
      }
      else {
        map.put(character, 1);
      }
      character = (char) reader.read();
    }
    return totalCount;
  }

  public static Path createDecodedPath(Path file) {
    String[] fileNames = file.getFileName().toString().split("\\.");
    return Paths.get(file.getParent().toString(), fileNames[0].replace("encoded", "decoded") + "." + fileNames[1]);
  }

  public static Path createEncodedPath(Path file) {
    String[] fileName = file.getFileName().toString().split("\\.");
    return Paths.get(file.getParent().toString(), fileName[0] + "_encoded." + fileName[1]);
  }

  public static class MetaInfo {

    private final Map<Character, Integer> map;
    private final long totalCount;

    public MetaInfo(Map<Character, Integer> map, long totalCount) {
      this.map = map;
      this.totalCount = totalCount;
    }

    public Map<Character, Integer> getMap() {
      return map;
    }

    public long getTotalCount() {
      return totalCount;
    }
  }
}
