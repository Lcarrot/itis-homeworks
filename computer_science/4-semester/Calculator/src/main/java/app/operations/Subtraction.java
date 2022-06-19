package app.operations;

import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class Subtraction implements MathOperation {
    @Override
    public int calculate(List<Integer> numbers) {
        Iterator<Integer> integerIterator = numbers.listIterator();
        int result = integerIterator.next();
        while (integerIterator.hasNext()){
            result -= integerIterator.next();
        }
        return result;
    }
}
