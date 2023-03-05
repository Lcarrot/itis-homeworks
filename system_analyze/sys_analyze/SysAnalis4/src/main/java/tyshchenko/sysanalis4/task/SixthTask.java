package tyshchenko.sysanalis4.task;

import org.springframework.beans.factory.annotation.Autowired;
import tyshchenko.sysanalis4.model.Statistics;
import tyshchenko.sysanalis4.util.DateCsvReader;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static tyshchenko.sysanalis4.math.MathFormulas.getAverage;

public class SixthTask implements Task {

    @Autowired
    private DateCsvReader dateCsvReader;

    @Override
    public List<Statistics> execute() {
        var dates = dateCsvReader.getDates();
        Map<Month, Long> applications = dates.stream()
                .collect(Collectors.groupingBy(LocalDateTime::getMonth, Collectors.counting()));
        var average = getAverage(applications.values());
        var dispersion = getDispersion(average, applications.values());
        return Collections.singletonList(Statistics.builder().average(average).dispersion(dispersion).build());
    }

    protected double getDispersion(double avg, Collection<Long> applicationAmountInWhatever) {
        var sum = applicationAmountInWhatever.stream().map(
                        l -> Math.pow((l - avg), 2)
                )
                .reduce(0.0, Double::sum);

        return sum / (applicationAmountInWhatever.size());

    }
}
