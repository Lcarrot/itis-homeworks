/**
 * Оберточный класс коллекции, предназначенный для более удобной работы с набором предметов
 */
public class Item {

  private final Integer weight;
  private final Integer value;

  public Item(Integer weight, Integer value) {
    this.weight = weight;
    this.value = value;
  }

  public Integer getWeight() {
    return weight;
  }

  public Integer getValue() {
    return value;
  }
}
