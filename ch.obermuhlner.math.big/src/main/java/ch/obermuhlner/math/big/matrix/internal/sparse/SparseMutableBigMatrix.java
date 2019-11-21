package ch.obermuhlner.math.big.matrix.internal.sparse;

import ch.obermuhlner.math.big.matrix.CoordValue;
import ch.obermuhlner.math.big.matrix.MutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void insertRow(int row, BigDecimal... values) {
        MatrixUtils.checkRow(rows+1, row);

        if (row == rows) {
            appendRow(values);
            return;
        }

        Map<Integer, BigDecimal> moved = new HashMap<>();
        List<Integer> removed = new ArrayList<>();
        for (Map.Entry<Integer, BigDecimal> entry : data.entrySet()) {
            int entryRow = entry.getKey() / columns;
            if (entryRow >= row) {
                removed.add(entry.getKey());
                moved.put(entry.getKey() + columns, entry.getValue());
            }
        }
        removed.forEach(i -> data.remove(i));
        data.putAll(moved);
        rows++;

        int n = Math.min(values.length, columns);
        for (int column = 0; column < n; column++) {
            internalSet(row, column, values[column]);
        }
    }

    @Override
    public void appendRow(BigDecimal... values) {
        rows++;
        int n = Math.min(values.length, columns);
        for (int column = 0; column < n; column++) {
            internalSet(rows-1, column, values[column]);
        }
    }

    @Override
    public void removeRow(int row) {
        MatrixUtils.checkRow(this, row);

        Map<Integer, BigDecimal> moved = new HashMap<>();
        List<Integer> removed = new ArrayList<>();
        for (Map.Entry<Integer, BigDecimal> entry : data.entrySet()) {
            int entryRow = entry.getKey() / columns;
            if (entryRow >= row) {
                removed.add(entry.getKey());
                if (entryRow > row) {
                    moved.put(entry.getKey() - columns, entry.getValue());
                }
            }
        }
        removed.forEach(i -> data.remove(i));
        data.putAll(moved);
        rows--;
    }

    @Override
    public void insertColumn(int column, BigDecimal... values) {
        MatrixUtils.checkRow(columns+1, column);

        Map<Integer, BigDecimal> moved = new HashMap<>();
        for (Map.Entry<Integer, BigDecimal> entry : data.entrySet()) {
            int oldRow = entry.getKey() / columns;
            int oldColumn = entry.getKey() % columns;
            int newColumn = oldColumn >= column ? oldColumn + 1 : oldColumn;
            int movedIndex = oldRow * (columns+1) + newColumn;
            moved.put(movedIndex, entry.getValue());
        }
        data = moved;

        columns++;

        int n = Math.min(values.length, columns);
        for (int row = 0; row < n; row++) {
            internalSet(row, column, values[row]);
        }
    }

    @Override
    public void appendColumn(BigDecimal... values) {
        insertColumn(columns, values);
    }

    @Override
    public void removeColumn(int column) {
        MatrixUtils.checkColumn(this, column);

        Map<Integer, BigDecimal> moved = new HashMap<>();
        for (Map.Entry<Integer, BigDecimal> entry : data.entrySet()) {
            int oldRow = entry.getKey() / columns;
            int oldColumn = entry.getKey() % columns;
            if (oldColumn != column) {
                int newColumn = oldColumn >= column ? oldColumn - 1 : oldColumn;
                int movedIndex = oldRow * (columns - 1) + newColumn;
                moved.put(movedIndex, entry.getValue());
            }
        }
        data = moved;

        columns--;
    }

    @Override
    public void fill(BigDecimal value) {
        data.clear();
        defaultValue = value;
    }
}
