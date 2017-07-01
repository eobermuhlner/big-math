package ch.obermuhlner.math.big.viewer;

import java.math.BigDecimal;
import java.math.MathContext;

public interface BigDecimalFunction2 {
	BigDecimal apply(BigDecimal value1, BigDecimal value2, MathContext mathContext);
}
