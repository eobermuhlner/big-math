package ch.obermuhlner.math.big.matrix.internal.sparse;

import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class AbstractSparseBigMatrix extends AbstractBigMatrix {
    protected final int rows;
    protected final int columns;
    protected final Map<Integer, BigDecimal> data = new HashMap<>();
    protected BigDecimal defaultValue = BigDecimal.ZERO;

    public AbstractSparseBigMatrix(int rows, int columns) {
        MatrixUtils.checkRows(rows);
        MatrixUtils.checkColumns(columns);

        this.rows = rows;
        this.columns = columns;
    }

    public AbstractSparseBigMatrix(int rows, int columns, BigDecimal... values) {
        this(rows, columns);

        for (int i = 0; i < values.length; i++) {
            data.put(i, values[i]);
        }
    }

    public AbstractSparseBigMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        this(rows, columns);

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                internalSet(row, column, valueFunction.apply(row, column));
            }
        }
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
        MatrixUtils.checkRow(this, row);
        MatrixUtils.checkColumn(this, column);

        int index = row*columns + column;
        return data.getOrDefault(index, defaultValue);
    }

    protected void internalSet(int row, int column, BigDecimal value) {
        MatrixUtils.checkRow(this, row);
        MatrixUtils.checkColumn(this, column);

        int index = row*columns + column;
        if (value.equals(defaultValue)) {
            data.remove(index);
        } else {
            data.put(index, value);
        }
    }
}
