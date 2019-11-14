package ch.obermuhlner.math.big.matrix.internal.sparse;

import ch.obermuhlner.math.big.matrix.CoordValue;
import ch.obermuhlner.math.big.matrix.MutableBigMatrix;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public class SparseMutableBigMatrix extends AbstractSparseBigMatrix implements MutableBigMatrix {
    public SparseMutableBigMatrix(int rows, int columns, BigDecimal... values) {
        super(rows, columns, values);
    }

    public SparseMutableBigMatrix(int rows, int columns, CoordValue... values) {
        super(rows, columns, values);
    }

    public SparseMutableBigMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        super(rows, columns, valueFunction);
    }

    @Override
    public void set(int row, int column, BigDecimal value) {
        internalSet(row, column, value);
    }

    @Override
    public void fill(BigDecimal value) {
        data.clear();
        defaultValue = value;
    }
}
