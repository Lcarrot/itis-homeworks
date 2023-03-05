package tyshchenko.sysanalis4.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tyshchenko.sysanalis4.model.Statistics;
import tyshchenko.sysanalis4.util.DateCsvReader;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tyshchenko.sysanalis4.math.MathFormulas.getAverage;

@Component
public class FirstTask implements Task {

    @Autowired
    private DateCsvReader dateCsvReader;

    @Override
    public List<Statistics> execute() {
        var dates = dateCsvReader.getDates();
        var firstDay = dates.stream()
                .filter(t -> t.getYear() == 2020)
                .filter(t -> t.getMonth() == Month.OCTOBER)
                .filter(localDateTime -> localDateTime.getDayOfMonth() == 1)
                .map(localDateTime -> localDateTime.getHour() + 1)
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));
        var secondDay = dates.stream()
                .filter(t -> t.getYear() == 2020)
                .filter(t -> t.getMonth() == Month.OCTOBER)
                .filter(localDateTime -> localDateTime.getDayOfMonth() == 2)
                .map(localDateTime -> localDateTime.getHour() + 25)
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));
        var thirdDay = dates.stream()
                .filter(t -> t.getYear() == 2020)
                .filter(t -> t.getMonth() == Month.OCTOBER)
                .filter(localDateTime -> localDateTime.getDayOfMonth() == 3)
                .map(localDateTime -> localDateTime.getHour() + 49)
                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));
        List<Long> counts = Stream.of(firstDay, secondDay, thirdDay).map(Map::values).flatMap(Collection::stream).toList();
        double average = getAverage(counts);
        double dispersion = getDispersion(average, counts);
        return Collections.singletonList(Statistics.builder().average(average).dispersion(dispersion).build());
    }

    protected double getDispersion(double avg, Collection<Long> applicationAmountInHours) {
        var sum = applicationAmountInHours.stream()
                .map(l -> Math.pow((l - avg), 2))
                .reduce(0.0, Double::sum);
        return sum / (applicationAmountInHours.size());
    }
}
