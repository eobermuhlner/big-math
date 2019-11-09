package ch.obermuhlner.math.big.matrix;

public class DenseMutableBigMatrixTest extends AbstractMutableBigMatrixTest {

    @Override
    protected MutableBigMatrix createMutableBigMatrix(int rows, int columns, double... values) {
        return MutableBigMatrix.denseMatrix(rows, columns, values);
    }
}
