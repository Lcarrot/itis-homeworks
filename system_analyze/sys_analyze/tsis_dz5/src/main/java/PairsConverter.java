import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PairsConverter {

    public static void main(String[] s) {
        try {
            List<String> pairJPY_RUB = new ArrayList<>();
            List<String> pairEUR_JPY = Files.readAllLines(Paths.get("pairs/EURUZS_220801_221030.csv"));
            List<String> pairEUR_RUB = Files.readAllLines(Paths.get("pairs/EURRUB_220801_221030.csv"));

            pairJPY_RUB.add("<TICKER>;<PER>;<DATE>;<TIME>;<OPEN>;<HIGH>;<LOW>;<CLOSE>;<VOL>");

            for (int i = 1; i < Math.min(pairEUR_JPY.size(), pairEUR_RUB.size()); i++) {
                String[] p_ey = pairEUR_JPY.get(i).split(";");
                String[] p_er = pairEUR_RUB.get(i).split(";");

                double open_ar = Double.parseDouble(p_er[4]) / Double.parseDouble(p_ey[4]);
                double close_ar = Double.parseDouble(p_er[7]) / Double.parseDouble(p_ey[7]);
                pairJPY_RUB.add("UZSRUB;D;"+p_er[2] +";0;" + String.format("%.7f",open_ar).replaceAll(",", ".")+";"+
                        ";"+
                        ";"+
                        String.format("%.7f",close_ar).replaceAll(",", ".")+";");

                Files.write(Paths.get("pairs/UZSRUB_220801_221030.csv"),pairJPY_RUB);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
