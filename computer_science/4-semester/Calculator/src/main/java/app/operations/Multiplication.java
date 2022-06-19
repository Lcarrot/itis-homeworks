package app.operations;

import java.util.List;

public class Multiplication implements MathOperation {
    @Override
    public int calculate(List<Integer> numbers) {
        int result = 1;
        for (Integer integer: numbers) {
            result *= integer;
        }
        return result;
    }
}
