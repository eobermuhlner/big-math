package ch.obermuhlner.math.big.vector;

public class MutableBigVectorTest extends AbstractBigVectorTest {

    @Override
    protected BigVector createBigVector(double... values) {
        return MutableBigVector.vector(values);
    }
}
