package archiver.app;

import archiver.api.Coding;
import archiver.task1.Huffman;
import archiver.task2.Arithmetic;
import archiver.task3.BwtAndLzw;
import archiver.task4.Hamming;

public class SimpleFactory {

  public static Coding getAlgorithm(int command) throws Exception {
    return switch (command) {
      case 1 -> new Huffman();
      case 2 -> new Arithmetic();
      case 3 -> new BwtAndLzw();
      case 4 -> new Hamming();
      default -> throw new Exception();
    };
  }
}
