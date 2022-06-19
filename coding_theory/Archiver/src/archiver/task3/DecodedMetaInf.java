package archiver.task3;

import java.util.List;

public class DecodedMetaInf {

  private final Character[] alphabet;
  private final List<Integer> codes;

  public DecodedMetaInf(Character[] alphabet, List<Integer> code) {
    this.alphabet = alphabet;
    this.codes = code;
  }

  public Character[] getAlphabet() {
    return alphabet;
  }

  public List<Integer> getCodes() {
    return codes;
  }
}
