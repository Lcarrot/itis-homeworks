package archiver.api;

import java.io.*;
import java.util.Scanner;

public class SourceFileReaderAndWriter implements Closeable {

  private final Scanner scanner;

  public SourceFileReaderAndWriter(File file) {
    try {
      scanner = new Scanner(file);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String readAllFile() {
    StringBuilder builder = new StringBuilder();
    while (scanner.hasNext()) {
      builder.append(scanner.nextLine());
    }
    return builder.toString();
  }

  @Override
  public void close() {
    scanner.close();
  }
}
