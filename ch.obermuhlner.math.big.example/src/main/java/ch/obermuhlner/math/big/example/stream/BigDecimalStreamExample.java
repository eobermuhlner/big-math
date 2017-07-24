package ch.obermuhlner.math.big.example.stream;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.stream.BigDecimalStream;

public class BigDecimalStreamExample {

	public static void main(String[] args) {
		MathContext mathContext = MathContext.DECIMAL128;

		System.out.println("Range [0, 10) step 1");
		BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.ONE, mathContext)
			.forEach(System.out::println);
		System.out.println();
		
		System.out.println("Range [0, 10) step 3 (using long as input parameters)");
		BigDecimalStream.range(0, 10, 3, mathContext)
			.forEach(System.out::println);
		System.out.println();

		System.out.println("Range [0, 10) step 3 (using double as input parameters)");
		BigDecimalStream.range(0.0, 10.0, 3.0, mathContext)
			.forEach(System.out::println);
		System.out.println();

		System.out.println("Range [10, 0) step -1");
		BigDecimalStream.range(10, 0, -1, mathContext)
			.forEach(System.out::println);
		System.out.println();

		System.out.println("Range [10, 0] step -1");
		BigDecimalStream.rangeClosed(10, 0, -1, mathContext)
			.forEach(System.out::println);
		System.out.println();
		
		System.out.println("Range [0, 10] step 1 parallel");
		BigDecimalStream.rangeClosed(0, 10, 1, mathContext)
			.parallel()
			.forEach(System.out::println);
		System.out.println();

	}
}
