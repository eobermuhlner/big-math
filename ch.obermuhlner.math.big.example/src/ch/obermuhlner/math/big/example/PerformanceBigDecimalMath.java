package ch.obermuhlner.math.big.example;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.BiFunction;

import ch.obermuhlner.math.big.BigDecimalMath;

public class PerformanceBigDecimalMath {

	public static void main(String[] args) {

//		System.out.println(BigDecimalMath.log(BigDecimal.valueOf(8), new MathContext(1100)));
		
		performanceReportStandardFunctions();
//		performanceReportSlowFunctions();
//		performanceReportVerySlowFunctions();

//		performanceReportLogBigRange();

//		performanceReportLogDebug();

//		performanceReportOverPrecision();
	}

	private static void performanceReportStandardFunctions() {
		MathContext mathContext = new MathContext(1000);

		printHeaders("x", "exp", "sqrt", "root2", "root3", "sin", "cos");
		performanceReportOverValue(
				mathContext,
				+0,
				+2.0,
				+0.01,
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sqrt(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(new BigDecimal(2), x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(new BigDecimal(3), x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sin(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.cos(x, calculationMathContext));
	}

	private static void performanceReportSlowFunctions() {
		MathContext mathContext = new MathContext(300);

		printHeaders("x", "exp", "log", "log2", "log10");
		performanceReportOverValue(
				mathContext,
				+0.01,
				+2.0,
				+0.01,
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.log2(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.log10(x, calculationMathContext));
	}

	private static void performanceReportVerySlowFunctions() {
		MathContext mathContext = new MathContext(200);

		printHeaders("x", "log", "pow");
		performanceReportOverValue(
				mathContext,
				+0.01,
				+2.0,
				+0.01,
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void performanceReportLogBigRange() {
		MathContext mathContext = new MathContext(300);

		printHeaders("x", "log");
		performanceReportOverValue(
				mathContext,
				+1,
				+100,
				+1,
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext));
	}

//	private static void performanceReportLogDebug() {
//		MathContext mathContext = new MathContext(300);
//
//		printHeaders("x", "log", "usingRoot");
//		performanceReportOverValue(
//				mathContext,
//				0.01,
//				10,
//				+0.01,
//				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
//				(x, calculationMathContext) -> BigDecimalMath.logUsingRoot(x, calculationMathContext));
//	}

	private static void performanceReportOverPrecision() {
		printHeaders("precision", "exp", "log", "pow");
		performanceReportOverPrecision(
				BigDecimal.valueOf(3.1),
				10,
				1000,
				10,
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void printHeaders(String... headers) {
		for (int i = 0; i < headers.length; i++) {
			if (i != 0) {
				System.out.print(",");
			}
			System.out.printf("%6s", headers[i]);
		}
		System.out.println();
	}

	@SafeVarargs
	private static void performanceReportOverValue(MathContext mathContext, double xStart, double xEnd, double xStep, BiFunction<BigDecimal, MathContext, BigDecimal>... calculations) {
		performanceReportOverValue(mathContext, BigDecimal.valueOf(xStart), BigDecimal.valueOf(xEnd), BigDecimal.valueOf(xStep), calculations);
	}

	private static void performanceReportOverValue(MathContext mathContext, BigDecimal xStart, BigDecimal xEnd, BigDecimal xStep, BiFunction<BigDecimal, MathContext, BigDecimal>... calculations) {
		// warmup
		for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
			for (BiFunction<BigDecimal, MathContext, BigDecimal> calculation : calculations) {
				try {
					calculation.apply(x, MathContext.DECIMAL32);
				}
				catch (ArithmeticException ex) {
					// ignore
				}
			}
		}

		// real measurement
		for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
			long[] elapsedMillis = new long[calculations.length];

			for (int i = 0; i < calculations.length; i++) {
				BiFunction<BigDecimal, MathContext, BigDecimal> calculation = calculations[i];

				StopWatch stopWatch = new StopWatch();
				try {
					calculation.apply(x, mathContext);
					elapsedMillis[i] = stopWatch.getElapsedMillis();
				}
				catch (ArithmeticException ex) {
					// ignore
				}

			}

			System.out.printf("%6.3f", x);
			for (int i = 0; i < elapsedMillis.length; i++) {
				System.out.print(",");
				System.out.printf("%6d", elapsedMillis[i]);
			}
			System.out.println();
		}
	}

	private static void performanceReportOverPrecision(BigDecimal value, int precisionStart, int precisionEnd, int precisionStep, BiFunction<BigDecimal, MathContext, BigDecimal>... calculations) {
		// warmup
		for (int i = 0; i < 1000; i++) {
			for (BiFunction<BigDecimal, MathContext, BigDecimal> calculation : calculations) {
				try {
					calculation.apply(value, MathContext.DECIMAL32);
				}
				catch (ArithmeticException ex) {
					// ignore
				}
			}
		}

		// real measurement
		for (int precision = precisionStart; precision < precisionEnd; precision += precisionStep) {
			long[] elapsedMillis = new long[calculations.length];
			MathContext mathContext = new MathContext(precision);

			for (int i = 0; i < calculations.length; i++) {
				BiFunction<BigDecimal, MathContext, BigDecimal> calculation = calculations[i];

				StopWatch stopWatch = new StopWatch();

				try {
					calculation.apply(value, mathContext);
					elapsedMillis[i] = stopWatch.getElapsedMillis();
				}
				catch (ArithmeticException ex) {
					// ignore
				}
			}

			System.out.printf("%9d", precision);
			for (int i = 0; i < elapsedMillis.length; i++) {
				System.out.print(",");
				System.out.printf("%6d", elapsedMillis[i]);
			}
			System.out.println();
		}
	}
}
