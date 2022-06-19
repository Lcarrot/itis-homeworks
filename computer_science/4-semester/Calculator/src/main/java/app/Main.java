package app;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    private static int length;
    private static int width;
    public static void main(String[] args) {
        length = scanner.nextInt();
        width = scanner.nextInt();
        int[][] array = new int[length][width];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                array[i][j] = scanner.nextInt(); }
        }System.out.print(getMatRank(array));
    }
    static void twist(int[][] array, int row1, int row2, int col) {
        for (int i = 0; i < col; i++) {
            int temp = array[row1][i];
            array[row1][i] = array[row2][i];
            array[row2][i] = temp; }
    }
    static int getMatRank(int[][] array) {
        int grade = Math.min(width, length);
        for (int row = 0; row < grade; row++) {
            if (array[row][row] != 0) {
                for (int col = 0; col < length; col++) {
                    if (col != row) {

                        double mult = (double) array[col][row] / array[row][row];
                        for (int i = 0; i < grade; i++)
                            array[col][i] -= mult * array[row][i];
                    }
                }
            } else {
                boolean divide = true;
                for (int i = row + 1; i < length; i++) {
                    if (array[i][row] != 0) {
                        twist(array, row, i, grade);
                        divide = false;
                        break;
                    }
                }
                if (divide) {
                    grade--;
                    for (int i = 0; i < length; i++)
                        array[i][row] = array[i][grade];
                }row--;
            }
        }return grade;
    }
}