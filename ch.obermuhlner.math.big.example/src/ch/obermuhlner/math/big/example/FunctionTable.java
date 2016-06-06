package ch.obermuhlner.math.big.example;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import ch.obermuhlner.math.big.BigDecimalMath;

public class FunctionTable {

	public static void main(String[] args) {
//		printTableSin();
		printTableAsin();
//		printTableAcos();
//		printTableAtan();
//		printTableExp();
	}

	public static void printTableSin() {
		MathContext mathContext = new MathContext(20);
		printTable(
				0,
				10,
				0.1,
				Arrays.asList(
						"BigDecimalMath.sin",
						"Math.sin"),
				Arrays.asList(
						x -> BigDecimalMath.sin(x, mathContext),
						x -> BigDecimal.valueOf(Math.sin(x.doubleValue()))
						));
	}

	public static void printTableAsin() {
		MathContext mathContext = new MathContext(20);
		printTable(
				-1,
				1,
				0.01,
				Arrays.asList(
						"BigDecimalMath.asin",
						"Math.asin"),
				Arrays.asList(
						x -> BigDecimalMath.asin(x, mathContext),
						x -> BigDecimal.valueOf(Math.asin(x.doubleValue()))
						));
	}

	public static void printTableAcos() {
		MathContext mathContext = new MathContext(20);
		printTable(
				-1,
				1,
				0.01,
				Arrays.asList(
						"BigDecimalMath.acos",
						"Math.acos"),
				Arrays.asList(
						x -> BigDecimalMath.acos(x, mathContext),
						x -> BigDecimal.valueOf(Math.acos(x.doubleValue()))
						));
	}

	public static void printTableAtan() {
		MathContext mathContext = new MathContext(20);
		printTable(
				-1,
				1,
				0.01,
				Arrays.asList(
						"BigDecimalMath.atan",
						"Math.atan"),
				Arrays.asList(
						x -> BigDecimalMath.atan(x, mathContext),
						x -> BigDecimal.valueOf(Math.atan(x.doubleValue()))
						));
	}

	public static void printTableExp() {
		MathContext mathContext = new MathContext(20);
		printTable(
				0,
				10,
				0.1,
				Arrays.asList(
						"BigDecimalMath.exp",
						"Math.exp"),
				Arrays.asList(
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
		for (BigDecimal x = startX; x.compareTo(endX) <= 0; x = x.add(deltaX)) {
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
