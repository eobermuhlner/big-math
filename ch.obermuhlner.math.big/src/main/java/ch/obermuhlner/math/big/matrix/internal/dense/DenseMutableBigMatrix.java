package ch.obermuhlner.math.big.matrix.internal.dense;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.MutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

public class DenseMutableBigMatrix extends AbstractDenseBigMatrix implements MutableBigMatrix {
    public DenseMutableBigMatrix(int rows, int columns, BigDecimal... values) {
        super(rows, columns, values);
    }

    public DenseMutableBigMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        super(rows, columns, valueFunction);
    }

    @Override
    protected DenseMutableBigMatrix createBigMatrix(int rows, int columns) {
        return new DenseMutableBigMatrix(rows, columns);
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
    public void set(int row, int column, BigDecimal value) {
        internalSet(row, column, value);
    }

    @Override
    public BigDecimal get(int row, int column) {
        return data[index(row, column)];
    }

    @Override
    public void fill(BigDecimal value) {
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }
    }

    @Override
    public MutableBigMatrix subMatrix(int startRow, int startColumn, int rows, int columns) {
        return (MutableBigMatrix) super.subMatrix(startRow, startColumn, rows, columns);
    }

    @Override
    public MutableBigMatrix add(BigMatrix other, MathContext mathContext) {
        return (MutableBigMatrix) super.add(other, mathContext);
    }

    @Override
    public MutableBigMatrix subtract(BigMatrix other, MathContext mathContext) {
        return (MutableBigMatrix) super.subtract(other, mathContext);
    }

    @Override
    public MutableBigMatrix multiply(BigDecimal value, MathContext mathContext) {
        return (MutableBigMatrix) super.multiply(value, mathContext);
    }

    @Override
    public MutableBigMatrix multiply(BigMatrix other, MathContext mathContext) {
        return (MutableBigMatrix) super.multiply(other, mathContext);
    }

    @Override
    public MutableBigMatrix transpose() {
        return (MutableBigMatrix) super.transpose();
    }

    @Override
    public MutableBigMatrix invert(MathContext mathContext) {
        return (MutableBigMatrix) super.invert(mathContext);
    }
}
