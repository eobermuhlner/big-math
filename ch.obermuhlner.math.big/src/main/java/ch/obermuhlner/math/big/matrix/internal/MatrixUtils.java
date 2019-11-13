package ch.obermuhlner.math.big.matrix.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
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

    public static BigDecimal add(BigDecimal left, BigDecimal right, MathContext mathContext) {
        if (mathContext == null) {
            return left.add(right);
        } else {
            return left.add(right, mathContext);
        }
    }

    public static BigDecimal subtract(BigDecimal left, BigDecimal right, MathContext mathContext) {
        if (mathContext == null) {
            return left.subtract(right);
        } else {
            return left.subtract(right, mathContext);
        }
    }

    public static BigDecimal multiply(BigDecimal left, BigDecimal right, MathContext mathContext) {
        if (mathContext == null) {
            return left.multiply(right);
        } else {
            return left.multiply(right, mathContext);
        }
    }

    public static void checkRows(int rows) {
        if (rows < 0 ) {
            throw new IllegalArgumentException("rows < 0 : " + rows);
        }
    }

    public static void checkColumns(int columns) {
        if (columns < 0 ) {
            throw new IllegalArgumentException("columns < 0 : " + columns);
        }
    }

    public static void checkSquare(BigMatrix matrix) {
        if (matrix.columns() != matrix.rows()) {
            throw new IllegalArgumentException("columns " + matrix.columns() + " != rows " + matrix.rows());
        }
    }

    public static void checkRow(BigMatrix matrix, int row) {
        checkRow(matrix, "row", row);
    }

    public static void checkRow(BigMatrix matrix, String name, int row) {
        if (row < 0 ) {
            throw new IllegalArgumentException(name + " < 0 : " + row);
        }
        if (row >= matrix.rows()) {
            throw new IllegalArgumentException(name + " >= " + matrix.rows() + " : " + row);
        }
    }

    public static void checkColumn(BigMatrix matrix, int column) {
        checkColumn(matrix, "column", column);
    }

    public static void checkColumn(BigMatrix matrix, String name, int column) {
        if (column < 0 ) {
            throw new IllegalArgumentException(name + " < 0 : " + column);
        }
        if (column >= matrix.columns()) {
            throw new IllegalArgumentException(name + " >= " + matrix.columns() + " : " + column);
        }
    }

    public static void checkSameSize(BigMatrix matrix, BigMatrix other) {
        if (matrix.rows() != other.rows()) {
            throw new IllegalArgumentException("rows != other.rows : " + matrix.rows() + " != " + other.rows());
        }
        if (matrix.columns() != other.columns()) {
            throw new IllegalArgumentException("columns != other.columns : " + matrix.columns() + " != " + other.columns());
        }
    }

    public static void checkColumnsOtherRows(BigMatrix matrix, BigMatrix other) {
        if (matrix.columns() != other.rows()) {
            throw new IllegalArgumentException("columns != other.rows : " + matrix.columns() + " != " + other.rows());
        }
    }
}
