package ch.obermuhlner.math.big.vector;

import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.vector.internal.ImmutableBigVectorImpl;

import java.math.BigDecimal;

public interface ImmutableBigVector extends BigVector {

    static ImmutableBigVector vector(double... values) {
        return vector(MatrixUtils.toBigDecimal(values));
    }
    static ImmutableBigVector vector(BigDecimal... values) {
        return denseVector(values);
    }

    static ImmutableBigVector zeroVector(int size) {
        return new ImmutableBigVectorImpl(ImmutableBigMatrix.matrix(size, 1));
    }

    static ImmutableBigVector denseVector(double... values) {
        return denseVector(MatrixUtils.toBigDecimal(values));
    }
    static ImmutableBigVector denseVector(BigDecimal... values) {
        return new ImmutableBigVectorImpl(ImmutableBigMatrix.denseMatrix(values.length, 1, values));
    }

    static ImmutableBigVector sparseVector(double... values) {
        return sparseVector(MatrixUtils.toBigDecimal(values));
    }
    static ImmutableBigVector sparseVector(BigDecimal... values) {
        return new ImmutableBigVectorImpl(ImmutableBigMatrix.sparseMatrix(values.length, 1, values));
    }
}
