package ch.obermuhlner.math.big.matrix;

public class DenseImmutableBigMatrixTest extends AbstractImmutableBigMatrixTest {

    @Override
    protected ImmutableBigMatrix createImmutableBigMatrix(int rows, int columns, double... values) {
        return ImmutableBigMatrix.denseMatrix(rows, columns, values);
    }
}
