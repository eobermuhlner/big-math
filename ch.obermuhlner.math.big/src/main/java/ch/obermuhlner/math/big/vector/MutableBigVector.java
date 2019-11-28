package ch.obermuhlner.math.big.vector;

import ch.obermuhlner.math.big.matrix.MutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.vector.internal.MutableBigVectorImpl;

import java.math.BigDecimal;

public interface MutableBigVector extends BigVector {

    void set(int index, BigDecimal value);

    default void insert(int index, double value) {
        insert(index, BigDecimal.valueOf(value));
    }
    void insert(int index, BigDecimal value);
    default void append(double value) {
        append(BigDecimal.valueOf(value));
    }
    void append(BigDecimal value);
    void remove(int index);

    void fill(BigDecimal value);

    void clear();

    void swap(int index1, int index2);

    static MutableBigVector vector(double... values) {
        return vector(MatrixUtils.toBigDecimal(values));
    }
    static MutableBigVector vector(BigDecimal... values) {
        return denseVector(values);
    }

    static MutableBigVector vectorOfSize(int size, double... values) {
        return vectorOfSize(size, MatrixUtils.toBigDecimal(values));
    }
    static MutableBigVector vectorOfSize(int size, BigDecimal... values) {
        return denseVectorOfSize(size, values);
    }

    static MutableBigVector zeroVector(int size) {
        return vectorOfSize(size, new BigDecimal[0]);
    }

    static MutableBigVector denseVector(double... values) {
        return denseVector(MatrixUtils.toBigDecimal(values));
    }
    static MutableBigVector denseVector(BigDecimal... values) {
        return new MutableBigVectorImpl(MutableBigMatrix.denseMatrix(values.length, 1, values));
    }

    static MutableBigVector denseVectorOfSize(int size, double... values) {
        return denseVectorOfSize(size, MatrixUtils.toBigDecimal(values));
    }
    static MutableBigVector denseVectorOfSize(int size, BigDecimal... values) {
        return new MutableBigVectorImpl(MutableBigMatrix.denseMatrix(size, 1, values));
    }

    static MutableBigVector denseZeroVector(int size) {
        return denseVectorOfSize(size, new BigDecimal[0]);
    }


    static MutableBigVector sparseVector(double... values) {
        return sparseVector(MatrixUtils.toBigDecimal(values));
    }
    static MutableBigVector sparseVector(BigDecimal... values) {
        return new MutableBigVectorImpl(MutableBigMatrix.sparseMatrix(values.length, 1, values));
    }

    static MutableBigVector sparseVectorOfSize(int size, double... values) {
        return sparseVectorOfSize(size, MatrixUtils.toBigDecimal(values));
    }
    static MutableBigVector sparseVectorOfSize(int size, BigDecimal... values) {
        return new MutableBigVectorImpl(MutableBigMatrix.sparseMatrix(size, 1, values));
    }

    static MutableBigVector sparseZeroVector(int size) {
        return sparseVectorOfSize(size, new BigDecimal[0]);
    }

}
