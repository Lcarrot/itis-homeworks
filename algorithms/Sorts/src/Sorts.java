public class Sorts {

    public static  <T extends Comparable> boolean  BubbleSort(T[] array) {
        boolean flag = true;
        for (int i = 0; i<array.length && flag; i++) {
            flag = false;
            for (int k = array.length-1; k>i; k--) {
                if (array[k].compareTo(array[k-1]) < 0) {
                    flag = true;
                    T c = array[k-1];
                    array[k-1] = array[k];
                    array[k] = c;
                }
            }
        }
        return true;
    }
}
