package ch.obermuhlner.math.big.viewer;

import java.math.BigDecimal;

public class BigDecimalUtil {

	public static String toString(BigDecimal value) {
		String string = value.toString();
		
		if (!string.contains(".")) {
			return string;
		}
		
		int pos = string.length() - 1;
		while (string.charAt(pos) == '0') {
			pos--;
		}
		if (string.charAt(pos) == '.') {
			pos--;
		}
		
		return string.substring(0, pos + 1);
	}

}
