package tyshchenko.sysanalis4.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tyshchenko.sysanalis4.model.Statistics;
import tyshchenko.sysanalis4.util.DateCsvReader;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static tyshchenko.sysanalis4.math.MathFormulas.getAverage;

@Component
public class FourthTask implements Task {

    @Autowired
    private DateCsvReader dateCsvReader;

    @Override
    public List<Statistics> execute() {
        var dates = dateCsvReader.getDates();
        List<Long> forOctober = countApplicationsForMonth(dates, Month.OCTOBER);
        List<Long> forSeptember = countApplicationsForMonth(dates, Month.SEPTEMBER);
        List<Long> forDecember = countApplicationsForMonth(dates, Month.DECEMBER);
        List<Statistics> answer = new ArrayList<>();
        var average = getAverage(forOctober);
        answer.add(Statistics.builder().average(average)
                .dispersion(getDispersion(average, forOctober)).build());
        getDispersion(getAverage(forOctober), forOctober);

        average = getAverage(forSeptember);
        answer.add(Statistics.builder().average(average)
                .dispersion(getDispersion(average, forSeptember)).build());

        average = getAverage(forDecember);
        answer.add(Statistics.builder().average(average)
                .dispersion(getDispersion(average, forDecember)).build());
        return answer;
    }

    private List<Long> countApplicationsForMonth(List<LocalDateTime> dates, Month month) {
        List<Long> amountOfApplicationForMonth = new ArrayList<>();

        LocalDateTime lastDayOfMonth = dates.stream()
                .filter(t -> t.getMonth() == month)
                .map(t -> t.withDayOfMonth(t.getMonth().length(t.toLocalDate().isLeapYear())))
                .findFirst().get();

        LocalDateTime firstDayOfMonth = dates.stream()
                .filter(t -> t.getMonth() == month && t.getDayOfMonth() == 1)
                .findFirst().get();

        LocalDateTime firstSundayOfMonth = dates.stream()
                .filter(t -> t.getMonth() == month && t.getDayOfWeek() == DayOfWeek.SUNDAY)
                .findFirst().get();

        final AtomicReference<LocalDateTime> end = new AtomicReference<>(firstSundayOfMonth);
        final AtomicReference<LocalDateTime> start = new AtomicReference<>(firstDayOfMonth);

        while (!start.get().isAfter(lastDayOfMonth)) {
            long amountOfApplicationForWeek = dates.stream()
                    .filter(t -> t.getYear() == 2020 && t.getMonth() == month)
                    .filter(t ->
                            t.isAfter(start.get().minus(1, ChronoUnit.DAYS)) &&
                                    t.isBefore(end.get().plus(1, ChronoUnit.DAYS))
                    ).count();

            start.set(end.get().plus(1, ChronoUnit.DAYS));
            end.set(end.get().plus(7, ChronoUnit.DAYS));
            end.set(end.get().isAfter(lastDayOfMonth) ? lastDayOfMonth : end.get());

            amountOfApplicationForMonth.add(amountOfApplicationForWeek);
        }
        return amountOfApplicationForMonth;
    }

    protected double getDispersion(double avg, Collection<Long> applicationAmountInWhatever) {
        var sum = applicationAmountInWhatever.stream().map(
                        l -> Math.pow((l - avg), 2)
                )
                .reduce(0.0, Double::sum);
        return sum / (applicationAmountInWhatever.size());
    }
}
