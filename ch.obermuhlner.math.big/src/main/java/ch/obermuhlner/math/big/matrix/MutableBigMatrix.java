package ch.obermuhlner.math.big.matrix;

import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.matrix.internal.dense.DenseMutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.sparse.SparseMutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

import static java.math.BigDecimal.valueOf;

public interface MutableBigMatrix extends BigMatrix {
    void set(int row, int column, BigDecimal value);

    default void set(int row, int column, double value) {
        set(row, column, valueOf(value));
    }

    default void set(int row, int column, BigMatrix matrix) {
        for (int sourceRow = 0; sourceRow < matrix.rows(); sourceRow++) {
            for (int sourceColumn = 0; sourceColumn < matrix.columns(); sourceColumn++) {
                BigDecimal value = matrix.get(sourceRow, sourceColumn);
                set(row + sourceRow, column + sourceColumn, value);
            }
        }
    }

    default void set(CoordValue... values) {
        for (CoordValue value : values) {
            set(value.coord.row, value.coord.column, value.value);
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

    default void gaussianElimination(boolean reducedEchelonForm, MathContext mathContext) {
        int pivotRow = 0;
        int pivotColumn = 0;

        while (pivotRow < rows() && pivotColumn < columns()) {
            int maxRow = pivotRow;
            for (int row = pivotRow + 1; row < rows(); row++) {
                if (get(row, pivotColumn).abs().compareTo(get(maxRow, pivotColumn)) > 0) {
                    maxRow = row;
                }
            }

            BigDecimal pivotCell = get(maxRow, pivotColumn);
            if (pivotCell.signum() == 0) {
                pivotColumn++;
            } else {
                swapRows(pivotRow, maxRow);
                BigDecimal divisor = get(pivotRow, pivotColumn);
                for (int row = pivotRow + 1; row < rows(); row++) {
                    BigDecimal factor = get(row, pivotColumn).divide(divisor, mathContext);
                    set(row, pivotColumn, BigDecimal.ZERO);
                    for (int column = pivotColumn + 1; column < columns(); column++) {
                        BigDecimal value = get(row, column).subtract(get(pivotRow, column).multiply(factor, mathContext), mathContext).stripTrailingZeros();
                        set(row, column, value);
                    }
                }
            }

            if (reducedEchelonForm) {
                BigDecimal pivotDivisor = get(pivotRow, pivotColumn);
                set(pivotRow, pivotColumn, BigDecimal.ONE);
                for (int column = pivotColumn + 1; column < columns(); column++) {
                    BigDecimal value = get(pivotRow, column).divide(pivotDivisor, mathContext).stripTrailingZeros();
                    set(pivotRow, column, value);
                }

                for (int row = 0; row < pivotRow; row++) {
                    BigDecimal factor = get(row, pivotColumn);
                    set(row, pivotColumn, BigDecimal.ZERO);
                    for (int column = pivotColumn + 1; column < columns(); column++) {
                        BigDecimal value = get(row, column).subtract(get(pivotRow, column).multiply(factor, mathContext), mathContext).stripTrailingZeros();
                        set(row, column, value);
                    }
                }
            }

            pivotColumn++;
            pivotRow++;
        }
    }

    default void swapRows(int row1, int row2) {
        MatrixUtils.checkRow(this, "row1", row1);
        MatrixUtils.checkRow(this, "row2", row2);

        if (row1 == row2) {
            return;
        }

        for (int column = 0; column < columns(); column++) {
            BigDecimal tmp = get(row1, column);
            set(row1, column, get(row2, column));
            set(row2, column, tmp);
        }
    }

    default void swapColumns(int column1, int column2) {
        MatrixUtils.checkColumn(this, "column1", column1);
        MatrixUtils.checkColumn(this, "column2", column2);

        if (column1 == column2) {
            return;
        }

        for (int row = 0; row < columns(); row++) {
            BigDecimal tmp = get(row, column1);
            set(row, column1, get(row, column2));
            set(row, column2, tmp);
        }
    }

    static MutableBigMatrix matrix(BigMatrix matrix) {
        if (MatrixUtils.preferSparseMatrix(matrix, 1000, 0.5)) {
            return sparseMatrix(matrix);
        } else {
            return denseMatrix(matrix);
        }
    }

    static MutableBigMatrix matrix(int rows, int columns) {
        return matrix(rows, columns, new BigDecimal[0]);
    }

    static MutableBigMatrix matrix(int rows, int columns, double... values) {
        return matrix(rows, columns, MatrixUtils.toBigDecimal(values));
    }

    static MutableBigMatrix matrix(int rows, int columns, BigDecimal... values) {
        if (MatrixUtils.preferSparseMatrix(rows, columns, values)) {
            return sparseMatrix(rows, columns, values);
        } else {
            return denseMatrix(rows, columns, values);
        }
    }

    static MutableBigMatrix matrix(int rows, int columns, CoordValue... values) {
        if (MatrixUtils.preferSparseMatrix(rows, columns, values)) {
            return sparseMatrix(rows, columns, values);
        } else {
            return denseMatrix(rows, columns, values);
        }
    }

    static MutableBigMatrix matrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        if (MatrixUtils.preferSparseMatrix(rows, columns, valueFunction)) {
            return sparseMatrix(rows, columns, valueFunction);
        } else {
            return denseMatrix(rows, columns, valueFunction);
        }
    }

    static MutableBigMatrix denseMatrix(BigMatrix matrix) {
        return denseMatrix(matrix.rows(), matrix.columns(), (row, column) -> matrix.get(row, column));
    }

    static MutableBigMatrix denseMatrix(int rows, int columns) {
        return denseMatrix(rows, columns, new BigDecimal[0]);
    }

    static MutableBigMatrix denseMatrix(int rows, int columns, double... values) {
        return denseMatrix(rows, columns, MatrixUtils.toBigDecimal(values));
    }

    static MutableBigMatrix denseMatrix(int rows, int columns, BigDecimal... values) {
        return new DenseMutableBigMatrix(rows, columns, values);
    }

    static MutableBigMatrix denseMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new DenseMutableBigMatrix(rows, columns, valueFunction);
    }

    static MutableBigMatrix denseMatrix(int rows, int columns, CoordValue... values) {
        return new DenseMutableBigMatrix(rows, columns, values);
    }

    static MutableBigMatrix denseIdentityMatrix(int size) {
        return denseMatrix(ImmutableBigMatrix.identityMatrix(size));
    }

    static MutableBigMatrix sparseMatrix(BigMatrix matrix) {
        return sparseMatrix(matrix.rows(), matrix.columns(), (row, column) -> matrix.get(row, column));
    }

    static MutableBigMatrix sparseMatrix(int rows, int columns) {
        return sparseMatrix(rows, columns, new BigDecimal[0]);
    }

    static MutableBigMatrix sparseMatrix(int rows, int columns, double... values) {
        return sparseMatrix(rows, columns, MatrixUtils.toBigDecimal(values));
    }

    static MutableBigMatrix sparseMatrix(int rows, int columns, BigDecimal... values) {
        return new SparseMutableBigMatrix(rows, columns, values);
    }

    static MutableBigMatrix sparseMatrix(int rows, int columns, CoordValue... values) {
        return new SparseMutableBigMatrix(rows, columns, values);
    }

    static MutableBigMatrix sparseMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new SparseMutableBigMatrix(rows, columns, valueFunction);
    }

    static MutableBigMatrix sparseIdentityMatrix(int size) {
        return sparseMatrix(ImmutableBigMatrix.identityMatrix(size));
    }

    static MutableBigMatrix identityMatrix(int size) {
        return sparseIdentityMatrix(size);
    }
}