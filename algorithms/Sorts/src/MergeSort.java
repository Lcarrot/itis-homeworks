import java.util.Stack;

public class MergeSort {

    public static void sortRecursion(int[] data) {
        int[] array = sortRecursionBody(data);
        System.arraycopy(array, 0, data, 0, array.length);
    }

    private static int[] sortRecursionBody(int[] data) {
        if (data.length > 1) {
            int[][] smallArrays = new int[2][];
            int index = 0;
            int longs = data.length / 2;
            for (int i = 0; i < smallArrays.length; i++) {
                smallArrays[i] = new int[longs];
                System.arraycopy(data, index, smallArrays[i], 0, longs);
                index = longs;
                longs = data.length - longs;
            }
            for (int i = 0; i < smallArrays.length; i++) smallArrays[i] = sortRecursionBody(smallArrays[i]);
            data = merge(smallArrays[0], smallArrays[1]);
        }
        return data;
    }

    public static void sortDP(int[] data) {
        Stack<int[]> stack = new Stack<>();
        for (int i = 0; i < data.length - 1; i += 2) {
            if (data[i] < data[i + 1]) {
                stack.push(new int[]{data[i], data[i + 1]});
            } else {
                stack.push(new int[]{data[i + 1], data[i]});
            }
        }
        if (data.length % 2 != 0) {
            stack.push(new int[]{data[data.length - 1]});
        }
        while (stack.size() > 1) {
            int[] first = stack.pop();
            int[] second = stack.pop();
            stack.add(0, merge(first, second));
        }
        int[] array = stack.pop();
        System.arraycopy(array,0, data, 0, array.length);
    }

    private static int[] merge(int[] arr1, int[] arr2) {
        int[] data = new int[arr1.length + arr2.length];
        int firstIndex = 0;
        int secondIndex = 0;
        for (int i = 0; i < data.length; i++) {
            if (firstIndex < arr1.length) {
                if (secondIndex < arr2.length) {
                    data[i] = (arr1[firstIndex] < arr2[secondIndex]) ? arr1[firstIndex++] : arr2[secondIndex++];
                } else {
                    data[i] = arr1[firstIndex++];
                }
            } else {
                data[i] = arr2[secondIndex++];
            }
        }
        return data;
    }
}
