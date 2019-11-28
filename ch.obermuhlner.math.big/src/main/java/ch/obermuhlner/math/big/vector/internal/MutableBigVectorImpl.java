package ch.obermuhlner.math.big.vector.internal;

import ch.obermuhlner.math.big.matrix.MutableBigMatrix;
import ch.obermuhlner.math.big.vector.MutableBigVector;

import java.math.BigDecimal;

public class MutableBigVectorImpl extends AbstractBigVectorImpl<MutableBigMatrix> implements MutableBigVector {

    public MutableBigVectorImpl(MutableBigMatrix matrix) {
        super(matrix);
    }

    @Override
    public void set(int index, BigDecimal value) {
        matrix.set(index, 0, value);
    }

    @Override
    public void insert(int index, BigDecimal value) {
        matrix.insertRow(index, value);
    }

    @Override
    public void append(BigDecimal value) {
        matrix.appendRow(value);
    }

    @Override
    public void remove(int index) {
        matrix.removeRow(index);
    }

    @Override
    public void fill(BigDecimal value) {
        matrix.fill(value);
    }

    @Override
    public void clear() {
        matrix.clear();
    }

    @Override
    public void swap(int index1, int index2) {
        matrix.swapRows(index1, index2);
    }
}
