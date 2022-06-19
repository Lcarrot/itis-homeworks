package archiver.app;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import archiver.api.Coding;

public class Main {

  public static void main(String[] args) {
    try (Scanner scanner = new Scanner(System.in)) {
      System.out.print("Choose algorithm: \n1.Huffman \n2.Arithmetic \n3.BWT and LZW \n4.Hamming \nEnter a number:\n");
      int algorithm = scanner.nextInt();
      scanner.nextLine();
      Coding coding = SimpleFactory.getAlgorithm(algorithm);
      System.out.print("1.Encode \n2.Decode \nEnter a number: \n");
      int command = scanner.nextInt();
      scanner.nextLine();
      System.out.print("Enter relative or absolute path to file: \n");
      String path = scanner.nextLine();
      Path retPath;
      if (command == 1) retPath = coding.encode(Paths.get(path));
      else if (command == 2) retPath = coding.decode(Paths.get(path));
      else throw new Exception();
      System.out.println("Your file:\n" + retPath);
    }
    catch (Exception e) {
      System.out.println("Something was wrong.");
    }
  }
}
