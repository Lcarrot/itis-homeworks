package archiver.task1;

import java.util.*;
import java.util.stream.Collectors;

public class HuffmanTree {

  public Map<Character, Integer> setCodes(HuffmanValue[] values) {
    List<Node> arrayList = Arrays.stream(values)
        .map(value -> new Node(value, value.getCount()))
        .collect(Collectors.toCollection(LinkedList::new));
    while (arrayList.size() > 1) {
      nextStep(arrayList);
    }
    return arrayList.get(0).getHuffmanValues().stream().collect(Collectors.toMap(HuffmanValue::getSymbol, HuffmanValue::getCode));
  }

  public void nextStep(List<Node> nodes) {
    Node first_node = nodes.get(0);
    nodes.remove(first_node);
    Node second_node = nodes.get(0);
    nodes.remove(second_node);
    second_node.getHuffmanValues().forEach(value -> value.addCode(  1));
    first_node.getHuffmanValues().forEach(value -> value.addCode(0));
    first_node.addValues(second_node.getHuffmanValues());
    first_node.addCount(second_node.count);
    nodes.add(binarySearch(nodes, first_node), first_node);
  }

  private int binarySearch(List<Node> nodes, Node node) {
    int left_index = 0;
    int right_index = nodes.size() - 1;
    int middle = (right_index - left_index) / 2;
    while (right_index - left_index > 1) {
      if (nodes.get(middle).getCount().compareTo(node.getCount()) > 0) {
        right_index = middle;
      }
      else if (nodes.get(middle).getCount().compareTo(node.getCount()) < 0){
        left_index = middle;
      }
      else {
        return middle;
      }
      middle = (right_index - left_index) / 2 + left_index;
    }
    return middle;
  }

  public static class Node {
    private Set<HuffmanValue> huffmanValues = new HashSet<>();
    private Long count = 0L;

    public Node(HuffmanValue value, long count) {
      huffmanValues.add(value);
      this.count = count;
    }

    public void addValues(Set<HuffmanValue> values) {
     huffmanValues.addAll(values);
    }

    public Long getCount() {
      return count;
    }

    public void addCount(long count) {
      this.count += count;
    }

    public Set<HuffmanValue> getHuffmanValues() {
      return huffmanValues;
    }
  }
}
