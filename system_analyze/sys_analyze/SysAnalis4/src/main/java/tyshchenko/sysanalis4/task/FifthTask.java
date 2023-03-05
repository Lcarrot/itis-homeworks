package tyshchenko.sysanalis4.task;

import org.apache.commons.lang3.tuple.MutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tyshchenko.sysanalis4.model.Statistics;
import tyshchenko.sysanalis4.util.DateCsvReader;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static tyshchenko.sysanalis4.math.MathFormulas.getAverage;

@Component
public class FifthTask implements Task {

    @Autowired
    private DateCsvReader dateCsvReader;

    @Override
    public List<Statistics> execute() {
        var dates = dateCsvReader.getDates();
        Map<MutablePair<Month, Integer>, Long> monthAndApplicationAmount = dates.stream()
                .sorted()
                .collect(Collectors.groupingBy(date -> new MutablePair<>(date.getMonth(), date.getYear()), Collectors.counting()));

        Map<Month, Integer> monthAndAmountInList = new EnumMap<>(Month.class);
        for (Month m : Month.values()){
            monthAndAmountInList.putIfAbsent(m, 0);
        }

        monthAndApplicationAmount.forEach((key, value) -> {
            Month month = key.getKey();
            monthAndAmountInList.put(month, monthAndAmountInList.getOrDefault(month, 0) + 1);
            key.setValue(monthAndAmountInList.get(month));
        });
        var average = getAverage(monthAndApplicationAmount.values());
        var disp = getDispersion(average, monthAndApplicationAmount.values());
        return Collections.singletonList(Statistics.builder().average(average).dispersion(disp).build());
    }

    protected double getDispersion(double avg,
                                 Collection<Long> applicationAmountInMonth
    ) {
        var sum = applicationAmountInMonth.stream().map(
                        l -> Math.pow((l - avg), 2)
                )
                .reduce(0.0, Double::sum);
        return sum / (applicationAmountInMonth.size());

    }
}
