package archiver.api;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import archiver.task3.DecodedMetaInf;

public class EncoderFileReaderAndWriter {

  public DecodedMetaInf readWord(File file) {
    try (DataInputStream scanner = new DataInputStream(new FileInputStream(file))) {
      char character = scanner.readChar();
      ArrayList<Character> characters = new ArrayList<>();
      while (character != '\n') {
        characters.add(character);
        character = scanner.readChar();
      }
      Character[] alphabet = characters.toArray(Character[]::new);
      List<Integer> integers = new ArrayList<>();
      while (scanner.available() > 0) {
        integers.add(scanner.readInt());
      }
      return new DecodedMetaInf(alphabet, integers);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void writeWord(Path file, ArrayList<Integer> code, Character[] symbols) {
    Path path = Utils.createEncodedPath(file);
    try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(path.toFile()))) {
      Arrays.stream(symbols).forEach(symbol -> {
        try {
          writer.writeChar(symbol);
        }
        catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
      writer.writeChar('\n');
      code.forEach(ind -> {
        try {
          writer.writeInt(ind);
        }
        catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
