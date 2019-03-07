package ch.obermuhlner.math.big.example.internal;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalExperiments {
    public static void main(String[] args) {
        System.out.println("### BigDecimal Values");
        print(new BigDecimal("1.23E+100"));
        print(new BigDecimal("1.23E+3"));
        print(new BigDecimal("1230"));
        print(new BigDecimal("123"));
        print(new BigDecimal("12.3"));
        print(new BigDecimal("1.23"));
        print(new BigDecimal("0.123"));
        print(new BigDecimal("0.0123"));
        print(new BigDecimal("0.00123"));
        print(new BigDecimal("1.23E-3"));
        print(new BigDecimal("1.23E-100"));
        print(new BigDecimal("1.230"));
        print(new BigDecimal("1.23000000"));
        System.out.println();

        MathContext mc = new MathContext(5);
        System.out.println("### roundWithTrailingZeroes " + mc);
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23E+100"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23E+3"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1230"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("123"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("12.3"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.123"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.0123"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.00123"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23E-3"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23E-100"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.230"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.23000000"), mc));
        System.out.println();
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567E+100"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567E+3"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1234.567"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("123.4567"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("12.34567"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.1234567"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.01234567"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("0.001234567"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567E-3"), mc));
        print(BigDecimalMath.roundWithTrailingZeroes(new BigDecimal("1.234567E-100"), mc));
        System.out.println();

        System.out.println("### round " + mc);
        print(BigDecimalMath.round(new BigDecimal("1.234567E+100"), mc));
        print(BigDecimalMath.round(new BigDecimal("1.234567E+3"), mc));
        print(BigDecimalMath.round(new BigDecimal("1234.567"), mc));
        print(BigDecimalMath.round(new BigDecimal("123.4567"), mc));
        print(BigDecimalMath.round(new BigDecimal("12.34567"), mc));
        print(BigDecimalMath.round(new BigDecimal("1.234567"), mc));
        print(BigDecimalMath.round(new BigDecimal("0.1234567"), mc));
        print(BigDecimalMath.round(new BigDecimal("0.01234567"), mc));
        print(BigDecimalMath.round(new BigDecimal("0.001234567"), mc));
        print(BigDecimalMath.round(new BigDecimal("1.234567E-3"), mc));
        print(BigDecimalMath.round(new BigDecimal("1.234567E-100"), mc));
        System.out.println();


        System.out.println("### setScale " + 5);
        print(new BigDecimal("1.234567E+100").setScale(5));
        print(new BigDecimal("1.234567E+3").setScale(5));
        print(new BigDecimal("1234.567").setScale(5));
        print(new BigDecimal("123.4567").setScale(5));
        print(new BigDecimal("12.34567").setScale(5));
        print(new BigDecimal("1.234567").setScale(5));
        print(new BigDecimal("0.1234567").setScale(5));
        print(new BigDecimal("0.01234567").setScale(5));
        print(new BigDecimal("0.001234567").setScale(5));
        print(new BigDecimal("1.234567E-3").setScale(5));
        print(new BigDecimal("1.234567E-100").setScale(5));
        System.out.println();
    }

    public static void print(BigDecimal value) {
        System.out.println(value + " scale=" + value.scale() + " precision=" + value.precision() + " significant=" + BigDecimalMath.significantDigits(value) + " exponent=" + BigDecimalMath.exponent(value));
    }
}
