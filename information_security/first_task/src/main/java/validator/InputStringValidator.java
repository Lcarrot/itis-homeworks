package validator;

public class InputStringValidator {

  public boolean validate(String inputString) {
    for (char character: inputString.toCharArray()) {
      if (character < 'A' || character > 'Z') {
        return false;
      }
    }
    return true;
  }
}
