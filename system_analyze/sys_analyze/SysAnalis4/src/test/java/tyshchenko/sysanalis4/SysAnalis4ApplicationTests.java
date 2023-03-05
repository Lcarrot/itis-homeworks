package tyshchenko.sysanalis4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tyshchenko.sysanalis4.model.Statistics;
import tyshchenko.sysanalis4.task.Task;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import static tyshchenko.sysanalis4.math.MathFormulas.findRejectP;

@SpringBootTest
class SysAnalis4ApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(SysAnalis4ApplicationTests.class);

    @Autowired
    private List<Task> taskList;

    private Statistics statistics;

    @BeforeEach
    void before() {
        statistics = taskList.stream().map(Task::execute).flatMap(Collection::stream)
                .min(Comparator.comparing(Statistics::getDispersion, Comparator.naturalOrder())).orElseThrow();
    }

    @Test
    void test() {
        double cost = 2400;
        var n1 = 20;
        var n2 = 21;
        double p_rej_1 = findRejectP(n1, statistics);
        double p_rej_2 = findRejectP(n2, statistics);
        double x = cost / (statistics.getAverage() * (p_rej_1 - p_rej_2));
        logger.warn("x = {}", x);
        double x2 = x * 3;
        double prev = cost + statistics.getAverage() * findRejectP(1, statistics) * x2;
        double best_thread = 1;
        for (int i = 2; i < 200; i++) {
            double p_rej = findRejectP(i, statistics);
            double price = i * cost + statistics.getAverage() * p_rej * x2;
            if (price < prev) {
                prev = price;
                best_thread = i;
            }
        }
        logger.warn("Оптимальное количество = {}", best_thread);
    }
}
