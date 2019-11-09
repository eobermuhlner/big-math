package ch.obermuhlner.math.big.matrix;

import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.matrix.internal.dense.DenseImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.lamdba.LambdaImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.sparse.SparseImmutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

public interface ImmutableBigMatrix extends BigMatrix {

    ImmutableBigMatrix add(BigMatrix other, MathContext mathContext);

    ImmutableBigMatrix subtract(BigMatrix other, MathContext mathContext);

    ImmutableBigMatrix multiply(BigDecimal value, MathContext mathContext);

    ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext);

    default ImmutableBigMatrix invert(MathContext mathContext) {
        return toMutableMatrix().invert(mathContext).asImmutableMatrix();
    }

    default ImmutableBigMatrix transpose() {
        return new LambdaImmutableBigMatrix(columns(), rows(),
                (row, column) -> get(column, row));
    }

    default ImmutableBigMatrix subMatrix(int startRow, int startColumn, int rows, int columns) {
        return new LambdaImmutableBigMatrix(rows, columns,
                (row, column) -> get(row + startRow, column + startColumn));
    }

    default ImmutableBigMatrix minor(int skipRow, int skipColumn) {
        return new LambdaImmutableBigMatrix(rows() - 1, columns() - 1,
                (row, column) ->
                        get(row < skipRow ? row : row + 1,
                                column < skipColumn ? column : column + 1));
    }

    static ImmutableBigMatrix matrix(int rows, int columns, double... values) {
        return matrix(rows, columns, MatrixUtils.toBigDecimal(values));
    }

    static ImmutableBigMatrix matrix(int rows, int columns, BigDecimal... values) {
        int n = rows * columns;
        if (values.length - n >= 10000 || (values.length > 10000 && MatrixUtils.atLeastZeroValues(10000, values))) {
            return sparseMatrix(rows, columns, values);
        } else {
            return denseMatrix(rows, columns, values);
        }
    }

    static ImmutableBigMatrix matrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        int n = rows * columns;
        if (n >= 10000 && MatrixUtils.atLeastZeroValues(10000, rows, columns, valueFunction)) {
            return sparseMatrix(rows, columns, valueFunction);
        } else {
            return denseMatrix(rows, columns, valueFunction);
        }
    }

    static ImmutableBigMatrix denseMatrix(int rows, int columns, double... values) {
        return denseMatrix(rows, columns, MatrixUtils.toBigDecimal(values));
    }

    static ImmutableBigMatrix denseMatrix(int rows, int columns, BigDecimal... values) {
        return new DenseImmutableBigMatrix(rows, columns, values);
    }

    static ImmutableBigMatrix denseMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new DenseImmutableBigMatrix(rows, columns, valueFunction);
    }

    static ImmutableBigMatrix identityMatrix(int rows, int columns) {
        return new LambdaImmutableBigMatrix(rows, columns, (row, column) -> {
            return row == column ? BigDecimal.ONE : BigDecimal.ZERO;
        });
    }

    static ImmutableBigMatrix sparseMatrix(int rows, int columns, double... values) {
        return sparseMatrix(rows, columns, MatrixUtils.toBigDecimal(values));
    }

    static ImmutableBigMatrix sparseMatrix(int rows, int columns, BigDecimal... values) {
        return new SparseImmutableBigMatrix(rows, columns, values);
    }

    static ImmutableBigMatrix sparseMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new SparseImmutableBigMatrix(rows, columns, valueFunction);
    }

    static ImmutableBigMatrix lambdaMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new LambdaImmutableBigMatrix(rows, columns, valueFunction);
    }
}
