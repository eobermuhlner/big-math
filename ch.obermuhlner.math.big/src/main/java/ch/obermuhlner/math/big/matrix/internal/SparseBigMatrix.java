package ch.obermuhlner.math.big.matrix.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;

import java.math.BigDecimal;

public interface SparseBigMatrix extends BigMatrix {

    BigDecimal getSparseDefaultValue();

    int sparseFilledSize();

    default int sparseEmptySize() {
        return size() - sparseFilledSize();
    }

    default double sparseEmptyRatio() {
        if (size() == 0) {
            return 0.0;
        }

        return (double) sparseEmptySize() / size();
    }
}