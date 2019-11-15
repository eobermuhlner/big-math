package ch.obermuhlner.math.big.matrix.internal.sparse;

import ch.obermuhlner.math.big.matrix.CoordValue;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public class SparseImmutableBigMatrix extends AbstractSparseBigMatrix implements ImmutableBigMatrix {
    public SparseImmutableBigMatrix(int rows, int columns) {
        super(rows, columns);
    }

    public SparseImmutableBigMatrix(int rows, int columns, BigDecimal... values) {
        super(rows, columns, values);
    }

    public SparseImmutableBigMatrix(int rows, int columns, CoordValue... values) {
        super(rows, columns, values);
    }

    public SparseImmutableBigMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        super(rows, columns, valueFunction);
    }
}
