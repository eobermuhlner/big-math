package ch.obermuhlner.math.big.matrix.internal.sparse;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

public class SparseImmutableBigMatrix extends AbstractSparseBigMatrix implements ImmutableBigMatrix {
    public SparseImmutableBigMatrix(int rows, int columns, BigDecimal... values) {
        super(rows, columns, values);
    }

    public SparseImmutableBigMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        super(rows, columns, valueFunction);
    }

    @Override
    protected SparseImmutableBigMatrix createBigMatrix(int rows, int columns) {
        return new SparseImmutableBigMatrix(rows, columns);
    }

    @Override
    public ImmutableBigMatrix add(BigMatrix other, MathContext mathContext) {
        return (ImmutableBigMatrix) super.add(other, mathContext);
    }

    @Override
    public ImmutableBigMatrix subtract(BigMatrix other, MathContext mathContext) {
        return (ImmutableBigMatrix) super.subtract(other, mathContext);
    }

    @Override
    public ImmutableBigMatrix multiply(BigDecimal value, MathContext mathContext) {
        return (ImmutableBigMatrix) super.multiply(value, mathContext);
    }

    @Override
    public ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext) {
        return (ImmutableBigMatrix) super.multiply(other, mathContext);
    }
}
