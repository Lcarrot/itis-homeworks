package archiver.task1;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import archiver.api.Coding;
import archiver.api.Utils;

public class Huffman implements Coding {

  @Override
  public Path encode(Path file) {
    Path path = Utils.createEncodedPath(file);
    Map<Character, Integer> writing = getAlphabetAndCodes(file);
    try (BufferedReader reader = Files.newBufferedReader(file);
         DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(path.toFile()))) {
      Utils.writeAlphabet(writing, outputStream);
      Utils.writeCode(writing, outputStream, reader);
      outputStream.flush();
      }
    catch (IOException fileNotFoundException) {
      fileNotFoundException.printStackTrace();
    }
    return path;
  }

  private Map<Character, Integer> getAlphabetAndCodes(Path file) {
    Utils.MetaInfo info = Utils.readFileAndPutInMap(file);
    HuffmanValue[] values = info.getMap().entrySet()
        .stream()
        .map(entry -> new HuffmanValue(entry.getKey(), entry.getValue()))
        .sorted(new HuffmanComparator())
        .toArray(HuffmanValue[]::new);
    return setCodes(values);
  }

  private Map<Character, Integer> setCodes(HuffmanValue[] values) {
    return new HuffmanTree().setCodes(values);
  }


  @Override
  public Path decode(Path file) {
    Path path = Utils.createDecodedPath(file);
    decodeFile(file.toFile(), path);
    return path;
  }

  private void decodeFile(File file, Path decodedFile) {
    try (DataInputStream scanner = new DataInputStream(new FileInputStream(file));
         BufferedWriter outputStream = Files.newBufferedWriter(decodedFile)) {
      Map<Integer, Character> map = Utils.readAlphabetByIntegerKey(scanner);
      while (scanner.available() > 0) {
        int code = scanner.readInt();
        if (map.containsKey(code)) {
          outputStream.write(map.get(code));
        }
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

