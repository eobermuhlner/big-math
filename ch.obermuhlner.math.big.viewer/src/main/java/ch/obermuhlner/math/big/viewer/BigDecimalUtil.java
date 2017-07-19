package ch.obermuhlner.math.big.viewer;

import java.math.BigDecimal;

public class BigDecimalUtil {

	public static String toString(BigDecimal value) {
		return value.stripTrailingZeros().toPlainString();
	}

}
