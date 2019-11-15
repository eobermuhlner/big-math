package ch.obermuhlner.math.big.matrix.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.dense.DenseImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.sparse.AbstractSparseBigMatrix;
import ch.obermuhlner.math.big.matrix.internal.sparse.SparseImmutableBigMatrix;

import java.math.BigDecimal;
import java.math.MathContext;

public abstract class AbstractBigMatrix implements BigMatrix {

    public abstract void internalSet(int row, int column, BigDecimal value);

    @Override
    public ImmutableBigMatrix multiply(BigMatrix other) {
        return multiply(other, null);
    }

    public ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext) {
        MatrixUtils.checkColumnsOtherRows(this, other);

        AbstractBigMatrix result;
        if (isSparseWithLotsOfZeroes(this) && isSparseWithLotsOfZeroes(other)) {
            result = new SparseImmutableBigMatrix(rows(), other.columns(), new BigDecimal[0]);
        } else {
            result = new DenseImmutableBigMatrix(rows(), other.columns(), new BigDecimal[0]);
        }

        for (int row = 0; row < result.rows(); row++) {
            for (int column = 0; column < result.columns(); column++) {
                BigDecimal sum = BigDecimal.ZERO;
                for (int index = 0; index < columns(); index++) {
                    sum = MatrixUtils.add(sum, MatrixUtils.multiply(get(row, index), other.get(index, column), mathContext), mathContext);
                }
                result.internalSet(row, column, sum.stripTrailingZeros());
            }
        }

        return result.asImmutableMatrix();
    }

    private boolean isSparseWithLotsOfZeroes(BigMatrix matrix) {
        if (matrix instanceof AbstractSparseBigMatrix) {
            AbstractSparseBigMatrix sparseMatrix = (AbstractSparseBigMatrix) matrix;
            return sparseMatrix.getSparseDefaultValue().signum() == 0 && sparseMatrix.sparseEmptyRatio() > 0.5;
        }
        return false;
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
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BigMatrix)) {
            return false;
        }
        BigMatrix other = (BigMatrix) obj;

        if (rows() != other.rows()) {
            return false;
        }
        if (columns() != other.columns()) {
            return false;
        }
        for (int row = 0; row < rows(); row++) {
            for (int column = 0; column < columns(); column++) {
                if (get(row, column).compareTo(other.get(row, column)) != 0) {
                    return false;
                }
            }
        }
        return true;
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
