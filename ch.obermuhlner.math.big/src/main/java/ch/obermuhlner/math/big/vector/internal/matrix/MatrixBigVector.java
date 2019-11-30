package ch.obermuhlner.math.big.vector.internal.matrix;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.vector.BigVector;
import ch.obermuhlner.math.big.vector.ImmutableBigVector;
import ch.obermuhlner.math.big.vector.internal.AbstractBigVectorImpl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Function;

public class MatrixBigVector<M extends BigMatrix> extends AbstractBigVectorImpl implements BigVector {

    protected final M matrix;

    MatrixBigVector(M matrix) {
        this.matrix = matrix;
    }

    @Override
    public BigDecimal get(int index) {
        return matrix.get(index, 0);
    }

    @Override
    public int size() {
        return matrix.rows();
    }

    @Override
    public ImmutableBigVector round(MathContext mathContext) {
        return new MatrixImmutableBigVector(matrix.round(mathContext));
    }

    @Override
    public ImmutableBigVector add(BigVector other, MathContext mathContext) {
        return new MatrixImmutableBigVector(matrix.add(other.asBigMatrix(), mathContext));
    }

    @Override
    public ImmutableBigVector subtract(BigVector other, MathContext mathContext) {
        return new MatrixImmutableBigVector(matrix.subtract(other.asBigMatrix(), mathContext));
    }

    @Override
    public ImmutableBigVector multiply(BigDecimal value, MathContext mathContext) {
        return new MatrixImmutableBigVector(matrix.multiply(value, mathContext));
    }

    @Override
    public ImmutableBigVector divide(BigDecimal value, MathContext mathContext) {
        return new MatrixImmutableBigVector(matrix.divide(value, mathContext));
    }

    @Override
    public ImmutableBigVector elementOperation(Function<BigDecimal, BigDecimal> operation) {
        return new MatrixImmutableBigVector(matrix.elementOperation(operation));
    }

    @Override
    public BigMatrix asBigMatrix() {
        return matrix;
    }
}
