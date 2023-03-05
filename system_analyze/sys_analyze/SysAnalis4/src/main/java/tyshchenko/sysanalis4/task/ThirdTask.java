package tyshchenko.sysanalis4.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tyshchenko.sysanalis4.model.Statistics;
import tyshchenko.sysanalis4.util.DateCsvReader;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static tyshchenko.sysanalis4.math.MathFormulas.getAverage;

@Component
public class ThirdTask implements Task {

    @Autowired
    private DateCsvReader dateCsvReader;

    @Override
    public List<Statistics> execute() {
        var dates = dateCsvReader.getDates();
        return Arrays.stream(DayOfWeek.values()).map(
                day -> {
                    var november = dates.stream()
                            .filter(t -> t.getYear() == 2020)
                            .filter(t -> t.getMonth() == Month.OCTOBER)
                            .filter(t -> t.getDayOfWeek() == day)
                            .collect(Collectors.groupingBy(LocalDateTime::toLocalDate, Collectors.counting()));
                    var december = dates.stream()
                            .filter(t -> t.getYear() == 2020)
                            .filter(t -> t.getMonth() == Month.NOVEMBER)
                            .filter(t -> t.getDayOfWeek() == day)
                            .collect(Collectors.groupingBy(LocalDateTime::toLocalDate, Collectors.counting()));
                    var january = dates.stream()
                            .filter(t -> t.getYear() == 2020)
                            .filter(t -> t.getMonth() == Month.DECEMBER)
                            .filter(t -> t.getDayOfWeek() == day)
                            .collect(Collectors.groupingBy(LocalDateTime::toLocalDate, Collectors.counting()));

                    List<Long> counts = Stream.of(november, december, january).map(Map::values).flatMap(Collection::stream).toList();

                    var average = getAverage(counts);
                    var dispersion = getDispersion(average, counts);
                    getDispersion(average, counts);
                    return Statistics.builder().average(average).dispersion(dispersion).build();
                }
        ).collect(Collectors.toList());
    }

    protected double getDispersion(double avg, List<Long> applicationAmountInDay) {
        var sum = applicationAmountInDay.stream().map(
                        l -> Math.pow((l - avg), 2)
                )
                .reduce(0.0, Double::sum);

        return sum / (applicationAmountInDay.size());
    }
}
