package archiver.task1;

import java.util.Comparator;

public class HuffmanComparator implements Comparator<HuffmanValue> {

  @Override
  public int compare(HuffmanValue o1, HuffmanValue o2) {
    return o1.getCount().compareTo(o2.getCount());
  }
}
