import java.util.Scanner;

import decode.SaltOracle;
import encode.EncoderAesEcbPadding;
import encode.EncoderWithSalt;
import validator.InputStringValidator;

public class Main {

  private static final Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    var ecbPadding = new EncoderAesEcbPadding();
    var saltEncoder = new EncoderWithSalt(ecbPadding);
    var oracle = new SaltOracle(saltEncoder);
    System.out.println("Введите режим работв \n1. Ввести salt вручную \n2. Сгенерировать ");
    int command = scanner.nextInt();
    scanner.nextLine();
    switch (command) {
      case 1:
        withInputSalt(saltEncoder);
        break;
      case 2:
        withGenerateSalt(saltEncoder);
        break;
      case 3:
        System.out.println("Unknown command");
        break;
    }
    System.out.println(saltEncoder.getSalt());
    System.out.println(oracle.guessSalt(16));
  }

  private static void withGenerateSalt(EncoderWithSalt encoderWithSalt) {
    encoderWithSalt.generateSalt();
  }

  private static void withInputSalt(EncoderWithSalt encoderWithSalt) {
    System.out.println("Введите salt");
    String salt = scanner.nextLine().trim();
    boolean isValid = new InputStringValidator().validate(salt);
    if (isValid) {
      encoderWithSalt.setSalt(salt);
    }
    else {
      System.out.println("Строка невалидна, допустимы символы : A-Z");
    }
  }
}
