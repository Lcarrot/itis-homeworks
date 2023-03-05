import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {

    private static final String RESOURSES_PATH = "D:\\projects\\tsis\\tsis_dz5\\src\\main\\resources\\";

    static CurrencyData[] aud_usd;
    static CurrencyData[] cad_usd;
    static CurrencyData[] nok_usd;
    static CurrencyData[] rub_usd;

    // исходная сумма для вложения
    static double budget = 1000000;

    // кол-во дней в подвыборке
    static int portion_size = 5;

    static double A = 3000;

    // массив их 5 дневных (portion_size) выборок
    static Sample[] sampling;

    // пропорции валют в стратегиях
    static double[][] strategy = new double[][]{{0.1, 0.15, 0.15, 0.6}, {0.3, 0.3, 0.3, 0.1}, {0.6, 0.1, 0.1, 0.2}, {0.35, 0.35, 0.15, 0.15}};

    // размер всей исследуемой выборки
    static int sz;

    public static void main(String[] args) {
        // Загружаем данные
        InitData();
        // Строим выборку
        Sampling();

        MakeMatrix();
    }

    public static void Sampling() {
        // будем рассматривать выборки по portion_size дней из массива котировок
        // затем по ним фиксировать частоту появления событий при разных стратегиях
        sampling = new Sample[sz / portion_size];

        for (int i = 0; i < sz / portion_size; i++) { // цикл по подвыборкам, их количество = размер массива котировок / portion_size
            int l = i * portion_size; // индекс первого элемента подвыборки в общем массиве котировок

            // формируем и обсчитываем выборку из 5 (portion_size) дней
            // в цикле индекс очередной подвыборки формируется как [i/portion_size]
            sampling[i] = new Sample();

            // Расчитываем "прибыль"(profit) - разницу между пересчитанной в рублях валютной корзины по
            // курсам закрытия и исходной суммой для каждой стратегии
            // в i-ый день формируем корзину, в (i + portion_size)-ый день конвертируем в рубли
            for (int k = 0; k < 4; k++) { // цикл по стратегиям
                sampling[i].profit[k] = budget * (
                        strategy[k][0] * ((1 / aud_usd[l].open) * aud_usd[l + portion_size - 1].close - 1) +
                                strategy[k][1] * ((1 / cad_usd[l].open) * cad_usd[l + portion_size - 1].close - 1) +
                                strategy[k][2] * ((1 / nok_usd[l].open) * nok_usd[l + portion_size - 1].close - 1) +
                                strategy[k][3] * ((1 / rub_usd[l].open) * rub_usd[l + portion_size - 1].close - 1)
                );
            }

            // Для подсчета дисперсии по пропорциональной совокупности валют в корзине по каждой стратегии
            // сфомируем "курс" корзины на каждый день подвыборки из portion_size дней (для определенности, по цене закрытия)
            // как сумму произведений курсов валют на соответствующую пропорцию из стратегии
            // По такому "курсу" корзины для разных стратегий вычислим среднее и дисперсию
            double max_dispersion = 0; // переменная для сравнения дисперсий разных стратегий
            for (int k = 0; k < 4; k++) { // цикл по стратегиям
                // среднее
                double x_ = 0;
                for (int j = l; j < l + portion_size; j++) {
                    x_ = x_ + strategy[k][0] * aud_usd[j].close + strategy[k][1] * cad_usd[j].close +
                            strategy[k][2] * nok_usd[j].close + strategy[k][3] * rub_usd[j].close;
                }
                x_ = x_ / portion_size;

                // дисперсия
                double d = 0;
                for (int j = l; j < l + portion_size; j++) {
                    d = d + (strategy[k][0] * aud_usd[j].close + strategy[k][1] * cad_usd[j].close +
                            strategy[k][2] * nok_usd[j].close + strategy[k][3] * rub_usd[j].close - x_) *
                            (strategy[k][0] * aud_usd[j].close + strategy[k][1] * cad_usd[j].close +
                                    strategy[k][2] * nok_usd[j].close + strategy[k][3] * rub_usd[j].close - x_);
                }
                // зафиксируем дисперсию в структуре, хранящей разные параметры подвыборки
                sampling[i].d[k] = d / portion_size;

                // Проверим, не является ли стратегия наиболее рискованной
                if (sampling[i].d[k] > max_dispersion) {
                    max_dispersion = sampling[i].d[k];
                    sampling[i].risk_strategy = k;
                }
            }

            // Определим состояния "Природы" для стратегий
            for (int k = 0; k < 4; k++) { // цикл по стратегиям
                if (sampling[i].profit[k] > A && k == sampling[i].risk_strategy) {
                    sampling[i].environment_state[k] = 0;
                } else if (sampling[i].profit[k] < A && k == sampling[i].risk_strategy) {
                    sampling[i].environment_state[k] = 1;
                } else if (sampling[i].profit[k] > A && k != sampling[i].risk_strategy) {
                    sampling[i].environment_state[k] = 2;
                } else if (sampling[i].profit[k] < A && k != sampling[i].risk_strategy) {
                    sampling[i].environment_state[k] = 3;
                }
            }

            // распечатаем параметры подвыборки
            for (int k = 0; k < 4; k++) { // цикл по стратегиям
                System.out.println("s" + k + ": profit=" + sampling[i].profit[k] + " , dispersion=" + sampling[i].d[k] + ", environment_state=" + sampling[i].environment_state[k]);
            }
        }
    }

    /*
        Соберем значения в стратегиях с одинаковыми состояниями природы
        например:
        s0 вместе с состоянием  p0 встречается 0 раза
        s0 вместе с состоянием  p1 встречается 0 раза
        s0 вместе с состоянием  p2 встречается 5 раза
        s0 вместе с состоянием  p3 встречается 14 раза
        Тогда средний выигрыш s0 при условии состояния p0 составит 0 руб. с частотой 0
            средний выигрыш s0 при условии состояния p1 составит 0 руб. с частотой 0
            средний выигрыш s0 при условии состояния p2 составит (57419,073814245 / 5)=11483,814762849 руб. с частотой 5/19
            средний выигрыш s0 при условии состояния p3 составит (−60454,60621777 / 14)=−4318,186158412 руб. с частотой 14/19
     */
    public static void MakeMatrix() {

        System.out.println("----------------------------------------------------------------------------");

        for (int k = 0; k < 4; k++) {// перебираем стратегии
            double cp0 = 0;
            double cp1 = 0;
            double cp2 = 0;
            double cp3 = 0; // счетчики выпавших состояний природы при стратегии k
            double ss0 = 0;
            double ss1 = 0;
            double ss2 = 0;
            double ss3 = 0; // сумма выигрыша стратегии k при определенном состоянии природы

            for (int i = 0; i < sz / portion_size; i++) { // перебираем выборки и смотрим соответствие стратегии и состояния
                if (sampling[i].environment_state[k] == 0) {
                    cp0++;
                    ss0 += sampling[i].profit[k];
                } else if (sampling[i].environment_state[k] == 1) {
                    cp1++;
                    ss1 += sampling[i].profit[k];
                } else if (sampling[i].environment_state[k] == 2) {
                    cp2++;
                    ss2 += sampling[i].profit[k];
                } else if (sampling[i].environment_state[k] == 3) {
                    cp3++;
                    ss3 += sampling[i].profit[k];
                }
            }

            if (cp0 > 0) {
                ss0 = ss0 / cp0;
                cp0 = cp0 / (sz / portion_size);
            }
            if (cp1 > 0) {
                ss1 = ss1 / cp1;
                cp1 = cp1 / (sz / portion_size);
            }
            if (cp2 > 0) {
                ss2 = ss2 / cp2;
                cp2 = cp2 / (sz / portion_size);
            }
            if (cp3 > 0) {
                ss3 = ss3 / cp3;
                cp3 = cp3 / (sz / portion_size);
            }

            // Выводим строку матрицы
            System.out.println("s" + k + "|" + ss0 + "; " + cp0 + "|" + ss1 + "; " + cp1 + "|" + ss2 + "; " + cp2 + "|" + ss3 + "; " + cp3 + "|");
        }
    }

    public static void InitData() {
            /*
                File format
                <TICKER>;<PER>;<DATE>;<TIME>;<OPEN>;<HIGH>;<LOW>;<CLOSE>;<VOL>
             */
        try {
            List<String> pairAUD_USD = Files.readAllLines(Paths.get(RESOURSES_PATH + "AUDUSD_220711_221104.csv"));
            List<String> pairCAD_USD = Files.readAllLines(Paths.get(RESOURSES_PATH + "CADUSD_220711_221104.csv"));
            List<String> pairNOK_USD = Files.readAllLines(Paths.get(RESOURSES_PATH + "NOKUSD_220711_221104.csv"));
            List<String> pairRUB_USD = Files.readAllLines(Paths.get(RESOURSES_PATH + "RUBUSD_220711_221104.csv"));

            // Первая строка - заголовок
            sz = min(pairAUD_USD.size(), pairCAD_USD.size(), pairNOK_USD.size(), pairRUB_USD.size()) - 1;

            aud_usd = new CurrencyData[sz];
            cad_usd = new CurrencyData[sz];
            nok_usd = new CurrencyData[sz];
            rub_usd = new CurrencyData[sz];

            for (int i = 1; i < sz; i++) {
                String[] p_aud_usd = pairAUD_USD.get(i).split(",");
                String[] p_cad_usd = pairCAD_USD.get(i).split(",");
                String[] p_nok_usd = pairNOK_USD.get(i).split(",");
                String[] p_rub_usd = pairRUB_USD.get(i).split(",");
                aud_usd[i - 1] = new CurrencyData(parseDate(p_aud_usd[2]), Double.parseDouble(p_aud_usd[4]), Double.parseDouble(p_aud_usd[7]));
                cad_usd[i - 1] = new CurrencyData(parseDate(p_cad_usd[2]), Double.parseDouble(p_cad_usd[4]), Double.parseDouble(p_cad_usd[7]));
                nok_usd[i - 1] = new CurrencyData(parseDate(p_nok_usd[2]), Double.parseDouble(p_nok_usd[4]), Double.parseDouble(p_nok_usd[7]));
                rub_usd[i - 1] = new CurrencyData(parseDate(p_rub_usd[2]), Double.parseDouble(p_rub_usd[4]), Double.parseDouble(p_rub_usd[7]));
            }
        } catch (IOException | ParseException e) {
           throw new RuntimeException(e);
        }
    }
    static int min(int a1, int a2, int a3, int a4) {
        int x, y;
        return a1 < (y = a2 < (x = Math.min(a3, a4)) ? a2 : x) ? a1 : y;
    }

    static Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyyMMdd").parse(date);
    }
}

class Sample {
    // выборочная дисперсия по стратегии
    public double[] d = new double[4];
    // "выигрыш" для стратегии
    public double[] profit = new double[4];
    // индекс стратегии с максимальным риском (т.е. максимальной дисперсией)
    public int risk_strategy;
    // Состояния природы для стратегий.
    // Например, environment_state[0] - состояние природы для первой стратегии в этой конкретной выборке
    public int environment_state[] = new int[4];
}

class CurrencyData {

    public Date date;
    public double open;
    public double close;

    public CurrencyData(Date date, double open, double close) {
        this.date = date;
        this.open = open;
        this.close = close;
    }
}
