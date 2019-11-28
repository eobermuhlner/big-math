package ch.obermuhlner.math.big.vector;

public class ImmutableBigVectorTest extends AbstractBigVectorTest {

    @Override
    protected BigVector createBigVector(double... values) {
        return ImmutableBigVector.vector(values);
    }
}
