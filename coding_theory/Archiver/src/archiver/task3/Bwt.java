package archiver.task3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import archiver.api.Utils;

public class Bwt {

  public String decode(String code) {
    int init_index = -1;
    char[] source = code.toCharArray();
    char[] sorted = code.toCharArray();
    Arrays.sort(sorted);
    List<SortVector> sortVectors = new ArrayList<>(sorted.length);
    for (char symbol: sorted) {
      sortVectors.add(new SortVector(symbol));
    }
    BwtVector[] bwtVectors = new BwtVector[source.length];
    for (int i = 0; i < source.length; i++) {
      int symbol_index = findIndex(source[i], sortVectors);
      bwtVectors[symbol_index] = new BwtVector(source[i], i);
      if (source[i] == Utils.delimiterSymbol) init_index = symbol_index;
    }
    StringBuilder builder = new StringBuilder();
    int nextIndex = bwtVectors[init_index].getNextIndex();
    while (nextIndex != init_index) {
      builder.append(bwtVectors[nextIndex].getSourceSymbol());
      nextIndex = bwtVectors[nextIndex].getNextIndex();
    }
    return builder.toString();
  }

  public int findIndex(Character symbol, List<SortVector> vectors) {
    int left_index = 0;
    int right_index = vectors.size() - 1;
    int middle = (right_index - left_index) / 2;
    while (right_index - left_index > 1) {
      if (vectors.get(middle).getSymbol().compareTo(symbol) > 0) {
        right_index = middle;
      }
      else if (vectors.get(middle).getSymbol().compareTo(symbol) < 0) {
        left_index = middle;
      }
      else {
        if (!vectors.get(middle).isAdded()) {
          while (middle >= 0 && !vectors.get(middle).isAdded() && vectors.get(middle)
              .getSymbol()
              .equals(symbol)) {
            middle--;
          }
          middle += 1;
        }
        else {
          while (middle <= vectors.size() && vectors.get(middle).isAdded() && vectors.get(middle)
              .getSymbol()
              .equals(symbol)) {
            middle++;
          }
        }
        vectors.get(middle).setAdded(true);
        return middle;
      }
      middle = (right_index - left_index) / 2 + left_index;
    }
    if (!vectors.get(left_index).isAdded() && vectors.get(left_index).getSymbol().equals(symbol)) return left_index;
    return right_index;
  }

  public String encode(String sourceWord) {
    sourceWord = sourceWord.concat(String.valueOf(Utils.delimiterSymbol));
    String[] words = doCyclicalShifts(sourceWord);
    Arrays.sort(words);
    return codeWord(words);
  }

  private String codeWord(String[] words) {
    StringBuilder builder = new StringBuilder();
    Arrays.stream(words).forEach(word -> builder.append(word.charAt(word.length() - 1)));
    return builder.toString();
  }

  private String[] doCyclicalShifts(String firstWord) {
    char[] symbols = firstWord.toCharArray();
    int length = symbols.length;
    String[] words = new String[symbols.length];
    words[0] = firstWord;
    for (int i = 1; i < length; i++) {
      words[i] = String.valueOf(symbols, i, length - i).concat(String.valueOf(symbols, 0, i));
    }
    return words;
  }

  public static class BwtResult {

    private final String code;
    private final int index;

    BwtResult(String code, int index) {
      this.code = code;
      this.index = index;
    }

    public String getCode() {
      return code;
    }

    public int getIndex() {
      return index;
    }
  }
}
