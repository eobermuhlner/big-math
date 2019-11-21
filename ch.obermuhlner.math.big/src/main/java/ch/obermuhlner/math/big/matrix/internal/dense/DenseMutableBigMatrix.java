package ch.obermuhlner.math.big.matrix.internal.dense;

import ch.obermuhlner.math.big.matrix.CoordValue;
import ch.obermuhlner.math.big.matrix.MutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static java.math.BigDecimal.ZERO;

public class DenseMutableBigMatrix extends AbstractDenseNestedListBigMatrix implements MutableBigMatrix {
    public DenseMutableBigMatrix(int rows, int columns, BigDecimal... values) {
        super(rows, columns, values);
    }

    public DenseMutableBigMatrix(int rows, int columns, CoordValue... values) {
        super(rows, columns, values);
    }

    public DenseMutableBigMatrix(int rows, int columns, BiFunction<Integer, Integer, BigDecimal> valueFunction) {
        super(rows, columns, valueFunction);
    }

    @Override
    public void set(int row, int column, BigDecimal value) {
        internalSet(row, column, value);
    }

    @Override
    public void insertRow(int row, BigDecimal... values) {
        data.add(row, createRowList(values));
        rows++;
    }

    @Override
    public void appendRow(BigDecimal... values) {
        data.add(createRowList(values));
        rows++;
    }

    private List<BigDecimal> createRowList(BigDecimal... values) {
        List<BigDecimal> rowList = new ArrayList<>();

        int n = Math.min(columns, values.length);
        for (int i = 0; i < n; i++) {
            rowList.add(values[i]);
        }
        for (int i = n; i < columns; i++) {
            rowList.add(ZERO);
        }

        return rowList;
    }

    @Override
    public void removeRow(int row) {
        data.remove(row);
        rows--;
    }

    @Override
    public void insertColumn(int column, BigDecimal... values) {
        int n = Math.min(rows, values.length);
        for (int row = 0; row < n; row++) {
            data.get(row).add(column, values[row]);
        }
        for (int row = n; row < rows; row++) {
            data.get(row).add(column, ZERO);
        }
        columns++;
    }

    @Override
    public void appendColumn(BigDecimal... values) {
        int n = Math.min(rows, values.length);
        for (int row = 0; row < n; row++) {
            data.get(row).add(values[row]);
        }
        for (int row = n; row < rows; row++) {
            data.get(row).add(ZERO);
        }
        columns++;
    }

    @Override
    public void removeColumn(int column) {
        MatrixUtils.checkColumn(this, column);

        for (int row = 0; row < rows; row++) {
            data.get(row).remove(column);
        }
        columns--;
    }
}
