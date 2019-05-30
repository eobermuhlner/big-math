package ch.obermuhlner.math.big.matrix;

import java.math.BigDecimal;
import java.math.MathContext;

public interface ImmutableBigMatrix extends BigMatrix {

    ImmutableBigMatrix add(BigMatrix other, MathContext mathContext);
    ImmutableBigMatrix subtract(BigMatrix other, MathContext mathContext);
    ImmutableBigMatrix multiply(BigDecimal value, MathContext mathContext);
    ImmutableBigMatrix multiply(BigMatrix other, MathContext mathContext);

    ImmutableBigMatrix transpose();
}
