package ch.obermuhlner.math.big.example.internal;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.example.StopWatch;

public class PerformanceRegressionBigDecimalMath {

	private static final BigDecimal value0_1 = BigDecimal.valueOf(0.1);
	private static final BigDecimal value0_2 = BigDecimal.valueOf(0.2);
	private static final BigDecimal value0_3 = BigDecimal.valueOf(0.3);
	private static final BigDecimal value0_4 = BigDecimal.valueOf(0.4);
	private static final BigDecimal value0_5 = BigDecimal.valueOf(0.5);
	private static final BigDecimal value0_6 = BigDecimal.valueOf(0.6);
	private static final BigDecimal value0_7 = BigDecimal.valueOf(0.7);
	private static final BigDecimal value0_8 = BigDecimal.valueOf(0.8);
	private static final BigDecimal value0_9 = BigDecimal.valueOf(0.9);
	private static final BigDecimal value1 = BigDecimal.valueOf(1);
	private static final BigDecimal value2 = BigDecimal.valueOf(2);
	private static final BigDecimal value3 = BigDecimal.valueOf(3);
	private static final BigDecimal value9 = BigDecimal.valueOf(9);
	private static final BigDecimal value10 = BigDecimal.valueOf(10);
	private static final BigDecimal value100 = BigDecimal.valueOf(100);

	public static void main(String[] args) {
		performanceRegression(300);
	}

	private static void performanceRegression(int precision) {
		MathContext mathContext = new MathContext(precision);
		
		System.out.printf("%-20s, %15s, %15s, %15s\n", "Name", "Average [ns]", "Min [ns]", "Max [ns]");
		
		measurePerformance("log(0.1)", mathContext, () -> BigDecimalMath.log(value0_1, mathContext));
		measurePerformance("log(0.2)", mathContext, () -> BigDecimalMath.log(value0_2, mathContext));
		measurePerformance("log(0.3)", mathContext, () -> BigDecimalMath.log(value0_3, mathContext));
		measurePerformance("log(0.4)", mathContext, () -> BigDecimalMath.log(value0_4, mathContext));
		measurePerformance("log(0.5)", mathContext, () -> BigDecimalMath.log(value0_5, mathContext));
		measurePerformance("log(0.6)", mathContext, () -> BigDecimalMath.log(value0_6, mathContext));
		measurePerformance("log(0.7)", mathContext, () -> BigDecimalMath.log(value0_7, mathContext));
		measurePerformance("log(0.8)", mathContext, () -> BigDecimalMath.log(value0_8, mathContext));
		measurePerformance("log(0.9)", mathContext, () -> BigDecimalMath.log(value0_9, mathContext));
		measurePerformance("log(1)", mathContext, () -> BigDecimalMath.log(value1, mathContext));
		measurePerformance("log(2)", mathContext, () -> BigDecimalMath.log(value2, mathContext));
		measurePerformance("log(10)", mathContext, () -> BigDecimalMath.log(value10, mathContext));
		measurePerformance("log(100)", mathContext, () -> BigDecimalMath.log(value100, mathContext));
		measurePerformance("log2(2)", mathContext, () -> BigDecimalMath.log2(value2, mathContext));
		measurePerformance("log10(2)", mathContext, () -> BigDecimalMath.log10(value2, mathContext));
		measurePerformance("exp(2)", mathContext, () -> BigDecimalMath.exp(value2, mathContext));
		measurePerformance("sqrt(2)", mathContext, () -> BigDecimalMath.sqrt(value2, mathContext));
		measurePerformance("sqrt(9)", mathContext, () -> BigDecimalMath.sqrt(value9, mathContext));
		measurePerformance("pow(2;3)", mathContext, () -> BigDecimalMath.pow(value2, value3, mathContext));
		measurePerformance("pow(2;0.1)", mathContext, () -> BigDecimalMath.pow(value2, value0_1, mathContext));
		measurePerformance("root(2;3)", mathContext, () -> BigDecimalMath.root(value2, value3, mathContext));
		measurePerformance("sin(2)", mathContext, () -> BigDecimalMath.sin(value2, mathContext));
		measurePerformance("sin(100)", mathContext, () -> BigDecimalMath.sin(value100, mathContext));
		measurePerformance("cos(2)", mathContext, () -> BigDecimalMath.cos(value2, mathContext));
		measurePerformance("cos(100)", mathContext, () -> BigDecimalMath.cos(value100, mathContext));
		measurePerformance("tan(2)", mathContext, () -> BigDecimalMath.tan(value2, mathContext));
		measurePerformance("cot(2)", mathContext, () -> BigDecimalMath.cot(value2, mathContext));
		measurePerformance("asin(0.1)", mathContext, () -> BigDecimalMath.asin(value0_1, mathContext));
		measurePerformance("asin(0.8)", mathContext, () -> BigDecimalMath.asin(value0_8, mathContext));
		measurePerformance("acos(0.1)", mathContext, () -> BigDecimalMath.acos(value0_1, mathContext));
		measurePerformance("atan(0.1)", mathContext, () -> BigDecimalMath.atan(value0_1, mathContext));
		measurePerformance("acot(0.1)", mathContext, () -> BigDecimalMath.acot(value0_1, mathContext));
		measurePerformance("sinh(2)", mathContext, () -> BigDecimalMath.sinh(value2, mathContext));
		measurePerformance("cosh(2)", mathContext, () -> BigDecimalMath.cosh(value2, mathContext));
		measurePerformance("tanh(2)", mathContext, () -> BigDecimalMath.tanh(value2, mathContext));
		measurePerformance("asinh(0.1)", mathContext, () -> BigDecimalMath.asinh(value0_1, mathContext));
		measurePerformance("acosh(2)", mathContext, () -> BigDecimalMath.acosh(value2, mathContext));
		measurePerformance("atanh(0.1)", mathContext, () -> BigDecimalMath.atanh(value0_1, mathContext));
		measurePerformance("acoth(2)", mathContext, () -> BigDecimalMath.acoth(value2, mathContext));
		measurePerformance("coth(2)", mathContext, () -> BigDecimalMath.coth(value2, mathContext));
	}

	private static void measurePerformance(String name, MathContext mathContext, Runnable function) {
		int warmup = 1000;
		for (int i = 0; i < warmup; i++) {
			function.run();
		}

		int count = 1000;
		long totalNanos = 0;
		long minNanos = Long.MAX_VALUE;
		long maxNanos = Long.MIN_VALUE;
		for (int i = 0; i < count; i++) {
			StopWatch stopWatch = new StopWatch();
			function.run();
			long nanos = stopWatch.getElapsedNanos();
			
			totalNanos += nanos;
			minNanos = Math.min(minNanos, nanos);
			maxNanos = Math.max(maxNanos, nanos);
		}
		
		long avgNanos = totalNanos / count;
		System.out.printf("%-20s, %15d, %15d, %15d\n", name, avgNanos, minNanos, maxNanos);
	}
}
