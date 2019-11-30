package ch.obermuhlner.math.big.vector.internal.fix;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.vector.ImmutableBigVector;
import ch.obermuhlner.math.big.vector.internal.AbstractBigVectorImpl;
import ch.obermuhlner.math.big.vector.internal.VectorUtils;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class Fix2ImmutableBigVector extends AbstractBigVectorImpl implements ImmutableBigVector {

    private final BigDecimal value0;
    private final BigDecimal value1;

    public Fix2ImmutableBigVector(BigDecimal... values) {
        this(   values.length > 0 ? values[0] : ZERO,
                values.length > 1 ? values[1] : ZERO);
    }

    public Fix2ImmutableBigVector(BigDecimal value0, BigDecimal value1) {
        this.value0 = value0;
        this.value1 = value1;
    }

    @Override
    public BigDecimal get(int index) {
        VectorUtils.checkIndex(this, index);

        switch (index) {
            case 0:
                return value0;
            case 1:
                return value1;
        }

        throw new IllegalArgumentException("Invalid index: " + index);
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    public BigMatrix asBigMatrix() {
        return ImmutableBigMatrix.denseMatrix(2, 1, value0, value1);
    }
}
