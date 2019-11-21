package ch.obermuhlner.math.big.matrix.internal.dense;

import ch.obermuhlner.math.big.matrix.CoordValue;
import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static java.math.BigDecimal.ZERO;

public abstract class AbstractDenseNestedListBigMatrix extends AbstractBigMatrix {
    protected int rows;
    protected int columns;
    protected final List<List<BigDecimal>> data;

    public AbstractDenseNestedListBigMatrix(int rows, int columns) {
        MatrixUtils.checkRows(rows);
        MatrixUtils.checkColumns(columns);

        this.rows = rows;
        this.columns = columns;
        this.data = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            List<BigDecimal> rowList = new ArrayList<>();
            for (int i = 0; i < columns; i++) {
                rowList.add(ZERO);
            }
            data.add(rowList);
        }
    }

    public AbstractDenseNestedListBigMatrix(int rows, int columns, BigDecimal... values) {
        this(rows, columns);

        int n = rows * columns;
        for (int i = 0; i < n; i++) {
            int row = i / columns;
            int column = i % columns;
            BigDecimal value = i < values.length ? values[i] : ZERO;
            internalSet(row, column, value);
        }
    }

    public AbstractDenseNestedListBigMatrix(int rows, int columns, CoordValue... values) {
        this(rows, columns);

        for (CoordValue value : values) {
            internalSet(value.coord.row, value.coord.column, value.value);
        }
    }

    public AbstractDenseNestedListBigMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
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

        return data.get(row).get(column);
    }

    public void internalSet(int row, int column, BigDecimal value) {
        MatrixUtils.checkRow(this, row);
        MatrixUtils.checkColumn(this, column);

        data.get(row).set(column, value);
    }
}
