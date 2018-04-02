package ch.obermuhlner.math.big.example.internal;

import ch.obermuhlner.math.big.example.StopWatch;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static ch.obermuhlner.math.big.BigDecimalMath.*;

public class GammaExperiments {

	private static final java.math.BigDecimal TWO = BigDecimal.valueOf(2);

	private static final String OUTPUT_DIRECTORY = "docs/markdown/gamma/";

	public static BigDecimal factorialUsingEuler(BigDecimal x, int steps, MathContext mathContext) {
		MathContext mc = new MathContext(mathContext.getPrecision() * 2, mathContext.getRoundingMode());

		BigDecimal product = BigDecimal.ONE;
		for (int n = 1; n < steps; n++) {
			BigDecimal factor = BigDecimal.ONE.divide(BigDecimal.ONE.add(x.divide(BigDecimal.valueOf(n), mc), mc), mc).multiply(pow(BigDecimal.ONE.add(BigDecimal.ONE.divide(BigDecimal.valueOf(n), mc), mc), x, mc), mc);
			product = product.multiply(factor, mc);
		}

		return product.round(mathContext);
	}

	public static BigDecimal factorialUsingSpouge(BigDecimal x, MathContext mathContext) {
		// https://en.wikipedia.org/wiki/Spouge%27s_approximation
		MathContext mc = new MathContext(mathContext.getPrecision() * 2, mathContext.getRoundingMode());

		int a = mathContext.getPrecision() * 13 / 10;
		BigDecimal bigA = BigDecimal.valueOf(a);
		BigDecimal c0 = sqrt(pi(mc).multiply(TWO, mc), mc);

		boolean negative = false;
		BigDecimal factor = c0;
		for (int k = 1; k < a; k++) {
			BigDecimal bigK = BigDecimal.valueOf(k);
			BigDecimal ck = pow(BigDecimal.valueOf(a-k), bigK.subtract(BigDecimal.valueOf(0.5), mc), mc);
			ck = ck.multiply(exp(BigDecimal.valueOf(a-k), mc), mc);
			ck = ck.divide(factorial(k - 1), mc);
			if (negative) {
				ck = ck.negate();
			}
			factor = factor.add(ck.divide(x.add(bigK), mc), mc);
			negative = !negative;
		}

		BigDecimal result = pow(x.add(bigA, mc), x.add(BigDecimal.valueOf(0.5), mc), mc);
		result = result.multiply(exp(x.negate().subtract(bigA, mc), mc), mc);
		result = result.multiply(factor, mc);

		return result.round(mathContext);
	}

	public static BigDecimal errorOfFactorialUsingSpouge(int a, MathContext mc) {
		return pow(BigDecimal.valueOf(a), BigDecimal.valueOf(-0.5), mc).multiply(pow(TWO.multiply(pi(mc), mc), BigDecimal.valueOf(-a-0.5), mc), mc);
	}

	public static BigDecimal factorialUsingSpougeCached(BigDecimal x, MathContext mathContext) {
		// https://en.wikipedia.org/wiki/Spouge%27s_approximation
		MathContext mc = new MathContext(mathContext.getPrecision() * 2, mathContext.getRoundingMode());

		int a = mathContext.getPrecision() * 13 / 10;
		List<BigDecimal> constants = getSpougeFactorialConstants(a);

		BigDecimal bigA = BigDecimal.valueOf(a);

		boolean negative = false;
		BigDecimal factor = constants.get(0);
		for (int k = 1; k < a; k++) {
			BigDecimal bigK = BigDecimal.valueOf(k);
			factor = factor.add(constants.get(k).divide(x.add(bigK), mc), mc);
			negative = !negative;
		}

		BigDecimal result = pow(x.add(bigA, mc), x.add(BigDecimal.valueOf(0.5), mc), mc);
		result = result.multiply(exp(x.negate().subtract(bigA, mc), mc), mc);
		result = result.multiply(factor, mc);

		return result.round(mathContext);
	}

	private static Map<Integer, List<BigDecimal>> spougeFactorialConstantsCache = new ConcurrentHashMap<>();

	private static List<BigDecimal> getSpougeFactorialConstants(int a) {
		return spougeFactorialConstantsCache.computeIfAbsent(a, key -> {
			List<BigDecimal> constants = new ArrayList<>(a);
			MathContext mc = new MathContext(a * 15/10);

			BigDecimal c0 = sqrt(pi(mc).multiply(TWO, mc), mc);
			constants.add(c0);

			boolean negative = false;
			BigDecimal factor = c0;
			for (int k = 1; k < a; k++) {
				BigDecimal bigK = BigDecimal.valueOf(k);
				BigDecimal ck = pow(BigDecimal.valueOf(a-k), bigK.subtract(BigDecimal.valueOf(0.5), mc), mc);
				ck = ck.multiply(exp(BigDecimal.valueOf(a-k), mc), mc);
				ck = ck.divide(factorial(k - 1), mc);
				if (negative) {
					ck = ck.negate();
				}
				constants.add(ck);

				negative = !negative;
			}

			return constants;
		});
	}

