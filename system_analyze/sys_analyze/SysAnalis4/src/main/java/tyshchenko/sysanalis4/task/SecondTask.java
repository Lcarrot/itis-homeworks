package tyshchenko.sysanalis4.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tyshchenko.sysanalis4.model.Statistics;
import tyshchenko.sysanalis4.util.DateCsvReader;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static tyshchenko.sysanalis4.math.MathFormulas.getAverage;

@Component
public class SecondTask implements Task {

    @Autowired
    private DateCsvReader dateCsvReader;

    @Override
    public List<Statistics> execute() {
        var dates = dateCsvReader.getDates();
        Collection<Long> dayOfMonthAndAmountOfApplications = dates.stream()
                .filter(t -> t.getYear() == 2020)
                .filter(t -> t.getMonth() == Month.OCTOBER)
                .collect(Collectors.groupingBy(LocalDateTime::getDayOfMonth, Collectors.counting())).values();
        var average = getAverage(dayOfMonthAndAmountOfApplications);
        var dispersion = getDispersion(average, dayOfMonthAndAmountOfApplications);
        return Collections.singletonList(Statistics.builder().average(average).dispersion(dispersion).build());
    }

    protected double getDispersion(double avg, Collection<Long> applicationAmountInDays) {
        var sum = applicationAmountInDays.stream().map(
                        l -> Math.pow((l - avg), 2)
                )
                .reduce(0.0, Double::sum);
        return sum / (applicationAmountInDays.size());
    }
}
