package ch.obermuhlner.math.big.vector;

import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.vector.internal.fix.Fix2ImmutableBigVector;
import ch.obermuhlner.math.big.vector.internal.fix.Fix3ImmutableBigVector;

public class FixImmutableBigVectorTest extends AbstractBigVectorTest {

    @Override
    protected BigVector createBigVector(double... values) {
        switch (values.length) {
            case 2:
                return new Fix2ImmutableBigVector(MatrixUtils.toBigDecimal(values));
            case 3:
                return new Fix3ImmutableBigVector(MatrixUtils.toBigDecimal(values));
            default:
                return ImmutableBigVector.vector(values);
        }
    }
}
