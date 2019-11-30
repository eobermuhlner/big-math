package ch.obermuhlner.math.big.vector.internal.matrix;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.vector.ImmutableBigVector;

public class MatrixImmutableBigVector extends MatrixBigVector<BigMatrix> implements ImmutableBigVector {

    public MatrixImmutableBigVector(BigMatrix matrix) {
        super(matrix);
    }

}
