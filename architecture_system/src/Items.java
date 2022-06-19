import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Оберточный класс коллекции, предназначенный для более удобной работы с набором предметов
 */
public class Items {

  private int valueOfItems = 0;
  private int weightOfItems = 0;
  private final List<Item> items = new ArrayList<>();

  public int getValueOfItems() {
    return valueOfItems;
  }

  public int getWeightOfItems() {
    return weightOfItems;
  }

  public List<Item> getItems() {
    return items;
  }

  public void addItem(Item item) {
    valueOfItems += item.getValue();
    weightOfItems += item.getWeight();
    items.add(item);
  }

  public void addItems(Items items) {
    valueOfItems += items.getValueOfItems();
    weightOfItems += items.getWeightOfItems();
    this.items.addAll(items.getItems());
  }

  public static class ComparatorByWeight implements Comparator<Items> {

    @Override
    public int compare(Items o1, Items o2) {
      return o1.weightOfItems - o2.weightOfItems;
    }
  }
}
