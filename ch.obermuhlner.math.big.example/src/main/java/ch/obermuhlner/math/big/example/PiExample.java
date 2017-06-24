package ch.obermuhlner.math.big.example;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.BigDecimalMath;

/**
 * This example shows how to calculate pi with different precisions.
 */
public class PiExample {

	public static void main(String[] args) {
		for (int precision = 1; precision < 2000; precision++) {
			MathContext mathContext = new MathContext(precision);
			BigDecimal pi = BigDecimalMath.pi(mathContext);
			System.out.printf("%4d : %s\n", precision, pi.toString());
		}
	}
}
