package ch.obermuhlner.math.big.matrix.internal.sparse;

import ch.obermuhlner.math.big.matrix.Coord;
import ch.obermuhlner.math.big.matrix.CoordValue;
import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static ch.obermuhlner.math.big.matrix.Coord.coord;
import static java.math.BigDecimal.ZERO;

public abstract class AbstractSparseBigMatrix extends AbstractBigMatrix {
    protected final int rows;
    protected final int columns;
    protected final Map<Integer, BigDecimal> data = new HashMap<>();
    protected BigDecimal defaultValue;

    public AbstractSparseBigMatrix(BigDecimal defaultValue, int rows, int columns) {
        MatrixUtils.checkRows(rows);
        MatrixUtils.checkColumns(columns);

        this.rows = rows;
        this.columns = columns;
        this.defaultValue = defaultValue;
    }

    public AbstractSparseBigMatrix(int rows, int columns) {
        this(ZERO, rows, columns);
    }

    public AbstractSparseBigMatrix(int rows, int columns, BigDecimal... values) {
        this(rows, columns);

        for (int i = 0; i < values.length; i++) {
            internalSet(i, values[i]);
        }
    }

    public AbstractSparseBigMatrix(int rows, int columns, CoordValue... values) {
        this(rows, columns);

        for (CoordValue value : values) {
            internalSet(value.coord.row, value.coord.column, value.value);
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
        return internalGet(index);
    }

    @Override
    public boolean isSparse() {
        return true;
    }

    @Override
    public BigDecimal getSparseDefaultValue() {
        return defaultValue;
    }

    BigDecimal internalGet(int index) {
        return data.getOrDefault(index, defaultValue);
    }

    @Override
    public int sparseFilledSize() {
        return data.size();
    }

    public void internalSet(int row, int column, BigDecimal value) {
        MatrixUtils.checkRow(this, row);
        MatrixUtils.checkColumn(this, column);

        int index = row*columns + column;
        internalSet(index, value);
    }

    public void internalSet(int index, BigDecimal value) {
        if (value.compareTo(defaultValue) == 0) {
            data.remove(index);
        } else {
            data.put(index, value);
        }
    }

    public Stream<Coord> getCoords() {
        int columns = columns();
        return data.keySet().stream()
                .map(i -> coord(i / columns, i % columns));
    }
}
