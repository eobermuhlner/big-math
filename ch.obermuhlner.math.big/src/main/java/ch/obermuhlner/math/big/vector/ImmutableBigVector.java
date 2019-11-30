package ch.obermuhlner.math.big.vector;

import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.vector.internal.fix.Fix2ImmutableBigVector;
import ch.obermuhlner.math.big.vector.internal.fix.Fix3ImmutableBigVector;
import ch.obermuhlner.math.big.vector.internal.matrix.MatrixImmutableBigVector;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public interface ImmutableBigVector extends BigVector {

    static ImmutableBigVector vector(double... values) {
        return vector(MatrixUtils.toBigDecimal(values));
    }

    static ImmutableBigVector vector(BigDecimal... values) {
        return denseVector(values);
    }

    static ImmutableBigVector zeroVector(int size) {
        return denseVectorOfSize(size, new BigDecimal[0]);
    }

    static ImmutableBigVector denseVector(double... values) {
        return denseVector(MatrixUtils.toBigDecimal(values));
    }

    static ImmutableBigVector denseVector(BigDecimal... values) {
        return denseVectorOfSize(values.length, values);
    }

    static ImmutableBigVector denseVectorOfSize(int size, double... values) {
        return denseVectorOfSize(size, MatrixUtils.toBigDecimal(values));
    }

    static ImmutableBigVector denseVectorOfSize(int size, BigDecimal... values) {
        switch (size) {
            case 2:
                return new Fix2ImmutableBigVector(values);
            case 3:
                return new Fix3ImmutableBigVector(values);
            default:
                return new MatrixImmutableBigVector(ImmutableBigMatrix.denseMatrix(size, 1, values));
        }
    }

    static ImmutableBigVector sparseVector(double... values) {
        return sparseVector(MatrixUtils.toBigDecimal(values));
    }

    static ImmutableBigVector sparseVector(BigDecimal... values) {
        return sparseVectorOfSize(values.length, values);
    }

    static ImmutableBigVector sparseVectorOfSize(int size, double... values) {
        return sparseVectorOfSize(size, MatrixUtils.toBigDecimal(values));
    }

    static ImmutableBigVector sparseVectorOfSize(int size, BigDecimal... values) {
        return new MatrixImmutableBigVector(ImmutableBigMatrix.sparseMatrix(size, 1, values));
    }
}