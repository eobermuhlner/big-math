package ch.obermuhlner.math.big.matrix;

import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.matrix.internal.dense.DenseMutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.sparse.SparseMutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

public interface MutableBigMatrix extends BigMatrix {
    void set(int row, int column, BigDecimal value);

    default void set(int row, int column, BigMatrix matrix) {
        for (int sourceRow = 0; sourceRow < matrix.rows(); sourceRow++) {
            for (int sourceColumn = 0; sourceColumn < matrix.columns(); sourceColumn++) {
                BigDecimal value = matrix.get(sourceRow, sourceColumn);
                set(row + sourceRow, column + sourceColumn, value);
            }
        }
    }

    default void fill(BigDecimal value) {
        for (int row = 0; row < rows(); row++) {
            for (int col = 0; col < columns(); col++) {
                set(row, col, value);
            }
        }
    }

    default void clear() {
        fill(BigDecimal.ZERO);
    }

    MutableBigMatrix subMatrix(int startRow, int startColumn, int rows, int columns);

    MutableBigMatrix add(BigMatrix other, MathContext mathContext);
    MutableBigMatrix subtract(BigMatrix other, MathContext mathContext);
    MutableBigMatrix multiply(BigDecimal value, MathContext mathContext);
    MutableBigMatrix multiply(BigMatrix other, MathContext mathContext);

    MutableBigMatrix transpose();

    default void addToThis(BigMatrix other, MathContext mathContext) {
        if (rows() != other.rows()) {
            throw new ArithmeticException("rows: " + rows() + " != " + other.rows());
        }
        if (columns() != other.columns()) {
            throw new ArithmeticException("columns: " + columns() + " != " + other.columns());
        }

        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                set(row, column, get(row, column).add(other.get(row, column), mathContext));
            }
        }
    }

    default void subtractFromThis(BigMatrix other, MathContext mathContext) {
        if (rows() != other.rows()) {
            throw new ArithmeticException("rows: " + rows() + " != " + other.rows());
        }
        if (columns() != other.columns()) {
            throw new ArithmeticException("columns: " + columns() + " != " + other.columns());
        }

        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                set(row, column, get(row, column).subtract(other.get(row, column), mathContext));
            }
        }
    }

    default void multiplyToThis(BigDecimal value, MathContext mathContext) {
        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                set(row, column, get(row, column).multiply(value, mathContext));
            }
        }
    }

    MutableBigMatrix invert(MathContext mathContext);

    default void gaussianElimination(boolean reducedEchelonForm, MathContext mathContext) {
        int pivotRow = 0;
        int pivotColumn = 0;

        while (pivotRow < rows() && pivotColumn < columns()) {
            int maxRow = pivotRow;
            for (int row = pivotRow+1; row < rows(); row++) {
                if (get(row, pivotColumn).abs().compareTo(get(maxRow, pivotColumn)) > 0) {
                    maxRow = row;
                }
            }

            if (get(maxRow, pivotColumn).signum() == 0) {
                pivotColumn++;
            } else {
                swapRows(pivotRow, maxRow);
                BigDecimal divisor = get(pivotRow, pivotColumn);
                for (int row = pivotRow+1; row < rows(); row++) {
                    BigDecimal factor = get(row, pivotColumn).divide(divisor, mathContext);
                    set(row, pivotColumn, BigDecimal.ZERO);
                    for (int column = pivotColumn + 1; column < columns(); column++) {
                        BigDecimal value = get(row, column).subtract(get(pivotRow, column).multiply(factor, mathContext), mathContext);
                        set(row, column, value);
                    }
                }
            }

            if (reducedEchelonForm) {
                BigDecimal pivotDivisor = get(pivotRow, pivotColumn);
                set(pivotRow, pivotColumn, BigDecimal.ONE);
                for (int column = pivotColumn+1; column < columns(); column++) {
                    BigDecimal value = get(pivotRow, column).divide(pivotDivisor, mathContext);
                    set(pivotRow, column, value);
                }

                for (int row = 0; row < pivotRow; row++) {
                    BigDecimal factor = get(row, pivotColumn);
                    set(row, pivotColumn, BigDecimal.ZERO);
                    for (int column = pivotColumn+1; column < columns(); column++) {
                        BigDecimal value = get(row, column).subtract(get(pivotRow, column).multiply(factor, mathContext), mathContext);
                        set(row, column, value);
                    }
                }
            }

            pivotColumn++;
            pivotRow++;
        }
    }

    default void swapRows(int row1, int row2) {
        if (row1 == row2) {
            return;
        }

        for (int column = 0; column < columns(); column++) {
            BigDecimal tmp = get(row1, column);
            set(row1, column, get(row2, column));
            set(row2, column, tmp);
        }
    }

    static MutableBigMatrix matrix(int rows, int columns, BigDecimal... values) {
        int n = rows * columns;
        if (values.length - n > 10000 || (values.length > 10000 && MatrixUtils.countZeroValues(values) > 10000)) {
            return sparse(rows, columns, values);
        } else {
            return dense(rows, columns, values);
        }
    }

    static MutableBigMatrix dense(int rows, int columns, BigDecimal... values) {
        return new DenseMutableBigMatrix(rows, columns, values);
    }

    static MutableBigMatrix dense(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new DenseMutableBigMatrix(rows, columns, valueFunction);
    }

    static MutableBigMatrix denseIdentity(int rows, int columns) {
        return new DenseMutableBigMatrix(rows, columns, (row, column) -> {
            return row == column ? BigDecimal.ONE : BigDecimal.ZERO;
        });
    }

    static MutableBigMatrix sparse(int rows, int columns, BigDecimal... values) {
        return new SparseMutableBigMatrix(rows, columns, values);
    }

    static MutableBigMatrix sparse(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new SparseMutableBigMatrix(rows, columns, valueFunction);
    }

    static MutableBigMatrix sparseIdentity(int rows, int columns) {
        return new SparseMutableBigMatrix(rows, columns, (row, column) -> {
            return row == column ? BigDecimal.ONE : BigDecimal.ZERO;
        });
    }

}
