package ch.obermuhlner.math.big.example;

import ch.obermuhlner.math.big.BigRational;

public class BernoulliTable {

	public static void main(String[] args) {
		printBernoulliTable(100);
	}

	private static void printBernoulliTable(int n) {
		for (int i = 0; i < n; i++) {
			BigRational b = BigRational.bernoulli(i).reduce();
			System.out.printf("%3s : %s\n", i, b.toRationalString());
		}
	}
}
