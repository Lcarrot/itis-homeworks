package tyshchenko.sysanalis4.util;

import com.opencsv.CSVReader;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class DateCsvReader {

    private final List<LocalDateTime> timeList = new ArrayList<>();
    private final ResourceLoader resourceLoader;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DateCsvReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    void init() {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader
                (resourceLoader.getResource("classpath:ddd.csv").getInputStream()))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                String splitVal = values[0].split("[.]+")[0];
                timeList.add(LocalDateTime.from(DATE_TIME_FORMATTER.parse(splitVal)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        timeList.sort(Comparator.naturalOrder());
    }

    public List<LocalDateTime> getDates() {
        return new ArrayList<>(timeList);
    }
}
