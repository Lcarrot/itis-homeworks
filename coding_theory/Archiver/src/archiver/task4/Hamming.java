package archiver.task4;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import archiver.api.Coding;
import archiver.api.Utils;

public class Hamming implements Coding {

  BitUtils bitUtils = new BitUtils();

  @Override
  public Path decode(Path file) {
    Path path = Utils.createDecodedPath(file);
    try (BufferedReader inputStream = Files.newBufferedReader(file);
         FileOutputStream outputStream = new FileOutputStream(path.toFile())) {
      char[] code = new char[14];
      char bit = (char) inputStream.read();
      while (bit != (char) -1) {
        code[0] = bit;
        for (int i = 1; i < 14; i++) {
          code[i] = (char) inputStream.read();
        }
        byte source = bitUtils.hammingDecode(code);
        outputStream.write(source);
        bit = (char) inputStream.read();
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return path;
  }

  @Override
  public Path encode(Path file) {
    Path path = Utils.createEncodedPath(file);
    try (FileInputStream inputStream = new FileInputStream(file.toFile());
         BufferedWriter outputStream = Files.newBufferedWriter(path)) {
      while (inputStream.available() > 0) {
        char[] chars = bitUtils.hammingEncode((byte) inputStream.read());
        outputStream.write(chars, 0, 14);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return path;
  }
}
