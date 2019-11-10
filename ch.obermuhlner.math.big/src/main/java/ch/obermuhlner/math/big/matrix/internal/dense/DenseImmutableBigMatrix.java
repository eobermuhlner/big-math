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
}
