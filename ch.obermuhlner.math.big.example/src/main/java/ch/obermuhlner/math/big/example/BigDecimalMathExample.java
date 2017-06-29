package ch.obermuhlner.math.big.example;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.BigDecimalMath;

public class BigDecimalMathExample {

	public static void main(String[] args) {
		MathContext mathContext = new MathContext(100);
		
		System.out.println("All calculations with a precision of " + mathContext.getPrecision() + " digits.");
		System.out.println();
		System.out.println("Advanced functions:");
		System.out.println("  sqrt(2)        = " + BigDecimalMath.sqrt(new BigDecimal("2"), mathContext));
		System.out.println("  root(2, 3)     = " + BigDecimalMath.root(new BigDecimal("2"), new BigDecimal("3"), mathContext));
		System.out.println("  pow(2, 3)      = " + BigDecimalMath.pow(new BigDecimal("2"), new BigDecimal("3"), mathContext));
		System.out.println("  pow(2.1, 3.4)  = " + BigDecimalMath.pow(new BigDecimal("2.1"), new BigDecimal("3.4"), mathContext));
		System.out.println("  log(2)         = " + BigDecimalMath.log(new BigDecimal("2"), mathContext));
		System.out.println("  log2(2)        = " + BigDecimalMath.log2(new BigDecimal("2"), mathContext));
		System.out.println("  log10(2)       = " + BigDecimalMath.log10(new BigDecimal("2"), mathContext));
		System.out.println("  exp(2)         = " + BigDecimalMath.exp(new BigDecimal("2"), mathContext));
		System.out.println("  sin(2)         = " + BigDecimalMath.sin(new BigDecimal("2"), mathContext));
		System.out.println("  cos(2)         = " + BigDecimalMath.cos(new BigDecimal("2"), mathContext));
		System.out.println("  tan(2)         = " + BigDecimalMath.tan(new BigDecimal("2"), mathContext));
		System.out.println("  cot(2)         = " + BigDecimalMath.cot(new BigDecimal("2"), mathContext));
		System.out.println("  asin(0.1)      = " + BigDecimalMath.asin(new BigDecimal("0.1"), mathContext));
		System.out.println("  acos(0.1)      = " + BigDecimalMath.acos(new BigDecimal("0.1"), mathContext));
		System.out.println("  atan(0.1)      = " + BigDecimalMath.atan(new BigDecimal("0.1"), mathContext));
		System.out.println("  acot(0.1)      = " + BigDecimalMath.acot(new BigDecimal("0.1"), mathContext));
		System.out.println("  sinh(2)        = " + BigDecimalMath.sinh(new BigDecimal("2"), mathContext));
		System.out.println("  cosh(2)        = " + BigDecimalMath.cosh(new BigDecimal("2"), mathContext));
		System.out.println("  tanh(2)        = " + BigDecimalMath.tanh(new BigDecimal("2"), mathContext));
		System.out.println("  asinh(0.1)     = " + BigDecimalMath.asinh(new BigDecimal("0.1"), mathContext));
		System.out.println("  acosh(2)       = " + BigDecimalMath.acosh(new BigDecimal("2"), mathContext));
		System.out.println("  atanh(0.1)     = " + BigDecimalMath.atanh(new BigDecimal("0.1"), mathContext));
		System.out.println("  factorial(6)   = " + BigDecimalMath.factorial(6));
		System.out.println();
		System.out.println("Constants:");
		System.out.println("  pi             = " + BigDecimalMath.pi(mathContext));
		System.out.println("  e              = " + BigDecimalMath.e(mathContext));
		System.out.println();
		System.out.println("Useful BigDecimal methods:");
		System.out.println("  mantissa(1.456E99)      = " + BigDecimalMath.mantissa(new BigDecimal("1.456E99")));
		System.out.println("  exponent(1.456E99)      = " + BigDecimalMath.exponent(new BigDecimal("1.456E99")));
		System.out.println("  integralPart(123.456)   = " + BigDecimalMath.integralPart(new BigDecimal("123.456")));
		System.out.println("  fractionalPart(123.456) = " + BigDecimalMath.fractionalPart(new BigDecimal("123.456")));
		System.out.println("  isIntValue(123)         = " + BigDecimalMath.isIntValue(new BigDecimal("123")));
		System.out.println("  isIntValue(123.456)     = " + BigDecimalMath.isIntValue(new BigDecimal("123.456")));
	}
}
