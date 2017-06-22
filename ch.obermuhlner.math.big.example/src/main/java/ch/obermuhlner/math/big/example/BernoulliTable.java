package ch.obermuhlner.math.big.example;

import ch.obermuhlner.math.big.BigRational;

public class BernoulliTable {

	public static void main(String[] args) {
		printBernoulliTableSimple(500);
	}

	private static void printBernoulliTableSimple(int n) {
		System.out.printf("%3s,%s\n", "N", "Bernoulli");
		for (int i = 0; i < n; i++) {
			BigRational b = BigRational.bernoulli(i).reduce();
			System.out.printf("%3d,%s\n", i, b.toRationalString());
		}
	}
	
	private static void printBernoulliTableHtml(int n) {
		System.out.printf("<table>\n");
		
		System.out.printf("<thead>\n");
		System.out.printf("<th>N</th>");
		System.out.printf("<th>Bernoulli Number</th>\n");
		System.out.printf("</thead>\n");
		for (int i = 0; i < n; i++) {
			BigRational b = BigRational.bernoulli(i).reduce();
			System.out.printf("<tr>");
			System.out.printf("<td>%3d</td>", i);
			System.out.printf("<td>%50s</td>", b.toRationalString());
			System.out.printf("</tr>\n");
		}
		System.out.printf("</table>\n");
	}

	private static void printBernoulliTableHtml2(int n) {
		System.out.printf("<table>\n");
		
		System.out.printf("</thead>\n");
		for (int i = 0; i < n; i++) {
			BigRational b = BigRational.bernoulli(i).reduce();
			System.out.printf("<tr>");
			System.out.printf("<td style=\"vertical-align: middle\">B<sub>%d</sub>&nbsp;=</td>", i);
			System.out.printf("<td>\n");

			System.out.printf("<span style=\"display: block; text-align: center; vertical-align: middle\">\n");
			if (b.isInteger()) {
				System.out.printf("%s", b);
			} else {
				System.out.printf("<span style=\"border-bottom:solid 1px black;\">&nbsp;%s&nbsp;", b.getNumeratorBigInteger());
				System.out.printf("</span><br>\n");
				System.out.printf("<span>&nbsp;%s&nbsp;", b.getDenominatorBigInteger());
				System.out.printf("</span>\n");
			}
			System.out.printf("</span>\n");
			
			System.out.printf("<td/>\n");
			System.out.printf("</tr>\n");
		}
		System.out.printf("</table>\n");
	}
}
