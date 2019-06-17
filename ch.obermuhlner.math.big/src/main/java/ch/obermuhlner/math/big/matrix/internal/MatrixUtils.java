package ch.obermuhlner.math.big.matrix.internal;

import java.math.BigDecimal;

public class MatrixUtils {
    public static int countZeroValues(BigDecimal[] data) {
        int count = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i].signum() == 0) {
                count++;
            }
        }
        return count;
    }

    public static int countZeroValues(double[] data) {
        int count = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0.0) {
                count++;
            }
        }
        return count;
    }
}
