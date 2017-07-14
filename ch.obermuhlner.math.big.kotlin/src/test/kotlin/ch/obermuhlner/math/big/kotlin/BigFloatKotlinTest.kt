package ch.obermuhlner.math.big.kotlin

import org.junit.Test
import org.junit.Assert.assertEquals

import ch.obermuhlner.math.big.*
import java.math.BigDecimal

class BigFloatKotlinTest {
	
	@Test
	fun testOperatorPlus() {
		val context = BigFloat.context(100)
		
		assertEquals(context.valueOf(5), context.valueOf(2) + 3)
		assertEquals(context.valueOf(5), context.valueOf(2) + 3L)
		assertEquals(context.valueOf(5), context.valueOf(2) + 3.0)
		assertEquals(context.valueOf(5), context.valueOf(2) + BigDecimal.valueOf(3.0))
		assertEquals(context.valueOf(5), context.valueOf(2) + context.valueOf(3))
	}
	
	@Test
	fun testOperatorMinus() {
		val context = BigFloat.context(100)
		
		assertEquals(context.valueOf(2), context.valueOf(5) - 3)
		assertEquals(context.valueOf(2), context.valueOf(5) - 3L)
		assertEquals(context.valueOf(2), context.valueOf(5) - 3.0)
		assertEquals(context.valueOf(2), context.valueOf(5) - BigDecimal.valueOf(3.0))
		assertEquals(context.valueOf(2), context.valueOf(5) - context.valueOf(3))
	}

	@Test
	fun testOperatorTimes() {
		val context = BigFloat.context(100)
		
		assertEquals(context.valueOf(6), context.valueOf(2) * 3)
		assertEquals(context.valueOf(6), context.valueOf(2) * 3L)
		assertEquals(context.valueOf(6), context.valueOf(2) * 3.0)
		assertEquals(context.valueOf(6), context.valueOf(2) * BigDecimal.valueOf(3.0))
		assertEquals(context.valueOf(6), context.valueOf(2) * context.valueOf(3))
	}

	@Test
	fun testOperatorDiv() {
		val context = BigFloat.context(100)
		
		assertEquals(context.valueOf(2), context.valueOf(6) / 3)
		assertEquals(context.valueOf(2), context.valueOf(6) / 3L)
		assertEquals(context.valueOf(2), context.valueOf(6) / 3.0)
		assertEquals(context.valueOf(2), context.valueOf(6) / BigDecimal.valueOf(3.0))
		assertEquals(context.valueOf(2), context.valueOf(6) / context.valueOf(3))
		
		assertEquals(context.valueOf(0.125), context.valueOf(1) / 8)
		assertEquals(context.valueOf(0.125), context.valueOf(1) / 8L)
		assertEquals(context.valueOf(0.125), context.valueOf(1) / 8.0)
		assertEquals(context.valueOf(0.125), context.valueOf(1) / BigDecimal.valueOf(8.0))
		assertEquals(context.valueOf(0.125), context.valueOf(1) / context.valueOf(8))
	}

	@Test
	fun testOperatorUnaryMinus() {
		val context = BigFloat.context(100)
		
		assertEquals(context.valueOf(-5), - context.valueOf(5))
	}

	@Test
	fun testInfixPow() {
		val context = BigFloat.context(100)
		
		assertEquals(context.valueOf(8), context.valueOf(2) pow 3)
		assertEquals(context.valueOf(8), context.valueOf(2) pow 3L)
		assertEquals(context.valueOf(8), context.valueOf(2) pow 3.0)
		assertEquals(context.valueOf(8), context.valueOf(2) pow BigDecimal.valueOf(3.0))
		assertEquals(context.valueOf(8), context.valueOf(2) pow context.valueOf(3))
	}

	@Test
	fun testInfixRoot() {
		val context = BigFloat.context(100)
		
		assertEquals(context.valueOf(2), context.valueOf(8) root 3)
		assertEquals(context.valueOf(2), context.valueOf(8) root 3L)
		assertEquals(context.valueOf(2), context.valueOf(8) root 3.0)
		assertEquals(context.valueOf(2), context.valueOf(8) root BigDecimal.valueOf(3.0))
		assertEquals(context.valueOf(2), context.valueOf(8) root context.valueOf(3))
	}

	@Test
	fun testOperatorInc() {
		val context = BigFloat.context(100)
		
		var v1 = context.valueOf(5)
		assertEquals(context.valueOf(5), v1++)
		assertEquals(context.valueOf(6), v1)
		
		v1 = context.valueOf(5)
		assertEquals(context.valueOf(6), ++v1)
		assertEquals(context.valueOf(6), v1)
	}

	@Test
	fun testOperatorDec() {
		val context = BigFloat.context(100)
		
		var v1 = context.valueOf(5)
		assertEquals(context.valueOf(5), v1--)
		assertEquals(context.valueOf(4), v1)
		
		v1 = context.valueOf(5)
		assertEquals(context.valueOf(4), --v1)
		assertEquals(context.valueOf(4), v1)
	}
}
