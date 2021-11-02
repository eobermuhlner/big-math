package ch.obermuhlner.math.big;

import static ch.obermuhlner.math.big.BigRational.ONE;
import static ch.obermuhlner.math.big.BigRational.ZERO;
import static ch.obermuhlner.math.big.BigRational.bernoulli;
import static ch.obermuhlner.math.big.BigRational.max;
import static ch.obermuhlner.math.big.BigRational.min;
import static ch.obermuhlner.math.big.BigRational.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

/**
 * Tests {@link BigRational}.
 */
public class BigRationalTest {
	private static final String PI_STRING = "3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651";

	@Test
	public void testSerialize() throws IOException, ClassNotFoundException {
		assertSerialize(ZERO);
		assertSerialize(ONE);
		assertSerialize(valueOf(2, 3));
	}

	private void assertSerialize(Object object) throws IOException, ClassNotFoundException {
		assertEquals(object, deserialize(serialize(object)));
	}

	private byte[] serialize(Object object) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(object);
		objectOutputStream.close();
		return byteArrayOutputStream.toByteArray();
	}

	private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		return objectInputStream.readObject();
	}

	/**
	 * Tests {@link BigRational#valueOf(int)}.
	 */
	@Test
	public void testValueOfInt() {
		assertSame(ZERO, valueOf(0));
		assertSame(ONE, valueOf(1));

		assertEquals("0", valueOf(0).toString());
		assertEquals("123", valueOf(123).toString());
		assertEquals("-123", valueOf(-123).toString());
	}

	/**
	 * Tests {@link BigRational#valueOf(int, int)}.
	 */
	@Test
	public void testValueOfRationalInt() {
		assertSame(ZERO, valueOf(0, 1));
		assertSame(ZERO, valueOf(0, 2));
		assertSame(ZERO, valueOf(0, -3));
		assertSame(ONE, valueOf(1,1));
		assertSame(ONE, valueOf(2,2).reduce()); // needs reduce

		assertEquals("0.5", valueOf(1, 2).toString());
		assertEquals(BigInteger.valueOf(1), valueOf(1, 2).getNumeratorBigInteger());
		assertEquals(BigInteger.valueOf(2), valueOf(1, 2).getDenominatorBigInteger());

		assertEquals("1/2", valueOf(1, 2).toRationalString());
		assertEquals("2/4", valueOf(2, 4).toRationalString());
		assertEquals("1/2", valueOf(2, 4).reduce().toRationalString()); // needs reduce
	}

	/**
	 * Tests {@link BigRational#valueOf(int, int, int)}.
	 */
	@Test
	public void testValueOfIntegerRationalInt() {
		assertSame(ZERO, valueOf(0, 0, 1));
		
		assertEquals("3.5", valueOf(3, 1, 2).toString());
		assertEquals("-3.5", valueOf(-3, 1, 2).toString());
	}

	/**
	 * Tests {@link BigRational#valueOf(int, int, int)} with 0 denominator.
	 */
	@Test(expected = ArithmeticException.class)
	public void testValueOfIntegerRationalIntDenominator0() {
		valueOf(1, 2, 0);
	}
	
	/**
	 * Tests {@link BigRational#valueOf(int, int, int)} with all arguments 0 (including denominator).
	 */
	@Test(expected = ArithmeticException.class)
	public void testValueOfIntegerRationalIntAll0() {
		valueOf(0, 0, 0);
	}

	/**
	 * Tests {@link BigRational#valueOf(int, int, int)} with negative numerator.
	 */
	@Test(expected = ArithmeticException.class)
	public void testValueOfIntegerRationalIntNegativeFractionNumerator() {
		valueOf(1, -2, 3);
	}

	/**
	 * Tests {@link BigRational#valueOf(int, int, int)} with negative denominator.
	 */
	@Test(expected = ArithmeticException.class)
	public void testValueOfIntegerRationalIntNegativeFractionDenominator() {
		valueOf(1, 2, -3);
	}

	/**
	 * Tests {@link BigRational#valueOf(BigInteger, BigInteger)}.
	 */
	@Test
	public void testValueOfRationalBigInteger() {
		assertSame(ZERO, valueOf(BigInteger.ZERO, BigInteger.ONE));
		assertSame(ZERO, valueOf(BigInteger.ZERO, BigInteger.valueOf(2)));
		assertSame(ZERO, valueOf(BigInteger.ZERO, BigInteger.valueOf(-3)));
		assertSame(ONE, valueOf(BigInteger.ONE, BigInteger.ONE));
		assertSame(ONE, valueOf(BigInteger.TEN, BigInteger.TEN).reduce()); // needs reduce

		assertEquals("1/10", valueOf(BigInteger.ONE, BigInteger.TEN).toRationalString());
	}
	
	/**
	 * Tests {@link BigRational#valueOf(int, int)} with second argument 0. 
	 */
	@Test(expected = ArithmeticException.class)
	public void testValueOfRationalIntDivideByZero() {
		valueOf(3, 0);
	}

	/**
	 * Tests {@link BigRational#valueOf(double)}.
	 */
	@Test
	public void testValueOfDouble() {
		assertSame(ZERO, valueOf(0.0));
		assertSame(ONE, valueOf(1.0));

		assertEquals("0", valueOf(0.0).toString());
		assertEquals("123", valueOf(123).toString());
		assertEquals("-123", valueOf(-123).toString());
		assertEquals("123.456", valueOf(123.456).toString());
		assertEquals("-123.456", valueOf(-123.456).toString());
	}

	/**
	 * Tests {@link BigRational#valueOf(double)} with {@link Double#POSITIVE_INFINITY}.
	 */
	@Test(expected=NumberFormatException.class)
	public void testValueOfDoublePositiveInfinity() {
		valueOf(Double.POSITIVE_INFINITY);
	}
	
	/**
	 * Tests {@link BigRational#valueOf(double)} with {@link Double#NEGATIVE_INFINITY}.
	 */
	@Test(expected=NumberFormatException.class)
	public void testValueOfDoubleNegativeInfinity() {
		valueOf(Double.NEGATIVE_INFINITY);
	}
	
	/**
	 * Tests {@link BigRational#valueOf(double)} with {@link Double#NaN}.
	 */
	@Test(expected=NumberFormatException.class)
	public void testValueOfDoubleNaN() {
		valueOf(Double.NaN);
	}
	
	/**
	 * Tests {@link BigRational#valueOf(BigInteger)}.
	 */
	@Test
	public void testValueOfBigInteger() {
		assertSame(ZERO, valueOf(BigInteger.ZERO));
		assertSame(ONE, valueOf(BigInteger.ONE));

		assertEquals("0", valueOf(BigInteger.ZERO).toString());
		assertEquals("123", valueOf(BigInteger.valueOf(123)).toString());
		assertEquals("-123", valueOf(BigInteger.valueOf(-123)).toString());
	}

	/**
	 * Tests {@link BigRational#valueOf(BigDecimal)}.
	 */
	@Test
	public void testValueOfBigDecimal() {
		assertSame(ZERO, valueOf(BigDecimal.ZERO));
		assertSame(ONE, valueOf(BigDecimal.ONE));

		assertEquals("0", valueOf(new BigDecimal("0")).toString());
		assertEquals("123", valueOf(new BigDecimal("123")).toString());
		assertEquals("-123", valueOf(new BigDecimal("-123")).toString());
		assertEquals("123.456", valueOf(new BigDecimal("123.456")).toString());
		assertEquals("-123.456", valueOf(new BigDecimal("-123.456")).toString());
	}

	/**
	 * Tests {@link BigRational#valueOf(BigDecimal)}.
	 */
	@Test
	public void testValueOfRationalBigDecimal() {
		assertSame(ZERO, valueOf(BigDecimal.ZERO, BigDecimal.ONE));
		assertSame(ZERO, valueOf(BigDecimal.ZERO, BigDecimal.TEN));
		assertSame(ONE, valueOf(BigDecimal.ONE, BigDecimal.ONE));

		assertEquals("123", valueOf(new BigDecimal("123"), new BigDecimal("1")).toString());
		assertEquals("12.3", valueOf(new BigDecimal("123"), new BigDecimal("10")).toString());
		assertEquals("123", valueOf(new BigDecimal("12.3"), new BigDecimal("0.1")).toString());
		assertEquals("1230", valueOf(new BigDecimal("123"), new BigDecimal("0.1")).toString());
	}

	/**
	 * Tests {@link BigRational#valueOf(String)}.
	 */
	@Test
	public void testValueOfString() {
		assertSame(ZERO, valueOf("0"));
		assertSame(ONE, valueOf("1"));
		assertSame(ZERO, valueOf("0.0"));
		assertSame(ZERO, valueOf("0/1"));
		assertSame(ZERO, valueOf("0/2"));

		assertEquals("123", valueOf("123").toString());
		assertEquals("123.456", valueOf("123.456").toString());

		assertEquals("-123", valueOf("-246/2").toString());
		assertEquals("-1234.56", valueOf("123.456/-0.1").toString());
		assertEquals("123456", valueOf("1.23456E5").toString());
		assertEquals("-123456", valueOf("-1.23456E5").toString());
		assertEquals("12300000", valueOf("123E5").toString());
		assertEquals("-12300000", valueOf("-123E5").toString());

		//assertEquals("X", valueOfSimple2("123").toString());
	}

	@Test
	public void testValueOfString3() {
		assertSame(ZERO, valueOf(true, null, null, null, null));
		assertSame(ZERO, valueOf(true, "", "", "", ""));
		assertSame(ZERO, valueOf(true, "0", "", "", ""));
		assertSame(ZERO, valueOf(true, "0", "0", "0", "0"));

		assertEquals("0.456", valueOf(true, "0", "456", "", "").toString());
		assertEquals("123.45", valueOf(true, "123", "45", "", "").toString());
		assertEquals("1/3", valueOf(true, "", "", "3", "").reduce().toRationalString());
		assertEquals("4/3", valueOf(true, "1", "", "3", "").reduce().toRationalString());
		assertEquals("37/30", valueOf(true, "1", "2", "3", "").reduce().toRationalString());

		assertEquals("-123.45", valueOf(false, "123", "45", "", "").toString());

		assertEquals("123450", valueOf(true, "123", "45", "", "3").toString());
		assertEquals("1.2345", valueOf(true, "123", "45", "", "-2").toString());
	}

	/**
	 * Tests {@link BigRational#isZero()}.
	 */
	@Test
	public void testIsZero() {
		assertEquals(true, valueOf(0).isZero());
		
		assertEquals(false, valueOf(1).isZero());
		assertEquals(false, valueOf(0.5).isZero());
		assertEquals(false, valueOf(-1).isZero());
		assertEquals(false, valueOf(-0.5).isZero());
	}
	
	/**
	 * Tests {@link BigRational#isInteger()}.
	 */
	@Test
	public void testIsInteger() {
		assertEquals(true, valueOf(0).isInteger());
		assertEquals(true, valueOf(1).isInteger());
		assertEquals(true, valueOf(-1).isInteger());
		assertEquals(true, valueOf(4, 4).isInteger());
		assertEquals(true, valueOf(4, 2).isInteger());
		
		assertEquals(false, valueOf(0.5).isInteger());
		assertEquals(false, valueOf(-0.5).isInteger());
	}
	
	/**
	 * Tests {@link BigRational#toString()}.
	 */
	@Test
	public void testToString() {
		assertEquals("0", valueOf(0).toString());
		assertEquals("123", valueOf(123).toString());
		assertEquals("0.25", valueOf(1, 4).toString());
		assertEquals("2", valueOf(4, 2).toString());
		assertEquals("-0.25", valueOf(-1, 4).toString());
		assertEquals("-2", valueOf(-4, 2).toString());
	}
	
	/**
	 * Tests {@link BigRational#toRationalString()}.
	 */
	@Test
	public void testToRationalString() {
		assertEquals("0", valueOf(0).toRationalString());
		assertEquals("123", valueOf(123).toRationalString());
		assertEquals("2/3", valueOf(2, 3).toRationalString());
		assertEquals("-2/3", valueOf(-2, 3).toRationalString());
		assertEquals("-2/3", valueOf(2, -3).toRationalString());
		
		assertEquals("4/4", valueOf(4, 4).toRationalString()); // not reduced
	}
	
	/**
	 * Tests {@link BigRational#toRationalString()}.
	 */
	@Test
	public void testToIntegerRationalString() {
		assertEquals("0", valueOf(0).toIntegerRationalString());
		assertEquals("1", valueOf(1).toIntegerRationalString());
		assertEquals("-1", valueOf(-1).toIntegerRationalString());

		assertEquals("1/2", valueOf(1, 2).toIntegerRationalString());
		assertEquals("-1/2", valueOf(-1, 2).toIntegerRationalString());

		assertEquals("1 2/3", valueOf(1, 2, 3).toIntegerRationalString());
		assertEquals("-1 2/3", valueOf(-1, 2, 3).toIntegerRationalString());
	}

	/**
	 * Tests {@link BigRational#toPlainString()}.
	 */
	@Test
	public void testToPlainString() {
		assertEquals("0", valueOf(0).toPlainString());
		assertEquals("123", valueOf(123).toPlainString());
		assertEquals("0.5", valueOf(1, 2).toPlainString());
		assertEquals("-0.5", valueOf(-1, 2).toPlainString());
		assertEquals("-0.5", valueOf(1, -2).toPlainString());
		assertEquals("0.6666666666666666666666666666666667", valueOf(2, 3).toPlainString());

		assertEquals("1", valueOf(4, 4).toPlainString());
	}

	/**
	 * Tests {@link BigRational#toDouble()}.
	 */
	@Test
	public void testToDouble() {
		assertEquals(0.0, valueOf(0).toDouble(), 0.0);
		assertEquals(123.0, valueOf(123.0).toDouble(), 0.0);
		assertEquals(123.4, valueOf(123.4).toDouble(), 0.0);
		assertEquals(-123.0, valueOf(-123.0).toDouble(), 0.0);
		assertEquals(-123.4, valueOf(-123.4).toDouble(), 0.0);
		assertEquals(0.3333333333333333, valueOf(1, 3).toDouble(), 0.0);
		assertEquals(0.6666666666666666, valueOf(2, 3).toDouble(), 0.0);

		assertEquals(8.804462619980757, valueOf("8.804462619980757911125181749462772084351").toDouble(), 0.0);
		assertEquals(8.804462619980757, valueOf("8.80446261998075791112518174946277208435").toDouble(), 0.0);

		assertEquals(5E-21, valueOf(BigDecimal.ONE, new BigDecimal("2E20")).toDouble(), 0.0);
		assertEquals(5E-101, valueOf(BigDecimal.ONE, new BigDecimal("2E100")).toDouble(), 0.0);
		assertEquals(0.0, valueOf(BigDecimal.ONE, new BigDecimal("2E9999")).toDouble(), 0.0); // underflow to +0.0
		assertEquals(0.0, valueOf(BigDecimal.ONE, new BigDecimal("-2E9999")).toDouble(), 0.0); // underflow to +0.0
		assertEquals(0, Double.compare(+0.0, valueOf(BigDecimal.ONE, new BigDecimal("2E9999")).toDouble())); // underflow to +0.0
		assertEquals(0, Double.compare(-0.0, valueOf(BigDecimal.ONE, new BigDecimal("-2E9999")).toDouble())); // underflow to -0.0

		assertEquals(2E20f, valueOf(new BigDecimal("2E20")).toFloat(), 0.0);
		assertEquals(Double.POSITIVE_INFINITY, valueOf(new BigDecimal("2E100")).toFloat(), 0.0); // overflow to +infinity
		assertEquals(-2E20f, valueOf(new BigDecimal("-2E20")).toFloat(), 0.0);
		assertEquals(Double.NEGATIVE_INFINITY, valueOf(new BigDecimal("-2E100")).toFloat(), 0.0); // overflow to -infinity
	}
	
	/**
	 * Tests {@link BigRational#toFloat()}.
	 */
	@Test
	public void testToFloat() {
		assertEquals(0.0f, valueOf(0).toFloat(), 0.0);
		assertEquals(123.0f, valueOf(123.0).toFloat(), 0.0);
		assertEquals(123.4f, valueOf(123.4).toFloat(), 0.0);
		assertEquals(-123.0f, valueOf(-123.0).toFloat(), 0.0);
		assertEquals(-123.4f, valueOf(-123.4).toFloat(), 0.0);
		assertEquals(0.33333333f, valueOf(1, 3).toFloat(), 0.0);
		assertEquals(0.6666667f, valueOf(2, 3).toFloat(), 0.0);

		assertEquals(8.804462f, valueOf("8.804462619980757911125181749462772084351").toFloat(), 0.0);
		assertEquals(8.804462f, valueOf("8.80446261998075791112518174946277208435").toFloat(), 0.0);

		assertEquals(5E-21f, valueOf(BigDecimal.ONE, new BigDecimal("2E20")).toFloat(), 0.0);
		assertEquals(0.0f, valueOf(BigDecimal.ONE, new BigDecimal("2E100")).toFloat(), 0.0); // underflow to +0.0f
		assertEquals(0.0f, valueOf(BigDecimal.ONE, new BigDecimal("2E9999")).toFloat(), 0.0); // underflow to +0.0f
		assertEquals(0, Float.compare(+0.0f, valueOf(BigDecimal.ONE, new BigDecimal("2E9999")).toFloat())); // underflow to +0.0f
		assertEquals(0, Float.compare(-0.0f, valueOf(BigDecimal.ONE, new BigDecimal("-2E9999")).toFloat())); // underflow to -0.0f

		assertEquals(2E20f, valueOf(new BigDecimal("2E20")).toFloat(), 0.0);
		assertEquals(Float.POSITIVE_INFINITY, valueOf(new BigDecimal("2E100")).toFloat(), 0.0); // overflow to +infinity
		assertEquals(-2E20f, valueOf(new BigDecimal("-2E20")).toFloat(), 0.0);
		assertEquals(Float.NEGATIVE_INFINITY, valueOf(new BigDecimal("-2E100")).toFloat(), 0.0); // overflow to -infinity
	}

	/**
	 * Tests {@link BigRational#integerPart()}.
	 */
	@Test
	public void testIntegerPart() {
		assertEquals("0", valueOf(2, 3).integerPart().toString());
		assertEquals("1", valueOf(4, 3).integerPart().toString());

		assertEquals("0", valueOf(-2, 3).integerPart().toString());
		assertEquals("-1", valueOf(-4, 3).integerPart().toString());
	}

	/**
	 * Tests {@link BigRational#fractionPart()}.
	 */
	@Test
	public void testFractionPart() {
		assertEquals("2/3", valueOf(2, 3).fractionPart().toRationalString());
		assertEquals("1/3", valueOf(4, 3).fractionPart().toRationalString());

		assertEquals("-2/3", valueOf(-2, 3).fractionPart().toRationalString());
		assertEquals("-1/3", valueOf(-4, 3).fractionPart().toRationalString());
	}

	/**
	 * Tests {@link BigRational#withPrecision(int)}.
	 */
	@Test
	public void testWithPrecision() {
		assertEquals("123.456", valueOf(123.456).withPrecision(7).toString()); // unchanged
		assertEquals("123.456", valueOf(123.456).withPrecision(6).toString());
		assertEquals("123.46", valueOf(123.456).withPrecision(5).toString()); // rounding up
		assertEquals("123.5", valueOf(123.456).withPrecision(4).toString()); // rounding up
		assertEquals("123", valueOf(123.456).withPrecision(3).toString());
		assertEquals("120", valueOf(123.456).withPrecision(2).toString());
		assertEquals("100", valueOf(123.456).withPrecision(1).toString());
		
		assertEquals("123.456", valueOf(123.456).withPrecision(0).toString()); // unchanged
	}

	/**
	 * Tests {@link BigRational#withPrecision(int)} with negative precision.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testWithPrecisionIllegalPrecision() {
		assertEquals("123.456", valueOf(123.456).withPrecision(-1).toString());		
	}

	/**
	 * Tests {@link BigRational#withScale(int)}.
	 */
	@Test
	public void testWithScale() {
		assertEquals("123.456", valueOf(123.456).withScale(4).toString()); // unchanged
		assertEquals("123.456", valueOf(123.456).withScale(3).toString());
		assertEquals("123.46", valueOf(123.456).withScale(2).toString()); // rounding up
		assertEquals("123.5", valueOf(123.456).withScale(1).toString()); // rounding up
		assertEquals("123", valueOf(123.456).withScale(0).toString());
		assertEquals("120", valueOf(123.456).withScale(-1).toString());
		assertEquals("100", valueOf(123.456).withScale(-2).toString());
		assertEquals("0", valueOf(123.456).withScale(-3).toString());
		
		BigDecimal bigDecimalTestValue = new BigDecimal(PI_STRING);
		BigRational bigRationalTestValue = BigRational.valueOf(PI_STRING);
		for (int i = 0; i < 20; i++) {
			String referenceString = bigDecimalTestValue.setScale(i, RoundingMode.HALF_UP).toString();
			referenceString = referenceString.replaceAll("0+$", ""); // remove trailing '0'
			assertEquals("i="+i, referenceString, bigRationalTestValue.withScale(i).toString());
		}		
	}

	/**
	 * Tests {@link BigRational#equals(Object)}.
	 */
	@Test
	public void testEquals() {
		assertTrue(ZERO.equals(ZERO));
		assertTrue(ZERO.equals(valueOf(0, 99)));
		assertTrue(valueOf(33).equals(valueOf(33)));
		assertTrue(valueOf(1, 3).equals(valueOf(1, 3)));
		assertTrue(valueOf(-1, 3).equals(valueOf(1, -3)));

		assertFalse(ZERO.equals(null));
		assertFalse(ZERO.equals("string"));
		assertFalse(ZERO.equals(ONE));
		assertFalse(valueOf(1, 3).equals(valueOf(1, 4)));
	}

	/**
	 * Tests {@link BigRational#compareTo(BigRational)}.
	 */
	@Test
	public void testCompareTo() {
		assertEquals(0, ZERO.compareTo(ZERO));
		
		assertEquals(0, valueOf(1, 3).compareTo(valueOf(1, 3)));
		assertEquals(-1, valueOf(1, 4).compareTo(valueOf(1, 3)));
		assertEquals(1, valueOf(1, 2).compareTo(valueOf(1, 3)));
	}
	
	/**
	 * Tests {@link BigRational#hashCode()}.
	 */
	@Test
	public void testHashCode() {
		// no asserts, since hashCode() defines no concrete values
		ZERO.hashCode();
		
		valueOf(1, 3).hashCode();
	}
	
	/**
	 * Tests {@link BigRational#min(BigRational...)}.
	 */
	@Test
	public void testMin() {
		assertEquals(ZERO, min());

		assertEquals(valueOf(3), min(valueOf(3)));
		assertEquals(valueOf(-2), min(valueOf(-2)));
		assertEquals(valueOf(-2), min(valueOf(3), valueOf(-2)));
		assertEquals(valueOf(-2), min(valueOf(-2), valueOf(3)));
	}

	/**
	 * Tests {@link BigRational#max(BigRational...)}.
	 */
	@Test
	public void testMax() {
		assertEquals(ZERO, max());

		assertEquals(valueOf(3), max(valueOf(3)));
		assertEquals(valueOf(-2), max(valueOf(-2)));
		assertEquals(valueOf(3), max(valueOf(3), valueOf(-2)));
		assertEquals(valueOf(3), max(valueOf(-2), valueOf(3)));
	}

	/**
	 * Tests that the same instance {@link BigRational#ZERO} is returned from operations with result 0.
	 */
	@Test
	public void testSameZERO() {
		assertSame(ZERO, ZERO.negate());
		assertSame(ZERO, ZERO.abs());
		assertSame(ZERO, ZERO.add(ZERO));
		assertSame(ZERO, ZERO.subtract(ZERO));
		assertSame(ZERO, ZERO.multiply(ONE));
		assertSame(ZERO, ZERO.divide(ONE));
	}

	/**
	 * Tests that the same instance {@link BigRational#ZERO} is returned from operations with result 1.
	 */
	@Test
	public void testSameONE() {
		assertSame(ONE, ONE.negate().negate());
		assertSame(ONE, ONE.abs());
		assertSame(ONE, ONE.negate().abs());
		assertSame(ONE, ONE.add(ZERO));
		assertSame(ONE, ZERO.add(ONE));
		assertSame(ONE, ONE.subtract(ZERO));
		assertSame(ONE, ONE.multiply(ONE));
		assertSame(ONE, ONE.divide(ONE));
		assertSame(ONE, valueOf(3).divide(valueOf(3)).reduce()); // needs reduce
	}

	/**
	 * Tests {@link BigRational#negate()}.
	 */
	@Test
	public void testNegate() {
		assertEquals("-2", valueOf(2).negate().toString());

		assertEquals("0.5", valueOf(-0.5).negate().toString());
	}

	/**
	 * Tests {@link BigRational#reciprocal()}.
	 */
	@Test
	public void testReciprocal() {
		assertEquals("0.5", valueOf(2).reciprocal().toString());

		assertEquals("-2", valueOf(-0.5).reciprocal().toString());
	}

	/**
	 * Tests {@link BigRational#reciprocal()} with 0.
	 */
	@Test(expected = ArithmeticException.class)
	public void testReciprocalZero() {
		ZERO.reciprocal();
	}

	/**
	 * Tests {@link BigRational#abs()}.
	 */
	@Test
	public void testAbs() {
		assertEquals("2", valueOf(2).abs().toString());
		assertEquals("0.5", valueOf(-0.5).abs().toString());
	}

	/**
	 * Tests {@link BigRational#abs()} optimization to return the same instance for positive values.
	 */
	@Test
	public void testAbsOptimized() {
		BigRational L1 = valueOf(2);
		assertSame(L1, L1.abs());
	}

	/**
	 * Tests {@link BigRational#signum()}.
	 */
	@Test
	public void testSignum() {
		assertSame(0, ZERO.signum());
		assertSame(-1, valueOf(-47).signum());
		assertSame(1, valueOf(99).signum());
	}

	/**
	 * Tests {@link BigRational#increment()}.
	 */
	@Test
	public void testIncrement() {
		assertEquals("5", valueOf(4).increment().toString());
		assertEquals("1.2", valueOf(2, 10).increment().toString());
	}

	/**
	 * Tests {@link BigRational#decrement()}.
	 */
	@Test
	public void testDecrement() {
		assertEquals("3", valueOf(4).decrement().toString());
		assertEquals("-0.8", valueOf(2, 10).decrement().toString());
	}

	/**
	 * Tests {@link BigRational#add(BigRational)}.
	 */
	@Test
	public void testAdd() {
		assertEquals("2", valueOf(2).add(valueOf(0)).toString());
		assertEquals("5", valueOf(2).add(valueOf(3)).toString());

		assertEquals("200.03", valueOf(200).add(valueOf(0.03)).toString());
	}

	/**
	 * Tests {@link BigRational#add(BigRational)} if the denominator is the same (should be optimized).
	 */
	@Test
	public void testAddOptimized() {
		assertEquals("3/7", valueOf(2, 7).add(valueOf(1, 7)).toRationalString());
	}

	/**
	 * Tests {@link BigRational#add(int)}.
	 */
	@Test
	public void testAddInt() {
		assertEquals("2", valueOf(2).add(0).toString());
		
		assertEquals("5", valueOf(2).add(3).toString());
	}

	/**
	 * Tests {@link BigRational#add(BigInteger)}.
	 */
	@Test
	public void testAddBigInteger() {
		assertEquals("2", valueOf(2).add(BigInteger.valueOf(0)).toString());
		
		assertEquals("5", valueOf(2).add(BigInteger.valueOf(3)).toString());
	}

	/**
	 * Tests {@link BigRational#subtract(BigRational)}.
	 */
	@Test
	public void testSubtract() {
		assertEquals("2", valueOf(2).subtract(valueOf(0)).toString());
		assertEquals("-1", valueOf(2).subtract(valueOf(3)).toString());

		assertEquals("199.97", valueOf(200).subtract(valueOf(0.03)).toString());
	}

	/**
	 * Tests {@link BigRational#subtract(BigRational)} if the denominator is the same (should be optimized).
	 */
	@Test
	public void testSubtractOptimized() {
		assertEquals("2/7", valueOf(3, 7).subtract(valueOf(1, 7)).toRationalString());
	}
	
	/**
	 * Tests {@link BigRational#subtract(int)}.
	 */
	@Test
	public void testSubtractInt() {
		assertEquals("5", valueOf(5).subtract(0).toString());
		
		assertEquals("2", valueOf(5).subtract(3).toString());
	}

	/**
	 * Tests {@link BigRational#subtract(BigInteger)}.
	 */
	@Test
	public void testSubtractBigInteger() {
		assertEquals("5", valueOf(5).subtract(BigInteger.valueOf(0)).toString());
		
		assertEquals("2", valueOf(5).subtract(BigInteger.valueOf(3)).toString());
	}

	/**
	 * Tests {@link BigRational#multiply(BigRational)}.
	 */
	@Test
	public void testMultiply() {
		assertEquals("6", valueOf(2).multiply(valueOf(3)).toString());
		assertEquals("6", valueOf(3).multiply(valueOf(2)).toString());

		assertEquals("60", valueOf(300).multiply(valueOf(0.2)).toString());
		assertEquals("60", valueOf(0.2).multiply(valueOf(300)).toString());

		assertEquals("0.06", valueOf(0.3).multiply(valueOf(0.2)).toString());
		assertEquals("0.06", valueOf(0.2).multiply(valueOf(0.3)).toString());
		
		assertEquals("-0.06", valueOf(-0.3).multiply(valueOf(0.2)).toString());
		assertEquals("-0.06", valueOf(0.3).multiply(valueOf(-0.2)).toString());
	}

	/**
	 * Tests {@link BigRational#multiply(BigRational)} with certain special cases (should be optimizied).
	 */
	@Test
	public void testMultiplyOptimized() {
		// multiply with 0
		assertEquals("0", valueOf(2).multiply(valueOf(0)).toString());
		assertEquals("0", valueOf(0).multiply(valueOf(2)).toString());
		
		// multiply with 1
		assertEquals("2", valueOf(2).multiply(valueOf(1)).toString());
		assertEquals("2", valueOf(1).multiply(valueOf(2)).toString());
	}

	/**
	 * Tests {@link BigRational#multiply(int)}.
	 */
	@Test
	public void testMultiplyInt() {
		assertEquals("0.6", valueOf(0.2).multiply(3).toString());
	}

	/**
	 * Tests {@link BigRational#multiply(BigInteger)}.
	 */
	@Test
	public void testMultiplyBigInteger() {
		assertEquals("0", valueOf(2).multiply(BigInteger.valueOf(0)).toString());
		assertEquals("2", valueOf(2).multiply(BigInteger.valueOf(1)).toString());
		assertEquals("2", valueOf(1).multiply(BigInteger.valueOf(2)).toString());
		assertEquals("0.6", valueOf(0.2).multiply(BigInteger.valueOf(3)).toString());
	}

	/**
	 * Tests {@link BigRational#divide(BigRational)}.
	 */
	@Test
	public void testDivide() {
		assertEquals("2", valueOf(6).divide(valueOf(3)).toString());

		assertEquals("25", valueOf(5).divide(valueOf(0.2)).toString());
	}

	/**
	 * Tests {@link BigRational#divide(int)}.
	 */
	@Test
	public void testDivideInt() {
		assertEquals("2", valueOf(6).divide(3).toString());
	}

	/**
	 * Tests {@link BigRational#divide(BigInteger)}.
	 */
	@Test
	public void testDivideBigInteger() {
		assertEquals("6", valueOf(6).divide(BigInteger.valueOf(1)).toString());

		assertEquals("2", valueOf(6).divide(BigInteger.valueOf(3)).toString());
	}

	/**
	 * Tests {@link BigRational#divide(BigRational)} with 0.
	 */
	@Test(expected = ArithmeticException.class)
	public void testDivideByZero() {
		ONE.divide(ZERO);
	}

	/**
	 * Tests {@link BigRational#pow(int)}.
	 */
	@Test
	public void testPowInt() {
		assertEquals(String.valueOf((int) Math.pow(2, 3)), valueOf(2).pow(3).toString());
		assertEquals(String.valueOf((int) Math.pow(-2, 3)), valueOf(-2).pow(3).toString());

		assertEquals("1000", valueOf(10).pow(3).toString());
		assertEquals("0.001", valueOf(10).pow(-3).toString());

		BigRational L1 = valueOf(0.02);
		assertEquals(L1.multiply(L1).multiply(L1), L1.pow(3));

		assertSame(L1, L1.pow(1));
	}
	
	/**
	 * Tests {@link BigRational#bernoulli(int)}.
	 */
	@Test
	public void testBernoulli() {
		assertEquals("1", bernoulli(0).reduce().toRationalString());
		assertEquals("-1/2", bernoulli(1).reduce().toRationalString());
		assertEquals("1/6", bernoulli(2).reduce().toRationalString());
		assertEquals("0", bernoulli(3).reduce().toRationalString());
		assertEquals("-1/30", bernoulli(4).reduce().toRationalString());
		assertEquals("0", bernoulli(5).reduce().toRationalString());
		assertEquals("1/42", bernoulli(6).reduce().toRationalString());
		assertEquals("0", bernoulli(7).reduce().toRationalString());
		assertEquals("-1/30", bernoulli(8).reduce().toRationalString());
		assertEquals("0", bernoulli(9).reduce().toRationalString());
		assertEquals("5/66", bernoulli(10).reduce().toRationalString());
		assertEquals("0", bernoulli(11).reduce().toRationalString());
		assertEquals("-691/2730", bernoulli(12).reduce().toRationalString());
		assertEquals("0", bernoulli(13).reduce().toRationalString());
		assertEquals("7/6", bernoulli(14).reduce().toRationalString());
		assertEquals("0", bernoulli(15).reduce().toRationalString());
		assertEquals("-3617/510", bernoulli(16).reduce().toRationalString());
		assertEquals("0", bernoulli(17).reduce().toRationalString());
		assertEquals("43867/798", bernoulli(18).reduce().toRationalString());
	}

	@Test(expected = ArithmeticException.class)
	public void testBernoulliNegative() {
		bernoulli(-1);
	}
}
