package ch.obermuhlner.math.big.example.internal;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.example.StopWatch;

/**
 * Performance measurements for the functions in {@link BigDecimalMath}.
 */
public class PerformanceBigDecimalMath {

	private static final BigDecimal TWO = new BigDecimal(2);
	private static final BigDecimal ONE_HALF = new BigDecimal(0.5);

	private static MathContext REF_MATHCONTEXT = new MathContext(300);
	private static int REPEATS = 20;
	private static int FAST_REPEATS = 200;

	private static final MathContext WARMUP_MATHCONTEXT = MathContext.DECIMAL128;
	private static final int WARMUP_LOOP_REPEATS = 10;
	private static final int WARMUP_REPEATS = 1000;

	private static final double AVERAGE_PERCENTILE = 0.50;

	private static final String OUTPUT_DIRECTORY = "docu/benchmarks/";

	public static void main(String[] args) {
		StopWatch stopWatch = new StopWatch();

		fullReport();
		//fullOptimizationReport();

		//performanceReport_Fast_0_to_10();

		//performanceReport_toBigDecimal_optimization();
		//performanceReport_toBigDecimal();

		//performanceReport_factorial_optimization();

		System.out.println("Finished all in " + stopWatch);
	}

	private static String createStringNumber(int length) {
		StringBuilder result = new StringBuilder();

		if (length == 0) {
			result.append("0");
		}

		for (int i = 0; i < length; i++) {
			result.append(i % 10);
		}
		return result.toString();
	}

	private static String createStringNumber(int length, Random random) {
		StringBuilder result = new StringBuilder();

		if (length == 0) {
			result.append(random.nextInt(10));
		}

		result.append(random.nextInt(9) + 1);
		for (int i = 1; i < length; i++) {
			result.append(random.nextInt(10));
		}
		return result.toString();
	}

	public static void fullReport() {
		performanceReport_Fast_0_to_2();
		performanceReport_Fast_neg10_to_10();
		performanceReport_Fast_0_to_10();
		performanceReport_Fast_0_to_100();

		performanceReport_Trigo_0_to_1();
		performanceReport_Hyperbolic_0_to_2();

		performanceReport_Slow_0_to_2();
		performanceReport_Slow_neg10_to_10();
		performanceReport_Slow_0_to_10();
		performanceReport_Slow_0_to_100();
		performanceReport_Slow_0_to_1000();

		performanceReport_pow_0_to_100();

		performanceReport_pow_frac_0_to_1000();

		performanceReport_pow_int_0_to_10000();
		performanceReport_pow_int_0_to_100000();

		performanceReport_Fast_precision();
		performanceReport_Slow_precision();

		performanceReportAtan2_y_neg10_to_10_x_5();
		performanceReportAtan2_y_5_x_neg10_to_10();
		performanceReportAtan2_yx_neg10_to_10();
		functionValueAtan2_yx_neg10_to_10();

		performanceReport_toBigDecimal();

		performanceReport_Java9_sqrt();
	}

	public static void fullOptimizationReport() {
		// --- exp() optimizations:
		performanceReportExpOptimization_0_to_4();

		// --- asin() optimizations:
		performanceReportAsinOptimization_0_to_1();

		// --- sqrt() optimizations:
		performanceReportSqrtOptimization_0_to_1();
		performanceReportSqrtOptimization_0_to_100();
		performanceReportSqrtOptimization_0_to_1000();

		// --- root() optimizations:
		performanceReportRootOptimization_0_to_10();

		// --- log() optimizations:
		performanceReportLogNewtonAdaptive_0_to_10();

		performanceReportLogOptimizationNewton_0_to_10();
		performanceReportLogOptimizationNewton_0_to_100();

//		performanceReportLogOptimizationTry();
//		performanceReportLogOptimization1();
//		performanceReportLogOptimization2();
//		performanceReportLogOptimization3();
//		performanceReportLogOptimization4();
//		performanceReportLogOptimization5();
//		performanceReportLogOptimization6();

		performanceReport_toBigDecimal_optimization();
	}

