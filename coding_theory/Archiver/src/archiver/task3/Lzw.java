package archiver.task3;

import java.util.*;
import java.util.stream.Collectors;

public class Lzw {

  public Lzw.LzwResult encode(String sourceCode) {
    Character[] symbols = getAlphabet(sourceCode);
    ArrayList<String> alphabet = Arrays.stream(symbols).map(String::valueOf).collect(Collectors.toCollection(ArrayList::new));
    ArrayList<Integer> result = encode(alphabet, sourceCode);
    return new LzwResult(symbols, result);
  }

  public String decode(Character[] characters, List<Integer> code) {
    ArrayList<String> alphabet = Arrays.stream(characters).map(String::valueOf).collect(Collectors.toCollection(ArrayList::new));
    StringBuilder result = new StringBuilder();
    int firstIndex = code.get(0);
    for (int i = 1; i < code.size(); i++) {
      int secondIndex = code.get(i);
      String newWord;
      if (secondIndex == alphabet.size()) {
        newWord = alphabet.get(firstIndex) + alphabet.get(firstIndex).charAt(0);
      }
      else {
        newWord = alphabet.get(firstIndex) + alphabet.get(secondIndex).charAt(0);
      }
      alphabet.add(newWord);
      result.append(alphabet.get(firstIndex));
      firstIndex = secondIndex;
    }
    result.append(alphabet.get(firstIndex));
    return result.toString();
  }

  private Character[] getAlphabet(String word) {
    Set<Character> characters = new HashSet<>();
    for (int i = 0; i < word.length(); i++) {
      characters.add(word.charAt(i));
    }
    Character[] chars = characters.toArray(new Character[0]);
    Arrays.sort(chars);
    return chars;
  }

  private ArrayList<Integer> encode(ArrayList<String> alphabet, String sourceCode) {
    StringBuilder builder = new StringBuilder();
    ArrayList<Integer> result = new ArrayList<>();
    int index;
    for (char symbol: sourceCode.toCharArray()) {
      builder.append(symbol);
      if (!alphabet.contains(builder.toString())) {
        alphabet.add(builder.toString());
        builder.deleteCharAt(builder.lastIndexOf(String.valueOf(symbol)));
        index = alphabet.indexOf(builder.toString());
        result.add(index);
        builder = new StringBuilder();
        builder.append(symbol);
      }
    }
    index = alphabet.indexOf(builder.toString());
    result.add(index);
    return result;
  }

  public static class LzwResult {

    LzwResult(Character[] sourceAlphabet, ArrayList<Integer> word) {
      this.sourceAlphabet = sourceAlphabet;
      this.word = word;
    }

    public Character[] getSourceAlphabet() {
      return sourceAlphabet;
    }

    public ArrayList<Integer> getWord() {
      return word;
    }

    private final Character[] sourceAlphabet;
    private final ArrayList<Integer> word;
  }
}
