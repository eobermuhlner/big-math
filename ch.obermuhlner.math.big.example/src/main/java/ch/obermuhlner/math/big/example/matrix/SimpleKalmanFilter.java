package ch.obermuhlner.math.big.example.matrix;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;

import java.math.MathContext;

// Based on: http://ejml.org/wiki/index.php?title=Example_Kalman_Filter

public class SimpleKalmanFilter implements KalmanFilter {
    private static MathContext MC = MathContext.DECIMAL32;

    // kinematics description
    private BigMatrix F,Q,H;

    // sytem state estimate
    private BigMatrix x,P;

    @Override
    public void configure(BigMatrix F, BigMatrix Q, BigMatrix H) {
        this.F = F;
        this.Q = Q;
        this.H = H;
    }

    @Override
    public void setState(BigMatrix x, BigMatrix P) {
        this.x = x;
        this.P = P;
    }

    @Override
    public void predict() {
        // x = F x
        x = F.multiply(x, MC);

        // P = F P F' + Q
        P = F.multiply(P).multiply(F.transpose()).add(Q, MC);
    }

    @Override
    public void update(BigMatrix z, BigMatrix R) {
        // y = z - H x
        BigMatrix y = z.subtract(H.multiply(x));

        // S = H P H' + R
        BigMatrix S = H.multiply(P).multiply(H.transpose()).add(R);

        // K = PH'S^(-1)
        BigMatrix Sinv = S.invert(MC);
        BigMatrix K = P.multiply(H.transpose().multiply(Sinv));

        // x = x + Ky
        x = x.add(K.multiply(y, MC));

        // P = (I-kH)P = P - KHP
        P = P.subtract(K.multiply(H).multiply(P), MC);
    }

    @Override
    public BigMatrix getState() {
        return x;
    }

    @Override
    public BigMatrix getCovariance() {
        return P;
    }
}