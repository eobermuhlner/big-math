package ch.obermuhlner.math.big.example;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.internal.SinCalculator;
import ch.obermuhlner.math.big.internal.ExpCalculator;

public class FunctionTable {

	public static void main(String[] args) {
		printTableSin();
		//printTableExp();
	}

	public static void printTableSin() {
		MathContext mathContext = new MathContext(20);
		printTable(
				0,
				10,
				0.1,
				Arrays.asList(
						"SinCalculator",
						"BigDecimalMath.sin",
						"Math.sin"),
				Arrays.asList(
						x -> SinCalculator.INSTANCE.calculate(x, mathContext),
						x -> BigDecimalMath.sin(x, mathContext),
						x -> BigDecimal.valueOf(Math.sin(x.doubleValue()))
						));
	}

	public static void printTableExp() {
		MathContext mathContext = new MathContext(20);
		printTable(
				0,
				10,
				0.1,
				Arrays.asList(
						"ExpCalculator",
						"BigDecimalMath.exp",
						"Math.exp"),
				Arrays.asList(
						x -> ExpCalculator.INSTANCE.calculate(x, mathContext),
						x -> BigDecimalMath.exp(x, mathContext),
						x -> BigDecimal.valueOf(Math.exp(x.doubleValue()))
						));
	}

	private static void printTable(
			double startX,
			double endX,
			double deltaX,
			List<String> names,
			List<Function<BigDecimal, BigDecimal>> functions) {
		printTable(BigDecimal.valueOf(startX), BigDecimal.valueOf(endX), BigDecimal.valueOf(deltaX), names, functions);
	}

	private static void printTable(
			BigDecimal startX,
			BigDecimal endX,
			BigDecimal deltaX,
			List<String> names,
			List<Function<BigDecimal, BigDecimal>> functions) {

		// print names
		System.out.printf("%10s", "x");
		for (String name : names) {
			System.out.printf(",%30s", name);
		}
		System.out.println();
		
		// print types
		System.out.printf("%10s", "number");
		for (String name : names) {
			System.out.printf(",%30s", "number");
		}
		System.out.println();
		
		// print values
		for (BigDecimal x = startX; x.compareTo(endX) < 0; x = x.add(deltaX)) {
			System.out.printf("%10s", x);
			for (Function<BigDecimal, BigDecimal> function : functions) {
				try {
					BigDecimal y = function.apply(x);
					System.out.printf(",%30s", y);
				} catch(ArithmeticException ex) {
					System.out.printf(",%30s", "NaN");
					// ignore
				}
			}
			System.out.println();
		}
	}
}
