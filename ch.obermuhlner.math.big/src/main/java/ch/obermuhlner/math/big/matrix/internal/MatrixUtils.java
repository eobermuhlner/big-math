package ch.obermuhlner.math.big.matrix.internal;

import java.math.BigDecimal;
import java.util.function.BiFunction;

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

    public static boolean atLeastZeroValues(int minCount, BigDecimal[] data) {
        int count = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i].signum() == 0) {
                count++;
                if (count >= minCount) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean atLeastZeroValues(int minCount, int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        int count = 0;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (valueFunction.apply(row, column).signum() == 0) {
                    count++;
                    if (count >= minCount) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static BigDecimal[] toBigDecimal(double... values) {
        BigDecimal[] result = new BigDecimal[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = BigDecimal.valueOf(values[i]);
        }

        return result;
    }
}
