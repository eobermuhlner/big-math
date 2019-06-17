package ch.obermuhlner.math.big.matrix.internal.dense;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

public class DenseImmutableBigMatrix extends AbstractDenseBigMatrix implements ImmutableBigMatrix {
    public DenseImmutableBigMatrix(int rows, int columns, BigDecimal... values) {
        super(rows, columns, values);
    }

    public DenseImmutableBigMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        super(rows, columns, valueFunction);
    }

    @Override
    protected DenseImmutableBigMatrix createBigMatrix(int rows, int columns) {
        return new DenseImmutableBigMatrix(rows, columns);
    }

    @Override
    public ImmutableBigMatrix subMatrix(int startRow, int startColumn, int rows, int columns) {
        return (ImmutableBigMatrix) super.subMatrix(startRow, startColumn, rows, columns);
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

    @Override
    public ImmutableBigMatrix invert(MathContext mathContext) {
        return (ImmutableBigMatrix) super.invert(mathContext);
    }
}
