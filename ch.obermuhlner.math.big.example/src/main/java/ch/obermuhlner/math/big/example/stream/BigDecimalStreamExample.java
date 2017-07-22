package ch.obermuhlner.math.big.example.stream;

import java.math.BigDecimal;
import java.math.MathContext;

import ch.obermuhlner.math.big.stream.BigDecimalStream;

public class BigDecimalStreamExample {

	public static void main(String[] args) {
		BigDecimalStream.range(BigDecimal.valueOf(0), BigDecimal.valueOf(10), BigDecimal.valueOf(3), MathContext.DECIMAL128)
			.forEach(System.out::println);

		BigDecimalStream.range(0, 10, 3, MathContext.DECIMAL128)
			.forEach(System.out::println);
	}
}
