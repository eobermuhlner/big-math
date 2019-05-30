package ch.obermuhlner.math.big.matrix.internal.dense;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;

public class DenseImmutableBigMatrix extends AbstractDenseBigMatrix implements ImmutableBigMatrix {
    public DenseImmutableBigMatrix(int rows, int columns, BigDecimal... values) {
        super(rows, columns, values);
    }

    @Override
    protected DenseImmutableBigMatrix createBigMatrix(int rows, int columns) {
        return new DenseImmutableBigMatrix(rows, columns);
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

    @Override
    public ImmutableBigMatrix transpose() {
        return (ImmutableBigMatrix) super.transpose();
    }
}
