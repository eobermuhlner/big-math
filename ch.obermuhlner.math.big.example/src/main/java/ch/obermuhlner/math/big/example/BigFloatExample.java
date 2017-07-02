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
		BigFloat dividendTerm1 = context.valueOf(5); // -(6*k - 5)
		BigFloat dividendTerm2 = context.valueOf(-1); // 2*k - 1
		BigFloat dividendTerm3 = context.valueOf(-1); // 6*k - 1
		BigFloat kPower3 = context.valueOf(0);
		
		long iterationCount = (context.getPrecision()+13) / 14;
		for (long k = 1; k <= iterationCount; k++) {
			BigFloat valueK = context.valueOf(k);
			dividendTerm1 = dividendTerm1.add(-6);
			dividendTerm2 = dividendTerm2.add(2);
			dividendTerm3 = dividendTerm3.add(6);
			BigFloat dividend = context.valueOf(dividendTerm1).multiply(dividendTerm2).multiply(dividendTerm3);
			kPower3 = valueK.pow(3);
			BigFloat divisor = kPower3.multiply(valueDivisor);
			a = a.multiply(dividend).divide(divisor);
			BigFloat b = valueK.multiply(a);
			
			sumA = sumA.add(a);
			sumB = sumB.add(b);
		}
		
		final BigFloat factor = context.valueOf(426880).multiply(sqrt(context.valueOf(10005)));
		BigFloat pi = factor.divide(sumA.multiply(13591409).add(sumB.multiply(545140134)));
		
		return pi.withContext(context(precision));
	}
}
