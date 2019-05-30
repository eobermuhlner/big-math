package ch.obermuhlner.math.big.matrix;

import java.math.BigDecimal;
import java.math.MathContext;

public interface BigMatrix {
    int rows();
    int columns();

    BigDecimal get(int row, int column);
}
