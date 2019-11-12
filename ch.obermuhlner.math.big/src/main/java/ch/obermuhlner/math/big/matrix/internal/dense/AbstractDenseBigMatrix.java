package ch.obermuhlner.math.big.matrix.internal.dense;

import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import static java.math.BigDecimal.ZERO;

public abstract class AbstractDenseBigMatrix extends AbstractBigMatrix {
    protected final int rows;
    protected final int columns;
    protected final BigDecimal[] data;

    public AbstractDenseBigMatrix(int rows, int columns) {
        MatrixUtils.checkRows(rows);
        MatrixUtils.checkColumns(columns);

        this.rows = rows;
        this.columns = columns;
        this.data = new BigDecimal[rows * columns];
    }

    public AbstractDenseBigMatrix(int rows, int columns, BigDecimal... values) {
        this(rows, columns);

        int n = rows * columns;
        for (int i = 0; i < n; i++) {
            data[i] = i < values.length ? values[i] : ZERO;
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
        MatrixUtils.checkRow(this, row);
        MatrixUtils.checkColumn(this, column);

        return data[index(row, column)];
    }

    protected void internalSet(int row, int column, BigDecimal value) {
        MatrixUtils.checkRow(this, row);
        MatrixUtils.checkColumn(this, column);

        data[index(row, column)] = value;
    }

    protected int index(int row, int column) {
        return row*columns + column;
    }
}
