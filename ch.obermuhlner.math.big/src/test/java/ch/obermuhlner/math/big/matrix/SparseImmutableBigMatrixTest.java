package ch.obermuhlner.math.big.matrix;

public class SparseImmutableBigMatrixTest extends AbstractImmutableBigMatrixTest {

    @Override
    protected ImmutableBigMatrix createImmutableBigMatrix(int rows, int columns, double... values) {
        return ImmutableBigMatrix.sparseMatrix(rows, columns, values);
    }
}
