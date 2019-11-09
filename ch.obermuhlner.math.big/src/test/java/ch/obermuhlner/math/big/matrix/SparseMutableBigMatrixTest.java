package ch.obermuhlner.math.big.matrix;

public class SparseMutableBigMatrixTest extends AbstractMutableBigMatrixTest {

    @Override
    protected MutableBigMatrix createMutableBigMatrix(int rows, int columns, double... values) {
        return MutableBigMatrix.sparseMatrix(rows, columns, values);
    }
}
