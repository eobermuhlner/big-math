package ch.obermuhlner.math.big.vector;

import ch.obermuhlner.math.big.matrix.internal.MatrixUtils;
import ch.obermuhlner.math.big.vector.internal.fix.Fix3ImmutableBigVector;

public class Fix3ImmutableBigVectorTest extends AbstractBigVectorTest {

    @Override
    protected BigVector createBigVector(double... values) {
        if (values.length == 3) {
            return new Fix3ImmutableBigVector(MatrixUtils.toBigDecimal(values));
        } else {
            return ImmutableBigVector.vector(values);
        }
    }
}
