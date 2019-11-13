package ch.obermuhlner.math.big.matrix.internal.sparse;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
        return internalGet(index);
    }


    BigDecimal internalGet(int index) {
        return data.getOrDefault(index, defaultValue);
    }

    public int sparseSize() {
        return data.size();
    }

    @Override
    public ImmutableBigMatrix add(BigMatrix other) {
        if (other instanceof AbstractSparseBigMatrix) {
            return addSparse((AbstractSparseBigMatrix) other, null);
        }
        return super.add(other);
    }

    @Override
    public ImmutableBigMatrix add(BigMatrix other, MathContext mathContext) {
        if (other instanceof AbstractSparseBigMatrix) {
            return addSparse((AbstractSparseBigMatrix) other, mathContext);
        }
        return super.add(other, mathContext);
    }

    private ImmutableBigMatrix addSparse(AbstractSparseBigMatrix other, MathContext mathContext) {
        MatrixUtils.checkSameSize(this, other);

        SparseImmutableBigMatrix m = new SparseImmutableBigMatrix(rows, columns);
        m.defaultValue = MatrixUtils.subtract(defaultValue, other.defaultValue, mathContext);

        Set<Integer> mergedIndexes = new HashSet<>(data.keySet());
        mergedIndexes.addAll(other.data.keySet());

        for (int index : mergedIndexes) {
            m.internalSet(index, MatrixUtils.add(internalGet(index), other.internalGet(index), mathContext));
        }

        return m.asImmutableMatrix();
    }

    @Override
    public ImmutableBigMatrix subtract(BigMatrix other) {
        if (other instanceof AbstractSparseBigMatrix) {
            return subtractSparse((AbstractSparseBigMatrix) other, null);
        }
        return super.subtract(other);
    }

    @Override
    public ImmutableBigMatrix subtract(BigMatrix other, MathContext mathContext) {
        if (other instanceof AbstractSparseBigMatrix) {
            return subtractSparse((AbstractSparseBigMatrix) other, mathContext);
        }
        return super.subtract(other, mathContext);
    }

    private ImmutableBigMatrix subtractSparse(AbstractSparseBigMatrix other, MathContext mathContext) {
        MatrixUtils.checkSameSize(this, other);

        SparseImmutableBigMatrix m = new SparseImmutableBigMatrix(rows, columns);
        m.defaultValue = MatrixUtils.subtract(defaultValue, other.defaultValue, mathContext);

        Set<Integer> mergedIndexes = new HashSet<>(data.keySet());
        mergedIndexes.addAll(other.data.keySet());

        for (int index : mergedIndexes) {
            m.internalSet(index, MatrixUtils.subtract(internalGet(index), other.internalGet(index), mathContext));
        }

        return m.asImmutableMatrix();
    }

    @Override
    public ImmutableBigMatrix multiply(BigDecimal value) {
        return multiplySparse(value, null);
    }

    @Override
    public ImmutableBigMatrix multiply(BigDecimal value, MathContext mathContext) {
        return multiplySparse(value, mathContext);
    }

    private ImmutableBigMatrix multiplySparse(BigDecimal value, MathContext mathContext) {
        SparseImmutableBigMatrix m = new SparseImmutableBigMatrix(rows, columns);
        m.defaultValue = MatrixUtils.multiply(defaultValue, value, mathContext);

        for (Map.Entry<Integer, BigDecimal> entry : data.entrySet()) {
            int index = entry.getKey();
            BigDecimal sparseValue = entry.getValue();
            m.internalSet(index, MatrixUtils.multiply(sparseValue, value, mathContext));
        }

        return m.asImmutableMatrix();
    }

    protected void internalSet(int row, int column, BigDecimal value) {
        MatrixUtils.checkRow(this, row);
        MatrixUtils.checkColumn(this, column);

        int index = row*columns + column;
        internalSet(index, value);
    }

    void internalSet(int index, BigDecimal value) {
        if (value.equals(defaultValue)) {
            data.remove(index);
        } else {
            data.put(index, value);
        }
    }
}
