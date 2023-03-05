package com.example.nn;

import com.example.dto.AIData;
import lombok.SneakyThrows;

import java.io.*;
import java.util.List;

public class NeuralNetwork {

    private double w11, w12, w21, w22, v11, v12, v13, v21, v22, v23, w1, w2, w3;
    private double e;

    private static double x1[] = new double[100];
    private static double x2[] = new double[100];
    private static double y[] = new double[100];

    static {
        readTestData100();
    }

    public NeuralNetwork(double w11, double w12, double w21, double w22, double v11, double v12, double v13, double v21, double v22, double v23, double w1, double w2, double w3) {
        this.w11 = w11;
        this.w12 = w12;
        this.w21 = w21;
        this.w22 = w22;
        this.v11 = v11;
        this.v12 = v12;
        this.v13 = v13;
        this.v21 = v21;
        this.v22 = v22;
        this.v23 = v23;
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
    }

    public NeuralNetwork(AIData model) throws NumberFormatException {
        init(model);
    }

    public NeuralNetwork() {
    }

    public void init(AIData model) throws NumberFormatException {
        this.w11 = Double.parseDouble(model.getW11());
        this.w12 = Double.parseDouble(model.getW12());
        this.w21 = Double.parseDouble(model.getW21());
        this.w22 = Double.parseDouble(model.getW22());
        this.v11 = Double.parseDouble(model.getV11());
        this.v12 = Double.parseDouble(model.getV12());
        this.v13 = Double.parseDouble(model.getV13());
        this.v21 = Double.parseDouble(model.getV21());
        this.v22 = Double.parseDouble(model.getV22());
        this.v23 = Double.parseDouble(model.getV23());
        this.w1 = Double.parseDouble(model.getW1());
        this.w2 = Double.parseDouble(model.getW2());
        this.w3 = Double.parseDouble(model.getW3());
    }

    private double f(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double g(double x1, double x2) {
        double h11 = f(x1 * w11 + x2 * w21);
        double h12 = f(x1 * w12 + x2 * w22);
        return f(f(h11 * v11 + h12 * v21) * w1 + f(h11 * v12 + h12 * v22) * w2 + f(h11 * v13 + h12 * v23) * w3);
    }

    public double e() {
        double res = 0;
        for (int i = 0; i < 100; i++) {
            double yt = g(x1[i], x2[i]);
            res += (yt - y[i]) * (yt - y[i]);
        }
        return res;
    }

    @SneakyThrows
    private static void readTestData100() {
        List<String> lstData;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("D:\\projects\\PuckChain\\PuckChain\\src\\main\\resources\\test_data_100.csv")))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] s = line.split(";");
                x1[i] = Double.parseDouble(s[0]);
                x2[i] = Double.parseDouble(s[1]);
                y[i] = Double.parseDouble(s[2]);
                //System.out.println(x1[i] +";" + x2[i] +";" + y[i]);
                i++;
            }
        } catch (IOException | NumberFormatException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        /*
        {"prevhash":"8742e222c76666f1800665d7dc693ecde3ae3ddb8c7cd663379292b794a47a3f","data":
        {"e":"0.028838416435","publickey":"30819f300d06092a864886f70d010101050003818d0030818902818100a811365d2f3642952751029edf87c8fa2aeb6e0feafcf800190a7dd2cf750c63262f6abd8ef52b251c0e10291d5e2f7e6682de1aae1d64d4f9b242050f898744ca300a44c4d8fc8af0e7a1c7fd9b606d7bde304b29bec01fbef554df6ba1b7b1ec355e1ff68bd37f3d40fb27d1aa233fe3dd6b63f7241e734739851ce8c590f70203010001",
        "v21":"0.539763062386","w11":"0.626983568090","w22":"0.975011598497","v12":"0.068179673962","v23":"0.044663739162",
        "w21":"0.789741745355","v11":"0.456878575526","v22":"0.219327808802","w12":"0.389684979191","v13":"0.148925601792",
        "w1":"0.012316998339","w2":"0.025533448980","w3":"0.487270385485"},"info":"Egorova Anastasiia 11-805"}
         */


//        NeuralNetwork nn = new NeuralNetwork(0.11, 0.21, 0.22, 1.4, 2.0, 0.0002, 2.0056, 0.017,0.934, 1.5, 1.0047, 0.12, 1.32);
//        DataModel dm = new DataModel("1.11", "0.11", "0.23", "1.41", "1.94", "0.0003", "2.016", "0.007", "0.904", "1.58", "1.017", "0.102", "1.2","","");
        AIData dm = new AIData("0.21922459", "-1.6558539",
                "1.3770432", "0.22681728",
                "-1.2470318", "-0.01854472", "0.8405161",
                "0.21534312", "-0.7193887", "-0.2913953",
                "-0.6445387", "0.63749385", "0.7654909", "7.69336811606514E-6", "30820122300d06092a864886f70d01010105000382010f003082010a0282010100daaaea0f96d5d6149677f81e84af7417bdeefc254c2a9f202c8ee535afbdab2e1a30963143090e8151e2db8a2eeb119197fab6cc7efdfe73bca9600012c8f9515de994486207ffecab773af79191ae7a411d6fe33d7dc7f4a2d6cbfaefdcc67fa58908ec2d24508c11a23f388d056343cb2a85ea97f314a2b9473b56dded856c4e8250cc6937bf31a187db52066807b123fb612f76d72fed1ee91b8a2aaa756b3bf0e9640488f35941076217576a591dbede1e2176f695b18d71ad10dd4b26685d3efe17ce0baafcb8f353fe8f0e593b8cefd825d02385ba45ed4172b4232bf95bb498b1486a5c780b5e0eb22eea75b1b51ed207c95f27d7907afe3ff6df76cf0203010001");
        System.out.println(dm);
        System.out.println(dm.getAsString());
        //        NeuralNetwork nn = new NeuralNetwork(1.11, 0.11, 0.23, 1.41, 1.94, 0.0003, 2.016, 0.007,0.904, 1.58, 1.017, 0.102, 1.2);
        NeuralNetwork nn = new NeuralNetwork(dm);
        //nn.readTestData100();
        System.out.println(nn.e());

    }
}