	public static void main(String[] args) {
		runFactorialUsingEuler();
		//runErrorOfFactorialUsingSpouge();
		//runFactorialUsingSpouge();
		//runFactorialCalculatingSpougeConstants();
		//runFactorialUsingSpougeCached();
		//runFactorialUsingSpougeCachedOverPrecision();
	}

	private static void runFactorialUsingEuler() {
		MathContext mc = new MathContext(20);

		System.out.println("5! = " + factorialUsingEuler(BigDecimal.valueOf(5), 10000, mc));
	}

	private static void runFactorialUsingSpouge() {
		withPrintWriter("factorial_spouge_prec200.csv", writer -> {
			MathContext mc = new MathContext(200);

			writer.printf("%5s, %15s, %10s\n", "x", "significant", "ms");

			for (int i = 0; i < 400; i++) {
				BigDecimal f1 = factorial(i);

				StopWatch stopWatch = new StopWatch();
				BigDecimal f2 = factorialUsingSpouge(BigDecimal.valueOf(i), mc);
				long milliseconds = stopWatch.getElapsedMillis();

				boolean same = f1.compareTo(f2) == 0;
				int significantDigits = f1.precision();
				System.out.println(i + "! = " + same + " : " + significantDigits + " in " + milliseconds + " ms");
				writer.printf("%5d, %15d, %10d\n", i, significantDigits, milliseconds);
			}
		});
	}

	private static void runFactorialCalculatingSpougeConstants() {
		withPrintWriter("factorial_calculating_spouge_constants.csv", writer -> {
			writer.printf("%15s, %10s\n", "precision", "ms");

			for (int precision = 10; precision <= 400; precision+=10) {
				MathContext mc = new MathContext(precision);

				StopWatch stopWatch = new StopWatch();
				factorialUsingSpougeCached(BigDecimal.valueOf(0), mc);
				long milliseconds = stopWatch.getElapsedMillis();

				System.out.println(precision + " in " + milliseconds + " ms");
				writer.printf("%15d, %10d\n", precision, milliseconds);
			}
		});
	}

	private static void runFactorialUsingSpougeCached() {
		withPrintWriter("factorial_spouge_cached_prec200.csv", writer -> {
			MathContext mc = new MathContext(200);

			factorialUsingSpougeCached(BigDecimal.valueOf(1), mc);

			writer.printf("%5s, %15s, %10s\n", "x", "significant", "ms");

			for (int i = 0; i < 100; i++) {
				BigDecimal f1 = factorial(i);

				StopWatch stopWatch = new StopWatch();
				BigDecimal f2 = factorialUsingSpougeCached(BigDecimal.valueOf(i), mc);
				long milliseconds = stopWatch.getElapsedMillis();

				boolean same = f1.compareTo(f2) == 0;
				int significantDigits = f1.precision();
				System.out.println(i + "! = " + same + " : " + significantDigits + " in " + milliseconds + " ms");
				writer.printf("%5d, %15d, %10d\n", i, significantDigits, milliseconds);
			}
		});
	}

	private static void runFactorialUsingSpougeCachedOverPrecision() {
		withPrintWriter("factorial_spouge_cached_precisions.csv", writer -> {
			writer.printf("%15s, %10s\n", "precision", "ms");

			for (int precision = 0; precision < 400; precision+=10) {
				MathContext mc = new MathContext(precision);

				factorialUsingSpougeCached(BigDecimal.valueOf(1), mc);

				StopWatch stopWatch = new StopWatch();
				factorialUsingSpougeCached(BigDecimal.valueOf(precision), mc);
				long milliseconds = stopWatch.getElapsedMillis();

				System.out.println(precision + " in " + milliseconds + " ms");
				writer.printf("%15d, %10d\n", precision, milliseconds);
			}
		});
	}

	private static void runErrorOfFactorialUsingSpouge() {
		withPrintWriter("factorial_spouge_precision.csv", writer -> {
			MathContext mc = new MathContext(20);
			writer.printf("%5s, %10s\n", "a", "precision");

			for (int a = 1; a < 1000; a++) {
				BigDecimal error = errorOfFactorialUsingSpouge(a, mc);
				BigDecimal precision = integralPart(log10(error, mc).negate());

				System.out.println("a=" + a + " precision=" + precision);
				writer.printf("%5d, %10s\n", a, precision);
			}
		});
	}

	private static void withPrintWriter(String filename, Consumer<PrintWriter> block) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_DIRECTORY + filename))) {
			block.accept(writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static long inMilliseconds(Runnable block) {
		long startMillis = System.currentTimeMillis();
		block.run();
		long endMillis = System.currentTimeMillis();
		return endMillis - startMillis;
	}
}
