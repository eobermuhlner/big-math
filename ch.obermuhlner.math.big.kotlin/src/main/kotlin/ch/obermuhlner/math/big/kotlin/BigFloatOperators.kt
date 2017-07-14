package ch.obermuhlner.math.big.kotlin

import java.math.BigDecimal

import ch.obermuhlner.math.big.BigFloat

operator fun BigFloat.plus(other: BigFloat) : BigFloat = this.add(other)
operator fun BigFloat.plus(other: BigDecimal) : BigFloat = this.add(other)
operator fun BigFloat.plus(other: Double) : BigFloat = this.add(other)
operator fun BigFloat.plus(other: Long) : BigFloat = this.add(other)
operator fun BigFloat.plus(other: Int) : BigFloat = this.add(other)

operator fun BigFloat.minus(other: BigFloat) : BigFloat = this.subtract(other)
operator fun BigFloat.minus(other: BigDecimal) : BigFloat = this.subtract(other)
operator fun BigFloat.minus(other: Double) : BigFloat = this.subtract(other)
operator fun BigFloat.minus(other: Long) : BigFloat = this.subtract(other)
operator fun BigFloat.minus(other: Int) : BigFloat = this.subtract(other)

operator fun BigFloat.times(other: BigFloat) : BigFloat = this.multiply(other)
operator fun BigFloat.times(other: BigDecimal) : BigFloat = this.multiply(other)
operator fun BigFloat.times(other: Double) : BigFloat = this.multiply(other)
operator fun BigFloat.times(other: Long) : BigFloat = this.multiply(other)
operator fun BigFloat.times(other: Int) : BigFloat = this.multiply(other)

operator fun BigFloat.div(other: BigFloat) : BigFloat = this.divide(other)
operator fun BigFloat.div(other: BigDecimal) : BigFloat = this.divide(other)
operator fun BigFloat.div(other: Double) : BigFloat = this.divide(other)
operator fun BigFloat.div(other: Long) : BigFloat = this.divide(other)
operator fun BigFloat.div(other: Int) : BigFloat = this.divide(other)

operator fun BigFloat.rem(other: BigFloat) : BigFloat = this.remainder(other)
operator fun BigFloat.rem(other: BigDecimal) : BigFloat = this.remainder(other)
operator fun BigFloat.rem(other: Double) : BigFloat = this.remainder(other)
operator fun BigFloat.rem(other: Long) : BigFloat = this.remainder(other)
operator fun BigFloat.rem(other: Int) : BigFloat = this.remainder(other)

operator fun BigFloat.unaryMinus() : BigFloat = BigFloat.negate(this);

operator fun BigFloat.inc() : BigFloat = this.add(1)

operator fun BigFloat.dec() : BigFloat = this.subtract(1)

infix fun BigFloat.pow(other: BigFloat) : BigFloat = BigFloat.pow(this, other);
infix fun BigFloat.pow(other: BigDecimal) : BigFloat = BigFloat.pow(this, this.context.valueOf(other));
infix fun BigFloat.pow(other: Double) : BigFloat = BigFloat.pow(this, this.context.valueOf(other));
infix fun BigFloat.pow(other: Long) : BigFloat = BigFloat.pow(this, this.context.valueOf(other));
infix fun BigFloat.pow(other: Int) : BigFloat = BigFloat.pow(this, this.context.valueOf(other));

infix fun BigFloat.root(other: BigFloat) : BigFloat = BigFloat.root(this, other);
infix fun BigFloat.root(other: BigDecimal) : BigFloat = BigFloat.root(this, this.context.valueOf(other));
infix fun BigFloat.root(other: Double) : BigFloat = BigFloat.root(this, this.context.valueOf(other));
infix fun BigFloat.root(other: Long) : BigFloat = BigFloat.root(this, this.context.valueOf(other));
infix fun BigFloat.root(other: Int) : BigFloat = BigFloat.root(this, this.context.valueOf(other));

