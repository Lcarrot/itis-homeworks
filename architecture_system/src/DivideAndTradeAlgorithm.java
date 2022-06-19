import java.util.*;

public class DivideAndTradeAlgorithm {

  /**
   * Функция принимает на вход все предметы и вместимость рюкзака
   */
  public Items getStolenItems(Item[] allItems, Integer backpackSpace) {
    Items[] firstPart = getPartOfItems(0 , allItems.length / 2, allItems, backpackSpace); // получаем все возможные множества первой половины предметов
    Items[] secondPart = getPartOfItems(allItems.length / 2, allItems.length - 1, allItems, backpackSpace); // получаем все возможные множества второй половины предметов
    Items maxItems = firstPart[0]; // возьмём за начальный максимальный набор предметов, который можем получить, первый набор предметов из 1 коллекции
    for (Items items: secondPart) {
      int index = findItemsIndex(firstPart, items, backpackSpace); // находим индекс
      items.addItems(firstPart[index]); // добавляем предметы
      maxItems = items.getValueOfItems() > maxItems.getValueOfItems() ? items : maxItems; // сравниваем с максимальным
    }
    return maxItems;
  }

  /**
   * Функция предназначена для получения всевозможных комбинаций части предметов,
   * при этом комбинации, которые имеют больший вес и меньшую ценность, чем другие, отбрасываются
   * Комбинации отсортированы по весу всех предметов
   */
  private Items[] getPartOfItems(int startIndex, int endIndex, final Item[] sourceItemsArray, int backpackSpace) {
    Map<Integer, Items> itemMapByWeight = new HashMap<>();
    int size = endIndex - startIndex + 1;
    // получим все возмодные множества предметов, при этом
    // если они одинакового веса, возьмём с наибольшей ценностью,
    // если не вместится в рюкзак, выбрасываем
    for (int i = 0; i < Math.pow(2, size); i++) {
      Items items = new Items();
      for (int k = 0; k <= endIndex; k++) {
        if ((k & i) == 1) {
          items.addItem(sourceItemsArray[k]);
        }
      }
      if (items.getWeightOfItems() > backpackSpace) continue;
      Items currentItems = itemMapByWeight.get(items.getWeightOfItems());
      itemMapByWeight.put(items.getWeightOfItems(),
          items.getValueOfItems() > currentItems.getValueOfItems() ? items : currentItems
      );
    }
    Items[] itemsArray = itemMapByWeight.values().toArray(new Items[0]);
    Arrays.sort(itemsArray, new Items.ComparatorByWeight());
    // выбрасываем элементы, которые имеют больший вес, но меньшую стоимость по сравнению с другими
    for (int i = 0; i < itemsArray.length - 1; i++) {
      if (itemsArray[i] == null) continue;
      for (int j = i + 1; j < itemsArray.length; j++) {
        if (itemsArray[j] != null && itemsArray[i].getValueOfItems() >= itemsArray[j].getValueOfItems()) {
          itemsArray[j] = null;
        }
      }
    }
    return Arrays.stream(itemsArray).filter(Objects::nonNull).toArray(Items[]::new);
  }

  private int findItemsIndex(Items[] itemsArray, Items items, int backpackSpace) {
    int weight = items.getWeightOfItems();
    int leftBoard = 0;
    int rightBoard = itemsArray.length;
    // с помощью бинарного поиска ищем индекс, где элементы будут давать в сумме с предметвами вес меньше либо равным макс весом рюкзака
    while (rightBoard - leftBoard > 1) {
      int currentIndex = leftBoard + (rightBoard - leftBoard) / 2;
      Items currentItems = itemsArray[currentIndex];
      if (currentItems.getWeightOfItems() + weight > backpackSpace) {
        rightBoard = currentIndex;
      }
      else {
        leftBoard = currentIndex;
      }
    }
    if (itemsArray[rightBoard].getWeightOfItems() + weight > backpackSpace) rightBoard--;
    int index = 0;
    int maxValuable = 0;
    //  выбираем элемент, с которым в сумме будет максимальная выгода
    for (int i = rightBoard; i >= 0; i--) {
      if (items.getValueOfItems() + itemsArray[i].getValueOfItems() >= maxValuable) {
        maxValuable = items.getValueOfItems() + itemsArray[i].getValueOfItems();
        index = i;
      }
    }
    return index;
  }
}
