package ch.obermuhlner.math.big.example.internal;

import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.BigFloat;
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
		try {
			PrintStream out = new PrintStream(new FileOutputStream("performance.csv"));
			performanceRegression(out, 300);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void performanceRegression(PrintStream out, int precision) {
		MathContext mathContext = new MathContext(precision);

		out.printf("%-20s, %15s, %15s, %15s, %15s\n", "Name", "Average [ns]", "Median [ns]", "Min [ns]", "Max [ns]");
		
		measurePerformance(out, "log(0.1)", mathContext, () -> BigDecimalMath.log(value0_1, mathContext));
		measurePerformance(out, "log(0.2)", mathContext, () -> BigDecimalMath.log(value0_2, mathContext));
		measurePerformance(out, "log(0.3)", mathContext, () -> BigDecimalMath.log(value0_3, mathContext));
		measurePerformance(out, "log(0.4)", mathContext, () -> BigDecimalMath.log(value0_4, mathContext));
		measurePerformance(out, "log(0.5)", mathContext, () -> BigDecimalMath.log(value0_5, mathContext));
		measurePerformance(out, "log(0.6)", mathContext, () -> BigDecimalMath.log(value0_6, mathContext));
		measurePerformance(out, "log(0.7)", mathContext, () -> BigDecimalMath.log(value0_7, mathContext));
		measurePerformance(out, "log(0.8)", mathContext, () -> BigDecimalMath.log(value0_8, mathContext));
		measurePerformance(out, "log(0.9)", mathContext, () -> BigDecimalMath.log(value0_9, mathContext));
		measurePerformance(out, "log(1)", mathContext, () -> BigDecimalMath.log(value1, mathContext));
		measurePerformance(out, "log(2)", mathContext, () -> BigDecimalMath.log(value2, mathContext));
		measurePerformance(out, "log(10)", mathContext, () -> BigDecimalMath.log(value10, mathContext));
		measurePerformance(out, "log(100)", mathContext, () -> BigDecimalMath.log(value100, mathContext));
		measurePerformance(out, "log2(2)", mathContext, () -> BigDecimalMath.log2(value2, mathContext));
		measurePerformance(out, "log10(2)", mathContext, () -> BigDecimalMath.log10(value2, mathContext));
		measurePerformance(out, "exp(2)", mathContext, () -> BigDecimalMath.exp(value2, mathContext));
		measurePerformance(out, "sqrt(2)", mathContext, () -> BigDecimalMath.sqrt(value2, mathContext));
		measurePerformance(out, "sqrt(9)", mathContext, () -> BigDecimalMath.sqrt(value9, mathContext));
		measurePerformance(out, "pow(2;3)", mathContext, () -> BigDecimalMath.pow(value2, value3, mathContext));
		measurePerformance(out, "pow(2;0.1)", mathContext, () -> BigDecimalMath.pow(value2, value0_1, mathContext));
		measurePerformance(out, "root(2;3)", mathContext, () -> BigDecimalMath.root(value2, value3, mathContext));
		measurePerformance(out, "sin(2)", mathContext, () -> BigDecimalMath.sin(value2, mathContext));
		measurePerformance(out, "sin(100)", mathContext, () -> BigDecimalMath.sin(value100, mathContext));
		measurePerformance(out, "cos(2)", mathContext, () -> BigDecimalMath.cos(value2, mathContext));
		measurePerformance(out, "cos(100)", mathContext, () -> BigDecimalMath.cos(value100, mathContext));
		measurePerformance(out, "tan(2)", mathContext, () -> BigDecimalMath.tan(value2, mathContext));
		measurePerformance(out, "cot(2)", mathContext, () -> BigDecimalMath.cot(value2, mathContext));
		measurePerformance(out, "asin(0.1)", mathContext, () -> BigDecimalMath.asin(value0_1, mathContext));
		measurePerformance(out, "asin(0.8)", mathContext, () -> BigDecimalMath.asin(value0_8, mathContext));
		measurePerformance(out, "acos(0.1)", mathContext, () -> BigDecimalMath.acos(value0_1, mathContext));
		measurePerformance(out, "atan(0.1)", mathContext, () -> BigDecimalMath.atan(value0_1, mathContext));
		measurePerformance(out, "acot(0.1)", mathContext, () -> BigDecimalMath.acot(value0_1, mathContext));
		measurePerformance(out, "sinh(2)", mathContext, () -> BigDecimalMath.sinh(value2, mathContext));
		measurePerformance(out, "cosh(2)", mathContext, () -> BigDecimalMath.cosh(value2, mathContext));
		measurePerformance(out, "tanh(2)", mathContext, () -> BigDecimalMath.tanh(value2, mathContext));
		measurePerformance(out, "asinh(0.1)", mathContext, () -> BigDecimalMath.asinh(value0_1, mathContext));
		measurePerformance(out, "acosh(2)", mathContext, () -> BigDecimalMath.acosh(value2, mathContext));
		measurePerformance(out, "atanh(0.1)", mathContext, () -> BigDecimalMath.atanh(value0_1, mathContext));
		measurePerformance(out, "acoth(2)", mathContext, () -> BigDecimalMath.acoth(value2, mathContext));
		measurePerformance(out, "coth(2)", mathContext, () -> BigDecimalMath.coth(value2, mathContext));
		measurePerformance(out, "atan2(2;3)", mathContext, () -> BigDecimalMath.atan2(value2, value3, mathContext));
		measurePerformance(out, "gamma(0.1)", mathContext, () -> BigDecimalMath.gamma(value0_1, mathContext));

		measurePerformance(out, "BigFloat.add", mathContext, () -> BigFloat.context(mathContext).valueOf(123).add(456).toBigDecimal());
		measurePerformance(out, "BigFloat.subtract", mathContext, () -> BigFloat.context(mathContext).valueOf(123).subtract(456).toBigDecimal());
		measurePerformance(out, "BigFloat.multiply", mathContext, () -> BigFloat.context(mathContext).valueOf(123).multiply(456).toBigDecimal());
		measurePerformance(out, "BigFloat.divide", mathContext, () -> BigFloat.context(mathContext).valueOf(123).divide(456).toBigDecimal());
	}

	private static void measurePerformance(PrintStream out, String name, MathContext mathContext, Runnable function) {
		try {
			int warmup = 1000;
			for (int i = 0; i < warmup; i++) {
				function.run();
			}

			int count = 100;
			int runCount = 10;
			double totalNanos = 0;
			double minNanos = Double.MAX_VALUE;
			double maxNanos = 0;
			List<Double> allNanos = new ArrayList<>();

			for (int i = 0; i < count; i++) {
				StopWatch stopWatch = new StopWatch();
				for (int j = 0; j < runCount; j++) {
					function.run();
				}
				double nanos = ((double) stopWatch.getElapsedNanos()) / runCount;

				totalNanos += nanos;
				minNanos = Math.min(minNanos, nanos);
				maxNanos = Math.max(maxNanos, nanos);
				allNanos.add(nanos);
			}

			Double avgNanos = totalNanos / count;
			Double medianNanos = median(allNanos);

			System.out.printf("%-20s: %15.1f\n", name, medianNanos);
			out.printf("%-20s, %15.1f, %15.1f, %15.1f, %15.1f\n", name, avgNanos, medianNanos, minNanos, maxNanos);
		} catch (Throwable e) {
			System.out.printf("%-20s: %15s %s\n", name, e.getClass(), e.getMessage());
			out.printf("%-20s, %15s, %15s, %15s, %15s\n", name, "", "", "", "");
		}
	}

	private static double median(List<Double> values) {
		Collections.sort(values);
		int center = values.size() / 2;
		if (values.size() % 2 == 0) {
			return (values.get(center) + values.get(center+1)) / 2;
		} else {
			return values.get(center);
		}
	}
}
