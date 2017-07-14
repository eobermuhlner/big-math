package ch.obermuhlner.math.big.kotlin.example

import ch.obermuhlner.math.big.BigFloat
import ch.obermuhlner.math.big.kotlin.*

fun main(args: Array<String>) {
	simpleExample()
}

fun simpleExample() {
	val context = BigFloat.context(100)
	val v1 = context.valueOf(2) / 3

	println(v1)
}