	private static void performanceReport_Fast_0_to_2() {
		performanceReportOverValue(
				"perf_fast_funcs_from_0_to_2.csv",
				REF_MATHCONTEXT,
				0,
				+2.0,
				+0.005,
				REPEATS,
				Arrays.asList("exp", "sqrt", "root2", "root3", "sin", "cos", "tan"),
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sqrt(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(x, new BigDecimal(2), calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(x, new BigDecimal(3), calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sin(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.cos(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.tan(x, calculationMathContext));
	}

	private static void performanceReport_Fast_neg10_to_10() {
		performanceReportOverValue(
				"perf_fast_funcs_from_-10_to_10.csv",
				REF_MATHCONTEXT,
				-10,
				+10,
				+0.1,
				REPEATS,
				Arrays.asList("exp", "sqrt", "root2", "root3", "sin", "cos", "tan"),
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sqrt(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(x, new BigDecimal(2), calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(x, new BigDecimal(3), calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sin(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.cos(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.tan(x, calculationMathContext));
	}

	private static void performanceReport_Fast_0_to_10() {
		performanceReportOverValue(
				"perf_fast_funcs_from_0_to_10.csv",
				REF_MATHCONTEXT,
				0,
				10,
				+0.01,
				REPEATS,
				Arrays.asList("exp", "sqrt", "root2", "root3", "sin", "cos", "tan"),
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sqrt(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(x, new BigDecimal(2), calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(x, new BigDecimal(3), calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sin(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.cos(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.tan(x, calculationMathContext));
	}

	private static void performanceReport_Fast_0_to_100() {
		performanceReportOverValue(
				"perf_fast_funcs_from_0_to_100.csv",
				REF_MATHCONTEXT,
				0,
				+100,
				+0.1,
				REPEATS,
				Arrays.asList("exp", "sqrt", "root2", "root3", "sin", "cos", "tan"),
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sqrt(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(x, new BigDecimal(2), calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(x, new BigDecimal(3), calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sin(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.cos(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.tan(x, calculationMathContext));
	}

	private static void performanceReport_Trigo_0_to_1() {
		performanceReportOverValue(
				"perf_trigo_funcs_from_0_to_1.csv",
				REF_MATHCONTEXT,
				0,
				+1.0,
				+0.01,
				REPEATS,
				Arrays.asList("sin", "cos", "tan", "asin", "acos", "atan"),
				(x, calculationMathContext) -> BigDecimalMath.sin(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.cos(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.tan(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.asin(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.acos(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.atan(x, calculationMathContext));
	}

	private static void performanceReport_Hyperbolic_0_to_2() {
		performanceReportOverValue(
				"perf_hyperbolic_funcs_from_0_to_2.csv",
				REF_MATHCONTEXT,
				0,
				+2.0,
				+0.01,
				REPEATS,
				Arrays.asList("sinh", "cosh", "tanh", "asinh", "acosh", "atanh"),
				(x, calculationMathContext) -> BigDecimalMath.sinh(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.cosh(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.tanh(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.asinh(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.acosh(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.atanh(x, calculationMathContext));
	}

	private static void performanceReport_Slow_0_to_2() {
		performanceReportOverValue(
				"perf_slow_funcs_from_0_to_2.csv",
				REF_MATHCONTEXT,
				0,
				+2.0,
				+0.005,
				REPEATS,
				Arrays.asList("exp", "log", "pow"),
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void performanceReport_Slow_neg10_to_10() {
		performanceReportOverValue(
				"perf_slow_funcs_from_-10_to_10.csv",
				REF_MATHCONTEXT,
				-10.0,
				+10.0,
				+0.1,
				REPEATS,
				Arrays.asList("exp", "log", "pow"),
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void performanceReport_Slow_0_to_10() {
		performanceReportOverValue(
				"perf_slow_funcs_from_0_to_10.csv",
				REF_MATHCONTEXT,
				0.0,
				+10.0,
				+0.05,
				REPEATS,
				Arrays.asList("exp", "log", "pow"),
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void performanceReport_Slow_0_to_100() {
		performanceReportOverValue(
				"perf_slow_funcs_from_0_to_100.csv",
				REF_MATHCONTEXT,
				0,
				+100,
				+0.1,
				REPEATS,
				Arrays.asList("exp", "log", "pow"),
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void performanceReport_Slow_0_to_1000() {
		performanceReportOverValue(
				"perf_slow_funcs_from_0_to_1000.csv",
				REF_MATHCONTEXT,
				0,
				+1000,
				5,
				REPEATS,
				Arrays.asList("exp", "log", "pow"),
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void performanceReport_pow_0_to_100() {
		performanceReportOverValue(
				"perf_pow_from_0_to_100.csv",
				REF_MATHCONTEXT,
				0,
				+100,
				+0.1,
				REPEATS,
				Arrays.asList("pow"),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void performanceReport_pow_frac_0_to_1000() {
		performanceReportOverValue(
				"perf_pow_frac_from_0_to_1000.csv",
				REF_MATHCONTEXT,
				0.5,
				+1000,
				+5,
				REPEATS,
				Arrays.asList("pow"),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void performanceReport_pow_int_0_to_10000() {
		performanceReportOverValue(
				"perf_pow_int_from_0_to_10000.csv",
				REF_MATHCONTEXT,
				0,
				+10000,
				+1,
				REPEATS,
				Arrays.asList("pow"),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void performanceReport_pow_int_0_to_100000() {
		performanceReportOverValue(
				"perf_pow_int_from_0_to_100000.csv",
				REF_MATHCONTEXT,
				0,
				+100000,
				+1,
				REPEATS,
				Arrays.asList("pow"),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void performanceReport_Fast_precision() {
		performanceReportOverPrecision(
				"perf_fast_funcs_precisions_to_1000.csv",
				BigDecimal.valueOf(3.1),
				10,
				1000,
				10,
				REPEATS,
				Arrays.asList("exp", "sqrt", "root2", "root3", "sin", "cos"),
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sqrt(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(x, new BigDecimal(2), calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.root(x, new BigDecimal(3), calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.sin(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.cos(x, calculationMathContext));
	}

	private static void performanceReport_Slow_precision() {
		performanceReportOverPrecision(
				"perf_slow_funcs_precisions_to_1000.csv",
				BigDecimal.valueOf(3.1),
				10,
				1000,
				10,
				REPEATS,
				Arrays.asList("exp", "log", "pow"),
				(x, calculationMathContext) -> BigDecimalMath.exp(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.log(x, calculationMathContext),
				(x, calculationMathContext) -> BigDecimalMath.pow(BigDecimal.valueOf(123.456), x, calculationMathContext));
	}

	private static void performanceReportExpOptimization_0_to_4() {
		performanceReportOverValue(
				"test_exp_impl_from_0_to_4.csv",
				REF_MATHCONTEXT,
				0,
				+4,
				+0.01,
				REPEATS,
				Arrays.asList("exp", "exp_256", "exp_1024", "exp_65536"),
				(x1, mc1) -> BigDecimalMathExperimental.exp(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.expReducing(x1, mc1, 256),
				(x1, mc1) -> BigDecimalMathExperimental.expReducing(x1, mc1, 1024),
				(x1, mc1) -> BigDecimalMathExperimental.expReducing(x1, mc1, 65536));
	}

	private static void performanceReportAsinOptimization_0_to_1() {
		performanceReportOverValue(
				"test_asin_impl_from_0_to_1.csv",
				REF_MATHCONTEXT,
				0,
				+1,
				+0.01,
				REPEATS,
				Arrays.asList("sin", "asinTaylor", "asinNewton"),
				(x1, mc1) -> BigDecimalMath.sin(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.asin(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.asinUsingNewton(x1, mc1));
	}

	private static void performanceReportSqrtOptimization_0_to_1() {
		System.out.println(BigDecimalMathExperimental.sqrtUsingHalley(BigDecimal.valueOf(2), new MathContext(100)));

		performanceReportOverValue(
				"test_sqrt_impl_from_0_to_1.csv",
				REF_MATHCONTEXT,
				0,
				+1,
				+0.01,
				REPEATS,
				Arrays.asList(
						"sqrtNewtonFix",
						"sqrtNewtonAdaptive",
						"sqrtNewtonAdaptiveImproved",
						"sqrtHalleyFix"),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingNewton(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingNewtonAdaptivePrecision(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingNewtonAdaptivePrecisionImproved(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingHalley(x1, mc1));
	}

	private static void performanceReportSqrtOptimization_0_to_100() {
		System.out.println(BigDecimalMathExperimental.sqrtUsingHalley(BigDecimal.valueOf(2), new MathContext(100)));

		performanceReportOverValue(
				"test_sqrt_impl_from_0_to_100.csv",
				REF_MATHCONTEXT,
				0,
				+100,
				+0.01,
				REPEATS,
				Arrays.asList(
						"sqrtNewtonFix",
						"sqrtNewtonAdaptive",
						"sqrtNewtonAdaptiveImproved",
						"sqrtHalleyFix"),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingNewton(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingNewtonAdaptivePrecision(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingNewtonAdaptivePrecisionImproved(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingHalley(x1, mc1));
	}

	private static void performanceReportSqrtOptimization_0_to_1000() {
		System.out.println(BigDecimalMathExperimental.sqrtUsingHalley(BigDecimal.valueOf(2), new MathContext(100)));

		performanceReportOverValue(
				"test_sqrt_impl_from_0_to_1000.csv",
				REF_MATHCONTEXT,
				0,
				+1000,
				+0.1,
				REPEATS,
				Arrays.asList(
						"sqrtNewtonFix",
						"sqrtNewtonAdaptive",
						"sqrtNewtonAdaptiveImproved",
						"sqrtHalleyFix"),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingNewton(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingNewtonAdaptivePrecision(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingNewtonAdaptivePrecisionImproved(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.sqrtUsingHalley(x1, mc1));
	}

	private static void performanceReportRootOptimization_0_to_10() {
		performanceReportOverValue(
				"test_root_impl_from_0_to_10.csv",
				REF_MATHCONTEXT,
				0,
				+10,
				+0.01,
				REPEATS,
				Arrays.asList("root", "rootAdaptive"),
				(x1, mc1) -> BigDecimalMathExperimental.rootFixPrecision(BigDecimal.valueOf(3), x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.rootAdaptivePrecision(BigDecimal.valueOf(3), x1, mc1, 2));
	}

	private static void performanceReportLogNewtonAdaptive_0_to_10() {
		performanceReportOverValue(
				"test_log_adaptive_impl_from_0_to_10.csv",
				REF_MATHCONTEXT,
				0.05,
				+10,
				+0.05,
				REPEATS,
				Arrays.asList("log", "logAdaptive"),
				(x1, mc1) -> BigDecimalMathExperimental.logUsingNewtonFixPrecision(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.logUsingNewtonAdaptivePrecision(x1, mc1, 17));
	}

	private static void performanceReportAtan2_y_neg10_to_10_x_5() {
		BigDecimal x = BigDecimal.valueOf(5);
		performanceReportOverValue(
				"perf_atan2_y_from_-10_to_10_x_5.csv",
				REF_MATHCONTEXT,
				-10,
				10,
				+0.1,
				REPEATS,
				Arrays.asList("atan2"),
				(y1, mc1) -> BigDecimalMath.atan2(y1, x, mc1));
	}

	private static void performanceReportAtan2_y_5_x_neg10_to_10() {
		BigDecimal y = BigDecimal.valueOf(5);
		performanceReportOverValue(
				"perf_atan2_y_5_x_from_-10_to_10.csv",
				REF_MATHCONTEXT,
				-10,
				10,
				+0.1,
				REPEATS,
				Arrays.asList("atan2"),
				(x1, mc1) -> BigDecimalMath.atan2(y, x1, mc1));
	}

	private static void performanceReportAtan2_yx_neg10_to_10() {
		MathContext mathContext = REF_MATHCONTEXT;
		BigDecimal xStart = BigDecimal.valueOf(-10);
		BigDecimal xEnd = BigDecimal.valueOf(10);
		BigDecimal xStep = BigDecimal.valueOf(1.0);
		BigDecimal xStepWarmup = BigDecimal.valueOf(2.0);
		BigDecimal yStart = BigDecimal.valueOf(-10);
		BigDecimal yEnd = BigDecimal.valueOf(10);
		BigDecimal yStep = BigDecimal.valueOf(1.0);
		BigDecimal yStepWarmup = BigDecimal.valueOf(2.0);
		int repeats = 3;

		String name = "perf_atan2_yx_from_-10_to_10.csv";

		// warmup
		for (BigDecimal y = yStart; y.compareTo(yEnd) < 0; y = y.add(yStepWarmup)) {
			for (BigDecimal x = xStart; x.compareTo(xEnd) < 0; x = x.add(xStepWarmup)) {
				try {
					BigDecimalMath.atan2(y, x, mathContext).doubleValue();
				} catch (Exception ex) {
					// ignore
				}
			}
		}

		try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_DIRECTORY + name))) {
			for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
				writer.print(", ");
				writer.print(x);
			}
			writer.println();

			for (BigDecimal y = yStart; y.compareTo(yEnd) <= 0; y = y.add(yStep)) {
				writer.print(y);
				writer.print(", ");
				for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
					if (x.compareTo(xStart) != 0) {
						writer.print(", ");
					}
					System.out.println("x = " + x + " y = " + y);
					double result = Double.MAX_VALUE;
					try {
						for (int i = 0; i < repeats; i++) {
							StopWatch stopWatch = new StopWatch();
							BigDecimalMath.atan2(y, x, mathContext).doubleValue();
							result = Math.min(result, stopWatch.getElapsedMillis());
						}
					} catch (Exception ex) {
						result = 0;
					}
					writer.print(result);
				}
				writer.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void functionValueAtan2_yx_neg10_to_10() {
		MathContext mathContext = MathContext.DECIMAL64;
		BigDecimal xStart = BigDecimal.valueOf(-10);
		BigDecimal xEnd = BigDecimal.valueOf(10);
		BigDecimal xStep = BigDecimal.valueOf(0.2);
		BigDecimal yStart = BigDecimal.valueOf(-10);
		BigDecimal yEnd = BigDecimal.valueOf(10);
		BigDecimal yStep = BigDecimal.valueOf(0.2);

		String name = "values_atan2_yx_from_-10_to_10.csv";

		try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_DIRECTORY + name))) {
			for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
				writer.print(", ");
				writer.print(x);
			}
			writer.println();

			for (BigDecimal y = yStart; y.compareTo(yEnd) <= 0; y = y.add(yStep)) {
				writer.print(y);
				writer.print(", ");
				for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
					if (x.compareTo(xStart) != 0) {
						writer.print(", ");
					}
					System.out.println("x = " + x + " y = " + y);
					double result;
					try {
						result = BigDecimalMath.atan2(y, x, mathContext).doubleValue();
					} catch (Exception ex) {
						result = Double.NaN;
					}
					writer.print(result);
				}
				writer.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Writing an implementation of the binary logarithm function with BigDecimal is surprisingly challenging.
	 * <p>
	 * The first step is to decide between the basic approach.
	 * Here are the two basic formulas I found that converge reasonably fast:
	 * Hyperbolic tangent area:
	 * CODE logHyperbolic
	 * <p>
	 * Newton's method:
	 * CODE logNewton
	 * <p>
	 * The following graph shows the time in nanoseconds necessary to calculate the log(x) to a precision of 300 significant digits.
	 * CHART hyperbolic, newton x=0.01 - 10
	 * <p>
	 * The hyperbolic tangent area approach wins hands down.
	 * <p>
	 * Both approaches have in common that they converge much faster when x is close to 1.0 and become increasingly slower the farther away we get from 1.0.
	 * CHART hyperbolic x=0.01 - 10
	 * CHART hyperbolic x=0.1 - 100
	 * <p>
	 * In order to optimize this calculation we must bring the argument x closer to 1.0 and correct this outside of the logarithm in the appropriate manner.
	 * The Wikipedia article http://en.wikipedia.org/wiki/Natural_logarithm mentions the following trick:
	 * We write the value x in the exponential form
	 * x = a * 10<sup>b</sup>
	 * then we can say that
	 * log(x) = log(a) + log(10) * b
	 * Since we can precalculate log(10) for reasonable precision this can be implemented very efficiently with BigDecimal.
	 * <p>
	 * CODE logUsingExponent
	 * <p>
	 * CHART logHyperbolic, logUsingExponent x=0.1 - 100
	 * <p>
	 * The argument of the log() can now only be in the range from 0.1 to 10, which is already an improvement.
	 * How can we further reduce the input range of log() and get even closer to the optimum value of 1.0?
	 * <p>
	 * For values > 1.0 we can try to divide by powers of 2.
	 * For example the worst case values in the range from 6.0 to 10.0 can be divided by (2*2*2), which gives us a range from 0.75 to 1.25, another improvement.
	 * This factor can be applied outside the log() function:
	 * log(10.0) = log(10.0 / 8) + log(2)*3
	 * To be efficient we must precalculate log(2).
	 * <p>
	 * The same logic applies to values > 1.0 but in this case we multiply with powers of 2.
	 * log(0.1) = log(0.1 * 8) - log(2)*3
	 * <p>
	 * CODE logUsingPowerTwo
	 * <p>
	 * CHART logUsingExponent, logUsingPowerTwo  x=0.1 - 2
	 * <p>
	 * The powers of 2 come in a pretty good sequence in the value range between 1.0 and 10.0: (2, 4, 8).
	 * Unfortunately this leaves a gaping whole around 6.0 so that the values in this area can not be efficiently optimized.
	 * If we do the same trick by multiplying combinations of the prim factors 2 and 3 we get a nicer sequence that leaves less holes:
	 * (2, 3, 2*2, 2*3, 2*2*2, 3*3) = (2, 3, 4, 6, 8, 9)
	 * While this becomes a bit more complex to implement that just the powers of 2 it is still simple enough that we can hardcode all the combinations.
	 * <p>
	 * CHART logUsingExponent, logUsingTwoThree  x=0.1 - 2
	 * <p>
	 * <p>
	 * Another approach that does not need all the tricks with exponential form and combinations of 2 and 3.
	 * REFERENCE https://github.com/miraclefoxx/bigdecimal-math/blob/master/src/main/java/io/github/miraclefoxx/math/BigDecimalMath.java
	 * This needs another function root(n, x) which calculates the n'th root of the value x.
	 * It follows the same recipe of bringing the argument of the log() function closer to 1 by writing it in this form:
	 * log(x) = log(root(r, x)^r) = r * log(root(r, x))
	 * By estimating r correctly we can bring the argument to log() reasonably close.
	 * <p>
	 * CODE  root
	 * CODE  logUsingRoot
	 * <p>
	 * CHART logHyperbolic, logUsingRoot  x=0.1 - 10
	 * <p>
	 * Looks very nice, the calculation becomes essentially independent of the distance to 1.0.
	 * <p>
	 * Lets compare the two approaches (assuming precalculated log(10), log(2), log(3)):
	 * CHART logUsingRoot, logUsingTwoThree  x=0.1 - 10
	 * <p>
	 * Nobody stops us from combining the two approaches, we calculate the final step
	 * (after using exponential form and combinations of 2 and 3) calling logRoot().
	 * This gives us:
	 * CHART logUsingRoot, logUsingTwoThreeRoot  x=0.1 - 10
	 * <p>
	 * If we cannot precalculate log(10), log(2) and  log(3) (or the desired precision is outside the precalculated range)
	 * then the root() approach becomes more efficient:
	 * CHART logUsingRoot, logUsingTwoThreeNotPrecalculated  x=0.1 - 10
	 * <p>
	 * This gives us the final solution:
	 * If we are inside the precalculated precision we use logUsingTwoThreeRoot, otherwise logUsingRoot.
	 * <p>
	 * REFERENCE github eobermuhlner/big-math
	 */
	private static void performanceReportLogOptimizationNewton_0_to_10() {
		performanceReportOverValue(
				"test_log_impl_from_0_to_10.csv",
				REF_MATHCONTEXT,
				0.05,
				+10,
				+0.05,
				REPEATS,
				Arrays.asList("newtonFix", "newton", "root+newton", "primes+newton", "primes+root+newton"),
				(x1, mc1) -> BigDecimalMathExperimental.logUsingNewtonFixPrecision(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.logUsingNewtonAdaptivePrecision(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.logUsingRoot(x1, mc1,
						BigDecimalMathExperimental::logUsingNewtonAdaptivePrecision),
				(x1, mc1) -> BigDecimalMathExperimental.logUsingPrimes(x1, mc1,
						BigDecimalMathExperimental::logUsingNewtonAdaptivePrecision),
				(x1, mc1) -> BigDecimalMathExperimental.logUsingPrimes(x1, mc1,
						(x2, mc2) -> BigDecimalMathExperimental.logUsingRoot(x1, mc2,
								BigDecimalMathExperimental::logUsingNewtonAdaptivePrecision)));
	}

	private static void performanceReportLogOptimizationNewton_0_to_100() {
		performanceReportOverValue(
				"test_log_impl_from_0_to_100.csv",
				REF_MATHCONTEXT,
				0.1,
				+100,
				+0.1,
				REPEATS,
				Arrays.asList("newton", "root+newton", "exp+primes+newton", "primes+root+newton"),
				(x1, mc1) -> BigDecimalMathExperimental.logUsingNewtonFixPrecision(x1, mc1),
				(x1, mc1) -> BigDecimalMathExperimental.logUsingRoot(x1, mc1,
						BigDecimalMathExperimental::logUsingNewtonFixPrecision),
				(x1, mc1) -> BigDecimalMathExperimental.logUsingExponent(x1, mc1,
						(x2, mc2) -> BigDecimalMathExperimental.logUsingPrimes(x2, mc2,
								BigDecimalMathExperimental::logUsingNewtonFixPrecision)),
				(x1, mc1) -> BigDecimalMathExperimental.logUsingPrimes(x1, mc1,
						(x2, mc2) -> BigDecimalMathExperimental.logUsingRoot(x2, mc2,
								BigDecimalMathExperimental::logUsingNewtonFixPrecision)));
	}

	private static class OperationInfo {
		BigDecimal b1;
		BigDecimal b2;
		MathContext mc;

		OperationInfo(BigDecimal b1, BigDecimal b2, MathContext mc) {
			this.b1 = b1;
			this.b2 = b2;
			this.mc = mc;
		}
	}

	private static void performanceReport_BasicOperations() {
		Random random = new Random();

		performanceReportOverLambda(
				"perf_basic_operations_fast_precisions_to_10000.csv",
				10000,
				100,
				FAST_REPEATS,
				(i) -> {
					return new OperationInfo(
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							new MathContext(Math.max(i, 1)));
				},
				Arrays.asList("add", "subtract"),
				(op) -> op.b1.add(op.b2, op.mc),
				(op) -> op.b1.subtract(op.b2, op.mc));

		performanceReportOverLambda(
				"perf_basic_operations_slow_precisions_to_10000.csv",
				10000,
				100,
				FAST_REPEATS,
				(i) -> {
					return new OperationInfo(
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							new MathContext(Math.max(i, 1)));
				},
				Arrays.asList("multiply", "divide"),
				(op) -> op.b1.multiply(op.b2, op.mc),
				(op) -> op.b1.divide(op.b2, op.mc));

		performanceReportOverLambda(
				"perf_basic_operations_slow2_precisions_to_10000.csv",
				10000,
				100,
				FAST_REPEATS,
				(i) -> {
					return new OperationInfo(
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							new MathContext(Math.max(i, 1)));
				},
				Arrays.asList("multiply 0.5", "divide 2"),
				(op) -> op.b1.multiply(ONE_HALF, op.mc),
				(op) -> op.b1.divide(TWO, op.mc));

		performanceReportOverLambda(
				"perf_basic_operations_fast_precisions_to_100000.csv",
				100000,
				1000,
				REPEATS,
				(i) -> {
					return new OperationInfo(
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							new MathContext(Math.max(i, 1)));
				},
				Arrays.asList("add", "subtract"),
				(op) -> op.b1.add(op.b2, op.mc),
				(op) -> op.b1.subtract(op.b2, op.mc));

		performanceReportOverLambda(
				"perf_basic_operations_slow_precisions_to_100000.csv",
				100000,
				1000,
				REPEATS,
				(i) -> {
					return new OperationInfo(
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							new MathContext(Math.max(i, 1)));
				},
				Arrays.asList("multiply", "divide"),
				(op) -> op.b1.multiply(op.b2, op.mc),
				(op) -> op.b1.divide(op.b2, op.mc));

		performanceReportOverLambda(
				"perf_basic_operations_slow2_precisions_to_100000.csv",
				100000,
				1000,
				REPEATS,
				(i) -> {
					return new OperationInfo(
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							BigDecimalMath.toBigDecimal(createStringNumber(i, random)),
							new MathContext(Math.max(i, 1)));
				},
				Arrays.asList("multiply 0.5", "divide 2"),
				(op) -> op.b1.multiply(ONE_HALF, op.mc),
				(op) -> op.b1.divide(TWO, op.mc));

	}

	private static void performanceReport_toBigDecimal_optimization() {
		performanceReportOverLambda(
				"perf_bigdec_string_impl_precisions_to_2000.csv",
				2000,
				1,
				REPEATS,
				(i) -> createStringNumber(i),
				Arrays.asList("toBigDecimal/3", "toBigDecimal/4", "toBigDecimal/5", "toBigDecimal/6", "toBigDecimal/8"),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 3),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 4),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 5),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 6),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 8));

		performanceReportOverLambda(
				"perf_bigdec_string_impl_precisions_to_10000.csv",
				20000,
				200,
				REPEATS,
				(i) -> createStringNumber(i),
				Arrays.asList("toBigDecimal/3", "toBigDecimal/4", "toBigDecimal/5", "toBigDecimal/6", "toBigDecimal/8"),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 3),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 4),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 5),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 6),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 8));

		performanceReportOverLambda(
				"perf_bigdec_string_impl_precisions_to_100000.csv",
				100000,
				10000,
				REPEATS,
				(i) -> createStringNumber(i),
				Arrays.asList("toBigDecimal/4", "toBigDecimal/8", "toBigDecimal/16"),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 4),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 8),
				(s) -> BigDecimalMathExperimental.toBigDecimalSplitCount(s, MathContext.UNLIMITED, 16));
	}

	private static void performanceReport_toBigDecimal() {
		performanceReportOverLambda(
				"perf_bigdec_string_precisions_to_2000.csv",
				2000,
				1,
				REPEATS,
				(i) -> createStringNumber(i),
				Arrays.asList("BigDecimal(String)", "toBigDecimal(String)"),
				(s) -> new BigDecimal(s),
				(s) -> BigDecimalMath.toBigDecimal(s));

		performanceReportOverLambda(
				"perf_bigdec_string_precisions_to_10000.csv",
				20000,
				200,
				REPEATS,
				(i) -> createStringNumber(i),
				Arrays.asList("BigDecimal(String)", "toBigDecimal(String)"),
				(s) -> new BigDecimal(s),
				(s) -> BigDecimalMath.toBigDecimal(s));

		performanceReportOverLambda(
				"perf_bigdec_string_precisions_to_100000.csv",
				100000,
				1000,
				REPEATS,
				(i) -> createStringNumber(i),
				Arrays.asList("BigDecimal(String)", "toBigDecimal(String)"),
				(s) -> new BigDecimal(s),
				(s) -> BigDecimalMath.toBigDecimal(s));

		performanceReportOverLambda(
				"perf_bigdec_string_precisions_to_1000000.csv",
				1000000,
				100000,
				3,
				(i) -> createStringNumber(i),
				Arrays.asList("BigDecimal(String)", "toBigDecimal(String)"),
				(s) -> new BigDecimal(s),
				(s) -> BigDecimalMath.toBigDecimal(s));
	}

	private static void performanceReport_factorial_optimization() {
		performanceReportOverLambda(
				"perf_factorial_impl_values_to_100.csv",
				100,
				1,
				FAST_REPEATS,
				(i) -> i,
				Arrays.asList("loop", "switchloop", "recursion"),
				(n) -> BigDecimalMathExperimental.factorialLoop(n),
				(n) -> BigDecimalMathExperimental.factorialSwitchLoop(n),
				(n) -> BigDecimalMathExperimental.factorialRecursion(n));

		performanceReportOverLambda(
				"perf_factorial_impl_values_to_200.csv",
				200,
				1,
				REPEATS,
				(i) -> i,
				Arrays.asList("loop", "switchloop", "recursion"),
				(n) -> BigDecimalMathExperimental.factorialLoop(n),
				(n) -> BigDecimalMathExperimental.factorialSwitchLoop(n),
				(n) -> BigDecimalMathExperimental.factorialRecursion(n));

		performanceReportOverLambda(
				"perf_factorial_impl_values_to_500.csv",
				500,
				5,
				FAST_REPEATS,
				(i) -> i,
				Arrays.asList("loop", "switchloop", "recursion"),
				(n) -> BigDecimalMathExperimental.factorialLoop(n),
				(n) -> BigDecimalMathExperimental.factorialSwitchLoop(n),
				(n) -> BigDecimalMathExperimental.factorialRecursion(n));

		performanceReportOverLambda(
				"perf_factorial_impl_values_to_1000.csv",
				1000,
				1,
				REPEATS,
				(i) -> i,
				Arrays.asList("loop", "switchloop", "recursion", "simple", "loopDivLimit"),
				(n) -> BigDecimalMathExperimental.factorialLoop(n),
				(n) -> BigDecimalMathExperimental.factorialSwitchLoop(n),
				(n) -> BigDecimalMathExperimental.factorialRecursion(n),
				(n) -> BigDecimalMathExperimental.factorialSimple(n),
				(n) -> BigDecimalMathExperimental.factorialLoopDivLimit(n));

		performanceReportOverLambda(
				"perf_factorial_impl_loops_values_to_1000.csv",
				1000,
				1,
				REPEATS,
				(i) -> i,
				Arrays.asList("loop", "loopDivLimit"),
				(n) -> BigDecimalMathExperimental.factorialLoop(n),
				(n) -> BigDecimalMathExperimental.factorialLoopDivLimit(n));

		performanceReportOverLambda(
				"perf_factorial_impl_recursion_values_to_100.csv",
				100,
				1,
				FAST_REPEATS,
				(i) -> i,
				Arrays.asList("recursion", "recursionAdaptive_65"),
				(n) -> BigDecimalMathExperimental.factorialRecursion(n),
				(n) -> BigDecimalMathExperimental.factorialRecursionAdaptive(n, 65));

		performanceReportOverLambda(
				"perf_factorial_impl_recursion_values_to_1000.csv",
				1000,
				1,
				FAST_REPEATS,
				(i) -> i,
				Arrays.asList("recursion", "recursionAdaptive_40", "recursionAdaptive_60", "recursionAdaptive_80", "recursionAdaptive_100"),
				(n) -> BigDecimalMathExperimental.factorialRecursion(n),
				(n) -> BigDecimalMathExperimental.factorialRecursionAdaptive(n, 40),
				(n) -> BigDecimalMathExperimental.factorialRecursionAdaptive(n, 60),
				(n) -> BigDecimalMathExperimental.factorialRecursionAdaptive(n, 80),
				(n) -> BigDecimalMathExperimental.factorialRecursionAdaptive(n, 100));
	}

	private static void performanceReport_Java9_sqrt() {
/*
		performanceReportOverValue(
				"perf_java9_sqrt_from_0_to_10.csv",
				REF_MATHCONTEXT,
				0,
				+10,
				+0.01,
				FAST_REPEATS,
				Arrays.asList(
						"sqrt",
						"java9_sqrt"),
				(x1, mc1) -> BigDecimalMath.sqrt(x1, mc1),
				(x1, mc1) -> x1.sqrt(mc1));

		performanceReportOverValue(
				"perf_java9_sqrt_from_0_to_100.csv",
				REF_MATHCONTEXT,
				0,
				+100,
				+0.1,
				FAST_REPEATS,
				Arrays.asList(
						"sqrt",
						"java9_sqrt"),
				(x1, mc1) -> BigDecimalMath.sqrt(x1, mc1),
				(x1, mc1) -> x1.sqrt(mc1));

		performanceReportOverValue(
				"perf_java9_sqrt_from_0_to_10000.csv",
				REF_MATHCONTEXT,
				0,
				+10000,
				+10,
				FAST_REPEATS,
				Arrays.asList(
						"sqrt",
						"java9_sqrt"),
				(x1, mc1) -> BigDecimalMath.sqrt(x1, mc1),
				(x1, mc1) -> x1.sqrt(mc1));

		performanceReportOverPrecision(
				"perf_java9_sqrt_precisions_to_200.csv",
				BigDecimal.valueOf(3.1),
				1,
				200,
				1,
				FAST_REPEATS,
				Arrays.asList("sqrt", "java9_sqrt"),
				(x, calculationMathContext) -> BigDecimalMath.sqrt(x, calculationMathContext),
				(x, calculationMathContext) -> x.sqrt(calculationMathContext));

		performanceReportOverPrecision(
				"perf_java9_sqrt_precisions_to_1000.csv",
				BigDecimal.valueOf(3.1),
				10,
				1000,
				2,
				FAST_REPEATS,
				Arrays.asList("sqrt", "java9_sqrt"),
				(x, calculationMathContext) -> BigDecimalMath.sqrt(x, calculationMathContext),
				(x, calculationMathContext) -> x.sqrt(calculationMathContext));

		performanceReportOverPrecision(
				"perf_java9_sqrt_precisions_to_10000.csv",
				BigDecimal.valueOf(3.1),
				10,
				10000,
				10,
				REPEATS,
				Arrays.asList("sqrt", "java9_sqrt"),
				(x, calculationMathContext) -> BigDecimalMath.sqrt(x, calculationMathContext),
				(x, calculationMathContext) -> x.sqrt(calculationMathContext));
*/
	}

	@SafeVarargs
	private static void performanceReportOverValue(String name, MathContext mathContext, double xStart, double xEnd, double xStep, int repeats, List<String> functionNames, BiFunction<BigDecimal, MathContext, BigDecimal>... functions) {
		performanceReportOverValue(name, mathContext, BigDecimal.valueOf(xStart), BigDecimal.valueOf(xEnd), BigDecimal.valueOf(xStep), repeats, functionNames, functions);
	}

	@SafeVarargs
	private static void performanceReportOverValue(String name, MathContext mathContext, BigDecimal xStart, BigDecimal xEnd, BigDecimal xStep, int repeats, List<String> functionNames, BiFunction<BigDecimal, MathContext, BigDecimal>... calculations) {
		StopWatch stopWatch = new StopWatch();
		System.out.println("Writing  " + name);

		try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_DIRECTORY + name))) {
			performanceReportOverValue(writer, mathContext, xStart, xEnd, xStep, repeats, functionNames, calculations);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.out.println("Finished in " + stopWatch);
	}

	@SafeVarargs
	private static void performanceReportOverValue(PrintWriter writer, MathContext mathContext, BigDecimal xStart, BigDecimal xEnd, BigDecimal xStep, int repeats, List<String> functionNames, BiFunction<BigDecimal, MathContext, BigDecimal>... functions) {
		if (functionNames.size() != functions.length) {
			throw new IllegalArgumentException("Must be same number of functionNames (" + functionNames.size() + ") and functions (" + functions.length + ")");
		}

		// warmup
		for (int i = 0; i < WARMUP_LOOP_REPEATS; i++) {
			for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
				for (BiFunction<BigDecimal, MathContext, BigDecimal> calculation : functions) {
					try {
						calculation.apply(x, WARMUP_MATHCONTEXT);
					} catch (ArithmeticException ex) {
						// ignore
					}
				}
			}
		}

		// print headers
		writer.printf("%8s", "x");
		for (int i = 0; i < functionNames.size(); i++) {
			writer.print(",");
			writer.printf("%8s", functionNames.get(i));
		}
		writer.println();

		// print types
		writer.printf("%8s", "number");
		for (int i = 0; i < functionNames.size(); i++) {
			writer.print(",");
			writer.printf("%8s", "number");
		}
		writer.println();

		// prepare data storage
		int xCount = 0;
		for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
			xCount++;
		}

		long[][][] nanosFunctionValueRepeat = new long[functions.length][][];
		for (int fIndex = 0; fIndex < functions.length; fIndex++) {
			nanosFunctionValueRepeat[fIndex] = new long[xCount][];
			for (int xIndex = 0; xIndex < xCount; xIndex++) {
				nanosFunctionValueRepeat[fIndex][xIndex] = new long[repeats];
			}
		}

		// real measurements
		for (int rIndex = 0; rIndex < repeats; rIndex++) {
			int xIndex = 0;
			for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
				for (int fIndex = 0; fIndex < functions.length; fIndex++) {
					BiFunction<BigDecimal, MathContext, BigDecimal> function = functions[fIndex];
					try {
						StopWatch stopWatch = new StopWatch();
						function.apply(x, mathContext);
						nanosFunctionValueRepeat[fIndex][xIndex][rIndex] = stopWatch.getElapsedNanos();
					} catch (ArithmeticException ex) {
						// ignore
					}
				}
				xIndex++;
			}
			System.out.print(".");
		}
		System.out.println();

		// write report
		{
			int xIndex = 0;
			for (BigDecimal x = xStart; x.compareTo(xEnd) <= 0; x = x.add(xStep)) {
				writer.printf("%8.3f", x);
				for (int fIndex = 0; fIndex < functions.length; fIndex++) {
					writer.print(",");
					double elapsedNanos = averagePercentile(AVERAGE_PERCENTILE, nanosFunctionValueRepeat[fIndex][xIndex]);
					writer.printf("%8.3f", elapsedNanos);
				}
				writer.println();

				xIndex++;
			}
		}
	}

	private static long median(long[] values) {
		Arrays.sort(values);
		int halfIndex = values.length / 2;
		if (values.length > 1 && values.length % 2 == 0) {
			return (values[halfIndex] + values[halfIndex + 1]) / 2;
		} else {
			return values[halfIndex];
		}
	}

	private static double averagePercentile(double percentile, long[] values) {
		Arrays.sort(values);

		int startIndex = (int) (values.length / 2 - values.length * percentile / 2);
		int endIndex = (int) (values.length / 2 + values.length * percentile / 2);

		double sum = 0;
		for (int i = startIndex; i < endIndex; i++) {
			sum += values[i];
		}

		return (sum / (endIndex - startIndex));
	}


	@SafeVarargs
	private static void performanceReportOverPrecision(String name, BigDecimal value, int precisionStart, int precisionEnd, int precisionStep, int repeats, List<String> functionNames, BiFunction<BigDecimal, MathContext, BigDecimal>... calculations) {
		StopWatch stopWatch = new StopWatch();
		System.out.println("Writing  " + name);

		try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_DIRECTORY + name))) {
			performanceReportOverPrecision(writer, value, precisionStart, precisionEnd, precisionStep, repeats, functionNames, calculations);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.out.println("Finished in " + stopWatch);
	}

	@SafeVarargs
	private static void performanceReportOverPrecision(PrintWriter writer, BigDecimal value, int precisionStart, int precisionEnd, int precisionStep, int repeats, List<String> functionNames, BiFunction<BigDecimal, MathContext, BigDecimal>... functions) {
		int innerRepeats = 10;

		// warmup
		for (int i = 0; i < WARMUP_REPEATS; i++) {
			for (BiFunction<BigDecimal, MathContext, BigDecimal> calculation : functions) {
				try {
					calculation.apply(value, WARMUP_MATHCONTEXT);
				} catch (ArithmeticException ex) {
					// ignore
				}
			}
		}

		// print headers
		writer.printf("%8s", "precision");
		for (int fIndex = 0; fIndex < functionNames.size(); fIndex++) {
			writer.print(",");
			writer.printf("%8s", functionNames.get(fIndex));
		}
		writer.println();

		// print types
		writer.printf("%8s", "number");
		for (int fIndex = 0; fIndex < functionNames.size(); fIndex++) {
			writer.print(",");
			writer.printf("%8s", "number");
		}
		writer.println();

		// prepare data storage
		int pCount = 0;
		for (int precision = precisionStart; precision <= precisionEnd; precision += precisionStep) {
			pCount++;
		}

		long[][][] nanosFunctionPrecisionRepeat = new long[functions.length][][];
		for (int fIndex = 0; fIndex < functions.length; fIndex++) {
			nanosFunctionPrecisionRepeat[fIndex] = new long[pCount][];

			for (int pIndex = 0; pIndex < pCount; pIndex++) {
				nanosFunctionPrecisionRepeat[fIndex][pIndex] = new long[repeats];
			}
		}

		// real measurement
		for (int rIndex = 0; rIndex < repeats; rIndex++) {
			for (int fIndex = 0; fIndex < functions.length; fIndex++) {
				int pIndex = 0;
				for (int precision = precisionStart; precision <= precisionEnd; precision += precisionStep) {
					MathContext mathContext = new MathContext(precision);

					BiFunction<BigDecimal, MathContext, BigDecimal> calculation = functions[fIndex];

					try {
						StopWatch stopWatch = new StopWatch();
						for (int innerIndex = 0; innerIndex < innerRepeats; innerIndex++) {
							calculation.apply(value, mathContext);
						}
						nanosFunctionPrecisionRepeat[fIndex][pIndex][rIndex] = stopWatch.getElapsedNanos();
					} catch (ArithmeticException ex) {
						// ignore
					}
					pIndex++;
				}

			}
			System.out.print(".");
		}
		System.out.println();

		// write report
		{
			int p = 0;
			for (int precision = precisionStart; precision <= precisionEnd; precision += precisionStep) {
				writer.printf("%8d", precision);
				for (int fIndex = 0; fIndex < functions.length; fIndex++) {
					writer.print(",");
					double elapsedNanos = averagePercentile(AVERAGE_PERCENTILE, nanosFunctionPrecisionRepeat[fIndex][p]) / innerRepeats;
					writer.printf("%8.3f", elapsedNanos);
				}
				p++;
				writer.println();
			}
		}
	}

	public static <T, U> void performanceReportOverLambda(String name, int count, int repeats, Function<Integer, T> convert, List<String> functionNames, Function<T, U>... functions) {
		performanceReportOverLambda(name, count, 1, repeats, convert, functionNames, functions);
	}

	public static <T, U> void performanceReportOverLambda(String name, int count, int countStep, int repeats, Function<Integer, T> convert, List<String> functionNames, Function<T, U>... functions) {
		StopWatch stopWatch = new StopWatch();
		System.out.println("Writing  " + name);

		try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_DIRECTORY + name))) {
			performanceReportOverLambda(writer, count, countStep, repeats, convert, functionNames, functions);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.out.println("Finished in " + stopWatch);
	}

	public static <T, U> void performanceReportOverLambda(PrintWriter writer, int count, int countStep, int repeats, Function<Integer, T> convert, List<String> functionNames, Function<T, U>... functions) {
		int innerRepeats = 10;

		int warmupValueCount = 10;

		// warmup
		for (int warmupCount = 0; warmupCount < WARMUP_REPEATS; warmupCount++) {
			for (int valueIndex = 0; valueIndex < warmupValueCount; valueIndex++) {
				for (Function<T, U> function : functions) {
					T value = convert.apply(valueIndex);
					function.apply(value);
				}
			}
		}

		// print headers
		writer.printf("%8s", "value");
		for (int fIndex = 0; fIndex < functionNames.size(); fIndex++) {
			writer.print(",");
			writer.printf("%8s", functionNames.get(fIndex));
		}
		writer.println();

		// print types
		writer.printf("%8s", "number");
		for (int fIndex = 0; fIndex < functionNames.size(); fIndex++) {
			writer.print(",");
			writer.printf("%8s", "number");
		}
		writer.println();

		// prepare data storage
		int pCount = 0;
		for (int valueIndex = 0; valueIndex <= count; valueIndex += countStep) {
			pCount++;
		}

		long[][][] nanosFunctionValueRepeat = new long[functions.length][][];
		for (int fIndex = 0; fIndex < functions.length; fIndex++) {
			nanosFunctionValueRepeat[fIndex] = new long[pCount][];

			for (int pIndex = 0; pIndex < pCount; pIndex++) {
				nanosFunctionValueRepeat[fIndex][pIndex] = new long[repeats];
			}
		}

		// real measurement
		for (int rIndex = 0; rIndex < repeats; rIndex++) {
			for (int fIndex = 0; fIndex < functions.length; fIndex++) {
				int pIndex = 0;
				for (int valueIndex = 0; valueIndex < count; valueIndex += countStep) {
					Function<T, U> calculation = functions[fIndex];
					T value = convert.apply(valueIndex);

					try {
						StopWatch stopWatch = new StopWatch();
						for (int innerIndex = 0; innerIndex < innerRepeats; innerIndex++) {
							calculation.apply(value);
						}
						nanosFunctionValueRepeat[fIndex][pIndex][rIndex] = stopWatch.getElapsedNanos();
					} catch (Exception ex) {
						// ignore
					}
					pIndex++;
				}

			}
			System.out.print(".");
		}
		System.out.println();

		// write report
		{
			int pIndex = 0;
			for (int valueIndex = 0; valueIndex < count; valueIndex += countStep) {
				writer.printf("%8d", valueIndex);
				for (int fIndex = 0; fIndex < functions.length; fIndex++) {
					writer.print(",");
					double elapsedNanos = averagePercentile(AVERAGE_PERCENTILE, nanosFunctionValueRepeat[fIndex][pIndex]) / innerRepeats;
					writer.printf("%8.3f", elapsedNanos);
				}
				pIndex++;
				writer.println();
			}
		}
	}
}