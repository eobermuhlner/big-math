package ch.obermuhlner.math.big.example;

import static ch.obermuhlner.math.big.BigFloat.*;

import ch.obermuhlner.math.big.BigFloat;
import ch.obermuhlner.math.big.BigFloat.Context;

public class BigFloatExample {

	public static void main(String[] args) {
		examplePi();
	}

	private static void examplePi() {
		System.out.println(piChudnovski(10));
	}

	private static BigFloat piChudnovski(int precision) {
		Context context = BigFloat.context(precision + 10);

		final BigFloat valueDivisor = context.valueOf(640320).pow(3).divide(24);

		BigFloat sumA = context.valueOf(1);
		BigFloat sumB = context.valueOf(0);

		BigFloat a = context.valueOf(1);
		long dividendTerm1 = 5; // -(6*k - 5)
		long dividendTerm2 = -1; // 2*k - 1
		long dividendTerm3 = -1; // 6*k - 1
		BigFloat kPower3 = context.valueOf(0);
		
		long iterationCount = (context.getPrecision()+13) / 14;
		for (long k = 1; k <= iterationCount; k++) {
			BigFloat valueK = context.valueOf(k);
			dividendTerm1 += -6;
			dividendTerm2 += 2;
			dividendTerm3 += 6;
			BigFloat dividend = context.valueOf(dividendTerm1).multiply(dividendTerm2).multiply(dividendTerm3);
			kPower3 = valueK.pow(3);
			BigFloat divisor = kPower3.multiply(valueDivisor);
			a = a.multiply(dividend).divide(divisor);
			BigFloat b = valueK.multiply(a);
			
			sumA = sumA.add(a);
			sumB = sumB.add(b);
		}
		
		final BigFloat value426880 = context.valueOf(426880);
		final BigFloat value10005 = context.valueOf(10005);
		final BigFloat factor = value426880.multiply(sqrt(value10005));
		BigFloat pi = factor.divide(sumA.multiply(13591409).add(sumB.multiply(545140134)));
		
		return pi.withContext(context(precision));
	}
}
