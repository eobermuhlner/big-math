package ch.obermuhlner.math.big.matrix.internal.dense;

import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;

import java.math.BigDecimal;

public abstract class AbstractDenseBigMatrix extends AbstractBigMatrix {
    protected final int rows;
    protected final int columns;
    protected final BigDecimal[] data;

    public AbstractDenseBigMatrix(int rows, int columns, BigDecimal... values) {
        this.rows = rows;
        this.columns = columns;
        this.data = new BigDecimal[rows*columns];

        for (int i = 0; i < values.length; i++) {
            data[i] = values[i];
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
        return data[index(row, column)];
    }

    protected void internalSet(int row, int column, BigDecimal value) {
        data[index(row, column)] = value;
    }

    protected int index(int row, int column) {
        return row*columns + column;
    }
}
