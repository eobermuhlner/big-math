package ch.obermuhlner.math.big.example;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.BigDecimalMath;

public class BigDecimalMathExample {

	public static void main(String[] args) {
		exampleForDocu();

		exampleForJavaDoc_roundTrailingZeroes();
	}

	public static void exampleForDocu() {
		MathContext mathContext = new MathContext(100);
		
		System.out.println("All calculations with a precision of " + mathContext.getPrecision() + " digits.");
		System.out.println();
		System.out.println("Advanced functions:");
		System.out.println("  sqrt(2)        = " + BigDecimalMath.sqrt(BigDecimal.valueOf(2), mathContext));
		System.out.println("  root(2, 3)     = " + BigDecimalMath.root(BigDecimal.valueOf(2), BigDecimal.valueOf(3), mathContext));
		System.out.println("  pow(2, 3)      = " + BigDecimalMath.pow(BigDecimal.valueOf(2), BigDecimal.valueOf(3), mathContext));
		System.out.println("  pow(2.1, 3.4)  = " + BigDecimalMath.pow(BigDecimal.valueOf(2.1), BigDecimal.valueOf(3.4), mathContext));
		System.out.println("  log(2)         = " + BigDecimalMath.log(BigDecimal.valueOf(2), mathContext));
		System.out.println("  log2(2)        = " + BigDecimalMath.log2(BigDecimal.valueOf(2), mathContext));
		System.out.println("  log10(2)       = " + BigDecimalMath.log10(BigDecimal.valueOf(2), mathContext));
		System.out.println("  exp(2)         = " + BigDecimalMath.exp(BigDecimal.valueOf(2), mathContext));
		System.out.println("  sin(2)         = " + BigDecimalMath.sin(BigDecimal.valueOf(2), mathContext));
		System.out.println("  cos(2)         = " + BigDecimalMath.cos(BigDecimal.valueOf(2), mathContext));
		System.out.println("  tan(2)         = " + BigDecimalMath.tan(BigDecimal.valueOf(2), mathContext));
		System.out.println("  cot(2)         = " + BigDecimalMath.cot(BigDecimal.valueOf(2), mathContext));
		System.out.println("  asin(0.1)      = " + BigDecimalMath.asin(BigDecimal.valueOf(0.1), mathContext));
		System.out.println("  acos(0.1)      = " + BigDecimalMath.acos(BigDecimal.valueOf(0.1), mathContext));
		System.out.println("  atan(0.1)      = " + BigDecimalMath.atan(BigDecimal.valueOf(0.1), mathContext));
		System.out.println("  acot(0.1)      = " + BigDecimalMath.acot(BigDecimal.valueOf(0.1), mathContext));
		System.out.println("  sinh(2)        = " + BigDecimalMath.sinh(BigDecimal.valueOf(2), mathContext));
		System.out.println("  cosh(2)        = " + BigDecimalMath.cosh(BigDecimal.valueOf(2), mathContext));
		System.out.println("  tanh(2)        = " + BigDecimalMath.tanh(BigDecimal.valueOf(2), mathContext));
		System.out.println("  asinh(0.1)     = " + BigDecimalMath.asinh(BigDecimal.valueOf(0.1), mathContext));
		System.out.println("  acosh(2)       = " + BigDecimalMath.acosh(BigDecimal.valueOf(2), mathContext));
		System.out.println("  atanh(0.1)     = " + BigDecimalMath.atanh(BigDecimal.valueOf(0.1), mathContext));
		System.out.println("  factorial(6)   = " + BigDecimalMath.factorial(6));
		System.out.println();
		System.out.println("Constants:");
		System.out.println("  pi             = " + BigDecimalMath.pi(mathContext));
		System.out.println("  e              = " + BigDecimalMath.e(mathContext));
		System.out.println();
		System.out.println("Useful BigDecimal methods:");
		System.out.println("  mantissa(1.456E99)      = " + BigDecimalMath.mantissa(BigDecimal.valueOf(1.456E99)));
		System.out.println("  exponent(1.456E99)      = " + BigDecimalMath.exponent(BigDecimal.valueOf(1.456E99)));
		System.out.println("  integralPart(123.456)   = " + BigDecimalMath.integralPart(BigDecimal.valueOf(123.456)));
		System.out.println("  fractionalPart(123.456) = " + BigDecimalMath.fractionalPart(BigDecimal.valueOf(123.456)));
		System.out.println("  isIntValue(123)         = " + BigDecimalMath.isIntValue(BigDecimal.valueOf(123)));
		System.out.println("  isIntValue(123.456)     = " + BigDecimalMath.isIntValue(BigDecimal.valueOf(123.456)));
	}

	private static void exampleForJavaDoc_roundTrailingZeroes() {
		MathContext mc = new MathContext(5);
		System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567"), mc));    // 1.2346
		System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("123.4567"), mc));    // 123.46
		System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.001234567"), mc)); // 0.0012346
		System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23"), mc));        // 1.2300
		System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.230000"), mc));    // 1.2300
		System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.00123"), mc));     // 0.0012300
		System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0"), mc));           // 0.0000
		System.out.println(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.00000000"), mc));  // 0.0000
	}
}
