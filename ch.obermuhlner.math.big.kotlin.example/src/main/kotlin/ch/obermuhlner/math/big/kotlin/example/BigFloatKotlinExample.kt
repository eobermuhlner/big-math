package ch.obermuhlner.math.big.kotlin.example

import ch.obermuhlner.math.big.BigFloat
import ch.obermuhlner.math.big.BigFloat.*
import ch.obermuhlner.math.big.kotlin.*

fun main(args: Array<String>) {
	simpleExample()
	piExample()
}

fun simpleExample() {
	val context = context(100)
	val v1 = context.valueOf(2) / 3

	println(v1)
}

fun piExample() {
	println(piChudnovsky(100))
}

fun piChudnovsky(precision: Int) : BigFloat {
	val context = context(precision + 10)
	
	val valueDivisor = (context.valueOf(640320) pow 3) / 24

	var sumA = context.valueOf(1)
	var sumB = context.valueOf(0)

	var a = context.valueOf(1)
	var dividendTerm1 = context.valueOf(5) // -(6*k - 5)
	var dividendTerm2 = context.valueOf(-1) // 2*k - 1
	var dividendTerm3 = context.valueOf(-1) // 6*k - 1
	
	val iterationCount = (context.getPrecision()+13) / 14 + 1
	for (k in 1 .. iterationCount) {
		dividendTerm1 += -6
		dividendTerm2 += 2
		dividendTerm3 += 6
		
		val dividend = dividendTerm1 * dividendTerm2 * dividendTerm3
		val divisor = (context.valueOf(k) pow 3) * valueDivisor
		a *= dividend / divisor
		val b = a * k
		
		sumA += a
		sumB += b
	}
	
	val factor = sqrt(context.valueOf(10005)) * 426880;
	val pi = factor / (sumA * 13591409 + sumB * 545140134);
	
	return context(precision).valueOf(pi);
}
