package ch.obermuhlner.math.big.example.internal;

import ch.obermuhlner.math.big.example.StopWatch;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	// https://en.wikipedia.org/wiki/Spouge%27s_approximation
	public static BigDecimal factorialUsingSpougeCached(BigDecimal x, MathContext mathContext) {
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

	private static Map<Integer, List<BigDecimal>> spougeFactorialConstantsCache = new HashMap<>();

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
		//runFactorialUsingEuler();
		//runErrorOfFactorialUsingSpouge();
		runFactorialUsingSpouge();
		//runFactorialCalculatingSpougeConstants();
		runFactorialUsingSpougeCached();
		runFactorialUsingSpougeCachedOverPrecision();
	}

	private static void runFactorialUsingEuler() {
		MathContext mc = new MathContext(20);

		System.out.println("5! in       1 steps = " + factorialUsingEuler(BigDecimal.valueOf(5), 1, mc));
		System.out.println("5! in      10 steps = " + factorialUsingEuler(BigDecimal.valueOf(5), 10, mc));
		System.out.println("5! in     100 steps = " + factorialUsingEuler(BigDecimal.valueOf(5), 100, mc));
		System.out.println("5! in    1000 steps = " + factorialUsingEuler(BigDecimal.valueOf(5), 1000, mc));
		System.out.println("5! in   10000 steps = " + factorialUsingEuler(BigDecimal.valueOf(5), 10000, mc));
		System.out.println("5! in  100000 steps = " + factorialUsingEuler(BigDecimal.valueOf(5), 100000, mc));
		System.out.println("5! in 1000000 steps = " + factorialUsingEuler(BigDecimal.valueOf(5), 1000000, mc));
	}

	private static void runFactorialUsingSpouge() {
		withPrintWriter("factorial_spouge_prec200.csv", writer -> {
			MathContext mc = new MathContext(200);

			writer.printf("%5s, %10s\n", "x", "ms");

			for (int x = 0; x < 100; x++) {
				BigDecimal f1 = factorial(x);

				StopWatch stopWatch = new StopWatch();
				BigDecimal f2 = factorialUsingSpouge(BigDecimal.valueOf(x), mc);
				long milliseconds = stopWatch.getElapsedMillis();

				boolean same = f1.compareTo(f2) == 0;
				int significantDigits = f1.precision();
				System.out.println(x + "! = " + same + " : " + significantDigits + " in " + milliseconds + " ms");
				writer.printf("%5d, %10d\n", x, milliseconds);
			}
		});
	}

	private static void runFactorialCalculatingSpougeConstants() {
		spougeFactorialConstantsCache.clear();

		withPrintWriter("factorial_calculating_spouge_constants.csv", writer -> {
			writer.printf("%15s, %10s\n", "precision", "ms");

			for (int a = 10; a <= 400; a+=10) {
				StopWatch stopWatch = new StopWatch();
				getSpougeFactorialConstants(a);
				long milliseconds = stopWatch.getElapsedMillis();

				System.out.println("coefficients of " + a + " in " + milliseconds + " ms");
				writer.printf("%15d, %10d\n", a, milliseconds);
			}
		});
	}

	private static void runFactorialUsingSpougeCached() {
		withPrintWriter("factorial_spouge_cached_prec200.csv", writer -> {
			MathContext mc = new MathContext(200);

			factorialUsingSpougeCached(BigDecimal.valueOf(1), mc); // make sure coefficients are calculated

			writer.printf("%5s, %10s\n", "x", "ms");

			for (int i = 0; i < 100; i++) {
				BigDecimal f1 = factorial(i);

				StopWatch stopWatch = new StopWatch();
				BigDecimal f2 = factorialUsingSpougeCached(BigDecimal.valueOf(i), mc);
				long milliseconds = stopWatch.getElapsedMillis();

				boolean same = f1.compareTo(f2) == 0;
				int significantDigits = f1.precision();
				System.out.println(i + "! = " + same + " : " + significantDigits + " in " + milliseconds + " ms");
				writer.printf("%5d, %10d\n", i, milliseconds);
			}
		});
	}

	private static void runFactorialUsingSpougeCachedOverPrecision() {
		withPrintWriter("factorial_spouge_cached_precisions.csv", writer -> {
			writer.printf("%15s, %10s\n", "precision", "ms");

			for (int precision = 0; precision < 400; precision+=10) {
				MathContext mc = new MathContext(precision);

				factorialUsingSpougeCached(BigDecimal.valueOf(1), mc); // make sure coefficients are calculated

				StopWatch stopWatch = new StopWatch();
				factorialUsingSpougeCached(BigDecimal.valueOf(5), mc);
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
