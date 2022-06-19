import java.math.BigInteger;
import java.util.Arrays;

public class Karatsuba {


    public BigInteger divideWithKaratsubaAlgorithm(BigInteger first, BigInteger second) {
        boolean[] firstBool = convertFromBigIntegerToBoolArray(first);
        boolean[] secondBool = convertFromBigIntegerToBoolArray(second);
        int n = Math.max(firstBool.length, secondBool.length);
        if (n <= 64) return first.multiply(second);
        int divider = (int)Math.pow(2, Math.round(n/2));
        boolean[] firstLeft = divArrayNumber(firstBool, divider);
        boolean[] firstRight = modArrayNumber(firstBool, divider);
        boolean[] secondLeft = divArrayNumber(secondBool, divider);
        boolean[] secondRight = modArrayNumber(secondBool, divider);
        BigInteger p1 = divideWithKaratsubaAlgorithm(convertFromBoolArrayToBigInteger(firstLeft), convertFromBoolArrayToBigInteger(secondLeft));
        BigInteger thirdInKaratsubaFormula = divideWithKaratsubaAlgorithm(convertFromBoolArrayToBigInteger(firstRight), convertFromBoolArrayToBigInteger(secondRight));
        BigInteger p3 = divideWithKaratsubaAlgorithm(convertFromBoolArrayToBigInteger(sumBoolArray(firstLeft, firstRight)),
                convertFromBoolArrayToBigInteger(sumBoolArray(secondLeft, secondRight)));
        BigInteger firstInKaratsubaFormula = p1.multiply(BigInteger.valueOf((int) Math.pow(2, n)));
        BigInteger secondInKaratsubaFormula = p3.subtract(thirdInKaratsubaFormula.add(p1)).multiply(BigInteger.valueOf((int)Math.pow(2, Math.round(n/2))));
        return firstInKaratsubaFormula.add(secondInKaratsubaFormula).add(thirdInKaratsubaFormula);
    }

    private boolean[] convertFromBigIntegerToBoolArray(BigInteger number) {
        char[] binaryChars = number.toString(2).toCharArray();
        boolean[] binaryBool = new boolean[binaryChars.length];
        for (int i = 0; i < binaryChars.length; i++) {
            binaryBool[i] = binaryChars[i] != '0';
        }
        return binaryBool;
    }

    private boolean[] divArrayNumber(boolean[] dividend, int divider) {
        return Arrays.copyOfRange(dividend, 0, dividend.length - Integer.toBinaryString(divider).length() + 1);
    }

    private boolean[] modArrayNumber(boolean[] dividend, int divider) {
        return Arrays.copyOfRange(dividend, dividend.length - Integer.toBinaryString(divider).length() + 1, dividend.length);
    }

    private BigInteger convertFromBoolArrayToBigInteger(boolean[] boolNumber) {
        StringBuilder stringBuilderNumber = new StringBuilder();
        for (boolean b : boolNumber) {
            char binaryNumber = b ? '1' : '0';
            stringBuilderNumber.append(binaryNumber);
        }
        return new BigInteger(stringBuilderNumber.toString(), 2);
    }

    private boolean[] sumBoolArray(boolean[] first, boolean[] second) {
        BigInteger firstNumber = convertFromBoolArrayToBigInteger(first);
        BigInteger secondNumber = convertFromBoolArrayToBigInteger(second);
        BigInteger sumResult = firstNumber.add(secondNumber);
        return convertFromBigIntegerToBoolArray(sumResult);
    }
}



