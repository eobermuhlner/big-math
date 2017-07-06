package ch.obermuhlner.math.big.viewer;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class BigDecimalUtilTest {

	@Test
	public void testToString() {
		assertEquals("0", BigDecimalUtil.toString(new BigDecimal("0")));
		assertEquals("1.23", BigDecimalUtil.toString(new BigDecimal("1.23")));
		assertEquals("1.23", BigDecimalUtil.toString(new BigDecimal("1.230")));
		assertEquals("1.23", BigDecimalUtil.toString(new BigDecimal("1.23000")));
		assertEquals("123000", BigDecimalUtil.toString(new BigDecimal("123000")));
		assertEquals("123000", BigDecimalUtil.toString(new BigDecimal("123000.000")));
	}
}
