package tyshchenko.sysanalis4.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tyshchenko.sysanalis4.model.Statistics;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.List;

public class MathFormulas {

    private static final MathContext cont = MathContext.DECIMAL128;
    private static final Logger logger = LoggerFactory.getLogger(MathFormulas.class);

    public static long getFact(int f) {
        long result = 1;
        for (int i = 2; i <= f; i++) {
            result *= i;
        }
        return result;
    }

    public static double findRejectP(int n, Statistics statistics) {
        logger.info("min dispersion is {}", statistics.getDispersion());
        logger.info("average is {}", statistics.getAverage());
        BigDecimal l = BigDecimal.valueOf(statistics.getAverage())
                .divide(BigDecimal.valueOf(statistics.getDispersion()), cont);
        BigDecimal t_accept = BigDecimal.valueOf(2.3);
        BigDecimal t_wait = BigDecimal.valueOf(30);
        BigDecimal m = BigDecimal.ONE.divide(t_accept, cont);
        BigDecimal ro = l.divide(m, cont);
        double p0 = 0;
        BigDecimal b = l.multiply(t_wait);
        var a = BigDecimal.valueOf(n).divide(ro, cont);
        double c = Math.exp(b.multiply(BigDecimal.ZERO.subtract(a)).doubleValue());
        double B = (c - 1) / (1 - a.doubleValue());
        for (int i = 0; i < n; i++) {
            p0 += Math.pow(ro.doubleValue(), i) / getFact(i);
        }
        p0 += (Math.pow(ro.doubleValue(), n) / getFact(n)) * B;
        p0 = 1 / p0;
        logger.info("Все каналы свободны с вероятностью {}", p0);
        double p_reject = (Math.pow(ro.doubleValue(), n) / getFact(n)) * c * p0;
        logger.info("Вероятность отказа {}", p_reject);
        double D = 1 + b.doubleValue() / 2;
        double W0 = t_accept.doubleValue() * Math.pow(ro.doubleValue(), n) * D * p0 / getFact(n);
        logger.info("Среднее время в очереди {}", W0);
        double Q = 1 - p_reject;
        logger.info("Q = {}", Q);
        double lambda_eff = l.doubleValue() * Q;
        logger.info("lambda_eff {}", lambda_eff);
        double lambda_otk = l.doubleValue() * p_reject;
        logger.info("lambda_otk {}", lambda_otk);
        return p_reject;
    }

    public static double getAverage(Collection<Long> amount) {
        return (double) amount.stream()
                .reduce(0L, Long::sum) / amount.size();
    }
}
