package ch.obermuhlner.math.big.matrix.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.CoordValue;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

import static java.math.BigDecimal.ONE;

public class MatrixUtils {

    private static final int THRESHOLD_ELEMENT_COUNT = 100000;

    private static final double THRESHOLD_EMPTY_RATIO = 0.9;

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

    public static BigDecimal pow(BigDecimal x, long y) {
        BigDecimal result = ONE;
        while (y > 0) {
            if ((y & 1) == 1) {
                // odd exponent -> multiply result with x
                result = result.multiply(x);
                y -= 1;
            }

            if (y > 0) {
                // even exponent -> square x
                x = x.multiply(x);
            }

            y >>= 1;
        }
        return result;
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
        checkRow(matrix.rows(), name, row);
    }

    public static void checkRow(int rows, int row) {
        checkRow(rows, "row", row);
    }

    public static void checkRow(int rows, String name, int row) {
        if (row < 0 ) {
            throw new IllegalArgumentException(name + " < 0 : " + row);
        }
        if (row >= rows) {
            throw new IllegalArgumentException(name + " >= " + rows + " : " + row);
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

    public static boolean preferSparseMatrix(int rows, int columns, BigDecimal... values) {
        int n = rows * columns;
        return values.length - n >= THRESHOLD_ELEMENT_COUNT || (values.length > THRESHOLD_ELEMENT_COUNT && MatrixUtils.atLeastZeroValues(THRESHOLD_ELEMENT_COUNT, values));
    }

    public static boolean preferSparseMatrix(int rows, int columns, CoordValue... values) {
        int n = rows * columns;
        return values.length - n >= THRESHOLD_ELEMENT_COUNT || (values.length > THRESHOLD_ELEMENT_COUNT && MatrixUtils.atLeastZeroValues(THRESHOLD_ELEMENT_COUNT, values));
    }

    public static boolean preferSparseMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        int n = rows * columns;
        return n >= THRESHOLD_ELEMENT_COUNT && MatrixUtils.atLeastZeroValues(THRESHOLD_ELEMENT_COUNT, rows, columns, valueFunction);
    }

    private static boolean atLeastZeroValues(int minCount, BigDecimal[] data) {
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

    private static boolean atLeastZeroValues(int minCount, CoordValue[] data) {
        int count = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i].value.signum() == 0) {
                count++;
                if (count >= minCount) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean atLeastZeroValues(int minCount, int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
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

    public static boolean preferSparseMatrix(BigMatrix matrix, int thresholdElementCount, double thresholdEmptyRatio) {
        return matrix.elementCount() > thresholdElementCount && matrix.sparseEmptyRatio() > thresholdEmptyRatio;
    }

    public static boolean isSparseWithLotsOfZeroes(BigMatrix matrix) {
        return matrix.getSparseDefaultValue().signum() == 0 && matrix.sparseEmptyRatio() > THRESHOLD_EMPTY_RATIO;
    }
}
