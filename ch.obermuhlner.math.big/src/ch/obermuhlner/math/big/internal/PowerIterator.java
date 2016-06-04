package ch.obermuhlner.math.big.internal;

import java.math.BigDecimal;

public interface PowerIterator {
	BigDecimal getCurrentPower();
	void calculateNextPower();
}