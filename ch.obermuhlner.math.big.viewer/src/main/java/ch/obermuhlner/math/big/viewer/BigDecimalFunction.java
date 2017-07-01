package ch.obermuhlner.math.big.viewer;

import java.math.BigDecimal;
import java.math.MathContext;

public interface BigDecimalFunction {
	BigDecimal apply(BigDecimal value, MathContext mathContext);
}
