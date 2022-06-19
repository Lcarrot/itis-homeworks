package app.operations;

import java.util.List;


public class Addition implements MathOperation {
    @Override
    public int calculate(List<Integer> numbers) {
        int result = 0;
        for (Integer integer: numbers) {
            result += integer;
        }
        return result;
    }
}
