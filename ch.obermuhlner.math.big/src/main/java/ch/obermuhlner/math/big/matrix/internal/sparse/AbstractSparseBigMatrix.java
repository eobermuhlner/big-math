package ch.obermuhlner.math.big.matrix.internal.sparse;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.Coord;
import ch.obermuhlner.math.big.matrix.CoordValue;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.AbstractBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.matrix.internal.SparseBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static ch.obermuhlner.math.big.matrix.Coord.coord;
import static java.math.BigDecimal.valueOf;

public abstract class AbstractSparseBigMatrix extends AbstractBigMatrix implements SparseBigMatrix {
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

    @Override
    public ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext) {
        if (defaultValue.signum() == 0 &&  MatrixUtils.preferSparseMatrix(this)) {
            return multiplySparse(other, mathContext);
        }
        return super.multiply(other, mathContext);
    }

    private ImmutableBigMatrix multiplySparse(BigMatrix other, MathContext mathContext) {
        Map<Integer, Map<Integer, BigDecimal>> leftByRowColumn = toSparseNestedMap();
        Map<Integer, Map<Integer, BigDecimal>> rightByColumnRow = other.toTransposedSparseNestedMap();

        SparseImmutableBigMatrix result = new SparseImmutableBigMatrix(rows(), other.columns());

        for (Map.Entry<Integer, Map<Integer, BigDecimal>> leftRow : leftByRowColumn.entrySet()) {
            for (Map.Entry<Integer, Map<Integer, BigDecimal>> rightColumn : rightByColumnRow.entrySet()) {
                BigDecimal sum = BigDecimal.ZERO;
                Set<Integer> commonIndices = new HashSet<>(leftRow.getValue().keySet());
                commonIndices.retainAll(rightColumn.getValue().keySet());
                for (Integer index : commonIndices) {
                    BigDecimal v = MatrixUtils.multiply(leftRow.getValue().get(index), rightColumn.getValue().get(index), mathContext);
                    sum = MatrixUtils.add(sum, v, mathContext);
                }
                result.internalSet(leftRow.getKey(), rightColumn.getKey(), sum);
            }
        }

        return result;
    }

    @Override
    public ImmutableBigMatrix elementOperation(Function<BigDecimal, BigDecimal> operation) {
        return elementOperationSparse(operation);
    }

    private ImmutableBigMatrix elementOperationSparse(Function<BigDecimal, BigDecimal> operation) {
        SparseImmutableBigMatrix result = new SparseImmutableBigMatrix(rows(), columns());

        result.defaultValue = operation.apply(defaultValue);

        getCoordValues().forEach(cv -> {
            result.internalSet(cv.coord.row, cv.coord.column, operation.apply(cv.value));
        });

        return result;
    }

    @Override
    public BigDecimal sum(MathContext mathContext) {
        BigDecimal result = MatrixUtils.multiply(valueOf(sparseEmptySize()), defaultValue, mathContext);

        for (BigDecimal value : data.values()) {
            result = MatrixUtils.add(result, value, mathContext);
        }

        return result;
    }

    @Override
    public BigDecimal product(MathContext mathContext) {
        BigDecimal result;
        if (mathContext == null) {
            result = MatrixUtils.pow(defaultValue, sparseEmptySize());
        } else {
            result = BigDecimalMath.pow(defaultValue, sparseEmptySize(), mathContext);
        }

        if (result.signum() == 0) {
            return result;
        }

        for (BigDecimal value : data.values()) {
            result = MatrixUtils.multiply(result, value, mathContext);
        }

        return result;
    }

    @Override
    public ImmutableBigMatrix transpose() {
        if (MatrixUtils.preferSparseMatrix(this)) {
            return transposeSparse();
        }
        return super.transpose();
    }

    private ImmutableBigMatrix transposeSparse() {
        SparseImmutableBigMatrix result = new SparseImmutableBigMatrix(columns, rows);

        result.defaultValue = defaultValue;

        for (Map.Entry<Integer, BigDecimal> entry : data.entrySet()) {
            int index = entry.getKey();
            BigDecimal value = entry.getValue();
            result.internalSet(index % columns, index / columns, value);
        }

        return result;
    }

    @Override
    public ImmutableBigMatrix round(MathContext mathContext) {
        return roundSparse(mathContext);
    }

    private ImmutableBigMatrix roundSparse(MathContext mathContext) {
        SparseImmutableBigMatrix result = new SparseImmutableBigMatrix(columns, rows);

        getCoordValues().forEach(cv -> {
            result.internalSet(cv.coord.row, cv.coord.column, cv.value.round(mathContext).stripTrailingZeros());
        });
        return result;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (obj instanceof AbstractSparseBigMatrix) {
            return equalsSparse((AbstractSparseBigMatrix) obj);
        }
        return super.equals(obj);
    }

    private boolean equalsSparse(AbstractSparseBigMatrix other) {
        if (rows() != other.rows()) {
            return false;
        }
        if (columns() != other.columns()) {
            return false;
        }

        Set<Integer> mergedIndexes = new HashSet<>(data.keySet());
        mergedIndexes.addAll(other.data.keySet());

        if (sparseEmptySize() != 0 && other.sparseEmptySize() != 0 && defaultValue != other.defaultValue) {
            return false;
        }

        for (int index : mergedIndexes) {
            if (internalGet(index).compareTo(other.internalGet(index)) != 0) {
                return false;
            }
        }

        return true;
    }

    public Stream<Coord> getCoords() {
        if (defaultValue.signum() == 0) {
            return getCoordsSparse();
        }

        return super.getCoords();
    }

    private Stream<Coord> getCoordsSparse() {
        int columns = columns();
        return data.keySet().stream()
                .map(i -> coord(i / columns, i % columns));
    }
}
