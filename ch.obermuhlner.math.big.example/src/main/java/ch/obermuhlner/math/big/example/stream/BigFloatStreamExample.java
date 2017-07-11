package ch.obermuhlner.math.big.example.stream;

import ch.obermuhlner.math.big.BigFloat;
import ch.obermuhlner.math.big.BigFloat.Context;
import ch.obermuhlner.math.big.stream.BigFloatStream;

public class BigFloatStreamExample {

	public static void main(String[] args) {
		Context context = BigFloat.context(20);
		BigFloatStream.range(context.valueOf(0), context.valueOf(10))
			.parallel()
			.forEach(System.out::println);
	}
}
