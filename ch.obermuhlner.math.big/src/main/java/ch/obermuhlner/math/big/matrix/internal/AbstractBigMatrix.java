package ch.obermuhlner.math.big.matrix.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableOperations;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.Function;

public abstract class AbstractBigMatrix implements BigMatrix {

    public abstract void internalSet(int row, int column, BigDecimal value);

    @Override
    public ImmutableBigMatrix add(BigMatrix other, MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(this) || MatrixUtils.preferSparseMatrix(other)) {
            return ImmutableOperations.sparseAdd(this, other, mathContext);
        }
        return ImmutableOperations.denseAdd(this, other, mathContext);
    }

    @Override
    public ImmutableBigMatrix subtract(BigMatrix other, MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(this) || MatrixUtils.preferSparseMatrix(other)) {
            return ImmutableOperations.sparseSubtract(this, other, mathContext);
        }
        return ImmutableOperations.denseSubtract(this, other, mathContext);
    }

    @Override
    public ImmutableBigMatrix multiply(BigDecimal value, MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(this)) {
            return ImmutableOperations.sparseMultiply(this, value, mathContext);
        }
        return ImmutableOperations.denseMultiply(this, value, mathContext);
    }

    @Override
    public ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(this) || MatrixUtils.preferSparseMatrix(other)) {
            return ImmutableOperations.sparseMultiply(this, other, mathContext);
        }
        return ImmutableOperations.denseMultiply(this, other, mathContext);
    }

    @Override
    public ImmutableBigMatrix elementOperation(Function<BigDecimal, BigDecimal> operation) {
        if (MatrixUtils.preferSparseMatrix(this)) {
            return ImmutableOperations.sparseElementOperation(this, operation);
        }
        return ImmutableOperations.denseElementOperation(this, operation);
    }

    @Override
    public ImmutableBigMatrix transpose() {
        if (MatrixUtils.preferSparseMatrix(this)) {
            return ImmutableOperations.sparseTranspose(this);
        }
        return ImmutableOperations.denseTranspose(this);
    }

    @Override
    public BigDecimal sum(MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(this)) {
            return ImmutableOperations.sparseSum(this, mathContext);
        }
        return ImmutableOperations.denseSum(this, mathContext);
    }

    @Override
    public BigDecimal product(MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(this)) {
            return ImmutableOperations.sparseProduct(this, mathContext);
        }
        return ImmutableOperations.denseProduct(this, mathContext);
    }

    @Override
    public ImmutableBigMatrix round(MathContext mathContext) {
        if (MatrixUtils.preferSparseMatrix(this)) {
            return ImmutableOperations.sparseRound(this, mathContext);
        }
        return ImmutableOperations.denseRound(this, mathContext);
    }

    @Override
    public int hashCode() {
        int result = 1;

        int rows = rows();
        int columns = columns();

        result = 31 * result + rows;
        result = 31 * result + columns;

        int n = rows * columns;
        int index = 1;
        int lastIndex = 1;

        while (index < n) {
            int elementHash = get(index / columns, index % columns).stripTrailingZeros().hashCode();
            result = 31 * result + elementHash;

            int tmp = index + lastIndex;
            lastIndex = index;
            index = tmp;
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (! (obj instanceof BigMatrix)) {
            return false;
        }

        BigMatrix other = (BigMatrix) obj;

        if (isSparse() || other.isSparse()) {
            return ImmutableOperations.sparseEquals(this, other);
        }

        return ImmutableOperations.denseEquals(this, other);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        int maxColumns = Math.min(columns(), 10);
        int maxRows = Math.min(rows(), 10);

        result.append("[");
        for (int row = 0; row < maxRows; row++) {
            result.append("[");
            for (int column = 0; column < maxColumns; column++) {
                if (column != 0) {
                    result.append(", ");
                }
                result.append(get(row, column));
            }
            if (columns() != maxColumns) {
                result.append(", ...");
            }
            result.append("]");
            if (row == rows() - 1) {
                result.append("]");
            } else {
                result.append(",\n");
            }
        }
        if (rows() != maxRows) {
            result.append("...\n");
            result.append("]");
        }

        return result.toString();
    }
}
