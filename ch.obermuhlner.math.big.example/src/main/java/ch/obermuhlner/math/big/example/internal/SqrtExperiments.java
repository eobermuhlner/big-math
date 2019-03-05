package ch.obermuhlner.math.big.example.internal;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

public class SqrtExperiments {
    public static void main(String[] args) {
        java9Sqrt();
    }

    private static void java9Sqrt() {
        MathContext mc = new MathContext(40);
        BigDecimal v = new BigDecimal("1.00000000000000000000001");
        BigDecimal v2 = v.multiply(v);

        /*
        System.out.println("java9    " + BigDecimal.valueOf(2).sqrt(mc));
        System.out.println("java9    " + BigDecimal.valueOf(4).sqrt(mc));
        System.out.println("java9    " + BigDecimal.valueOf(0.01).sqrt(mc));
        System.out.println("java9    " + v2.sqrt(mc));
        */

        System.out.println("big-math " + BigDecimalMath.sqrt(BigDecimal.valueOf(2), mc));
        System.out.println("big-math " + BigDecimalMath.sqrt(BigDecimal.valueOf(4), mc));
        System.out.println("big-math " + BigDecimalMath.sqrt(BigDecimal.valueOf(0.01), mc));
        System.out.println("big-math " + BigDecimalMath.sqrt(v2, mc));
    }
}
