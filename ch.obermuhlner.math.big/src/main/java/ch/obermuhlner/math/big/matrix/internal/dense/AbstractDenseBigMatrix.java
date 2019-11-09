package ch.obermuhlner.math.big.matrix.internal.dense;

import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public abstract class AbstractDenseBigMatrix extends AbstractBigMatrix {
    protected final int rows;
    protected final int columns;
    protected final BigDecimal[] data;

    public AbstractDenseBigMatrix(int rows, int columns) {
        checkRows(rows);
        checkColumns(columns);

        this.rows = rows;
        this.columns = columns;
        this.data = new BigDecimal[rows * columns];
    }

    public AbstractDenseBigMatrix(int rows, int columns, BigDecimal... values) {
        this(rows, columns);

        for (int i = 0; i < values.length; i++) {
            data[i] = values[i];
        }
    }

    public AbstractDenseBigMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
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
        checkRow(row);
        checkColumn(column);

        return data[index(row, column)];
    }

    protected void internalSet(int row, int column, BigDecimal value) {
        checkRow(row);
        checkColumn(column);

        data[index(row, column)] = value;
    }

    protected int index(int row, int column) {
        checkRow(row);
        checkColumn(column);

        return row*columns + column;
    }
}
