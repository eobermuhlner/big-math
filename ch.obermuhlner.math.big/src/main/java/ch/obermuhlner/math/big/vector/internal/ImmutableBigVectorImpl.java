package ch.obermuhlner.math.big.vector.internal;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.vector.ImmutableBigVector;

public class ImmutableBigVectorImpl extends AbstractBigVectorImpl<BigMatrix> implements ImmutableBigVector {

    public ImmutableBigVectorImpl(BigMatrix matrix) {
        super(matrix);
    }

}
