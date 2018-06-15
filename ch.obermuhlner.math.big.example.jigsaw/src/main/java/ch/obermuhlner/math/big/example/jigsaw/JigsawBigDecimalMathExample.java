package ch.obermuhlner.math.big.example.jigsaw;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.MathContext;

public class JigsawBigDecimalMathExample {
	public static void main (String[] args) {
		MathContext mc = new MathContext(100);

		System.out.println(BigDecimalMath.pi(mc));
	}
}
