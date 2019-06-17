package ch.obermuhlner.math.big.matrix;

import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.matrix.internal.dense.DenseImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.lamdba.LambdaImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.sparse.SparseImmutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

public interface ImmutableBigMatrix extends BigMatrix {

    ImmutableBigMatrix subMatrix(int startRow, int startColumn, int rows, int columns);

    ImmutableBigMatrix add(BigMatrix other, MathContext mathContext);
    ImmutableBigMatrix subtract(BigMatrix other, MathContext mathContext);
    ImmutableBigMatrix multiply(BigDecimal value, MathContext mathContext);
    ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext);

    ImmutableBigMatrix transpose();

    ImmutableBigMatrix invert(MathContext mathContext);

    static ImmutableBigMatrix matrix(int rows, int columns, BigDecimal... values) {
        int n = rows * columns;
        if (values.length - n > 10000 || (values.length > 10000 && MatrixUtils.countZeroValues(values) > 10000)) {
            return sparse(rows, columns, values);
        } else {
            return dense(rows, columns, values);
        }
    }

    static ImmutableBigMatrix dense(int rows, int columns, BigDecimal... values) {
        return new DenseImmutableBigMatrix(rows, columns, values);
    }

    static ImmutableBigMatrix dense(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new DenseImmutableBigMatrix(rows, columns, valueFunction);
    }

    static ImmutableBigMatrix identity(int rows, int columns) {
        return new LambdaImmutableBigMatrix(rows, columns, (row, column) -> {
            return row == column ? BigDecimal.ONE : BigDecimal.ZERO;
        });
    }

    static ImmutableBigMatrix sparse(int rows, int columns, BigDecimal... values) {
        return new SparseImmutableBigMatrix(rows, columns, values);
    }

    static ImmutableBigMatrix sparse(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        return new SparseImmutableBigMatrix(rows, columns, valueFunction);
    }
}
