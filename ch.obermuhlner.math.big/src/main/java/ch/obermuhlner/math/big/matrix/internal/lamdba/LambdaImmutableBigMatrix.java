package ch.obermuhlner.math.big.matrix.internal.lamdba;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.dense.DenseImmutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

public class LambdaImmutableBigMatrix extends AbstractBigMatrix implements ImmutableBigMatrix {
    private final int rows;
    private final int columns;
    private final BiFunction<Integer, Integer, BigDecimal> valueFunction;

    public LambdaImmutableBigMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        this.rows = rows;
        this.columns = columns;
        this.valueFunction = valueFunction;
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int columns() {
        return columns;
    }

    @Override
    public BigDecimal get(int row, int column) {
        return valueFunction.apply(row, column);
    }

    @Override
    protected void internalSet(int row, int column, BigDecimal value) {
        throw new IllegalStateException();
    }

    @Override
    protected AbstractBigMatrix createBigMatrix(int rows, int columns) {
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
}
