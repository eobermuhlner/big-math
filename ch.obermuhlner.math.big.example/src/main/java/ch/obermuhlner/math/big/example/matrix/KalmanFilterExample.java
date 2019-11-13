package ch.obermuhlner.math.big.example.matrix;

import ch.obermuhlner.math.big.matrix.BigMatrix;
import ch.obermuhlner.math.big.matrix.ImmutableBigMatrix;
import ch.obermuhlner.math.big.matrix.MutableBigMatrix;

import java.util.ArrayList;
import java.util.List;

public class KalmanFilterExample {

    private static final int MAX_STEPS = 10; // 1000
    private static final double T = 1.0;

    private static int measDOF = 8;

    ImmutableBigMatrix priorX = ImmutableBigMatrix.matrix(9, 1, 0.5, -0.2, 0, 0, 0.2, -0.9, 0, 0.2, -0.5);
    ImmutableBigMatrix priorP = ImmutableBigMatrix.identityMatrix(9);

    ImmutableBigMatrix trueX = ImmutableBigMatrix.matrix(9, 1, 0, 0, 0, 0.2, 0.2, 0.2, 0.5, 0.1, 0.6);

    List<BigMatrix> meas = createSimulatedMeas(trueX);

    BigMatrix F = createF(T);
    BigMatrix Q = createQ(T, 0.1);
    BigMatrix H = createH();

    public void evaluate(KalmanFilter f) {
        f.configure(F, Q, H);

        f.setState(priorX, priorP);
        processMeas(f, meas);

        System.out.println("State: " + f.getState());
        System.out.println("Covariance: " + f.getCovariance());
    }

    private List<BigMatrix> createSimulatedMeas(BigMatrix x) {
        List<BigMatrix> ret = new ArrayList<BigMatrix>();

        BigMatrix F = createF(T);
        BigMatrix H = createH();

        for (int i = 0; i < MAX_STEPS; i++) {
            BigMatrix x_next = F.multiply(x);
            BigMatrix z = H.multiply(x_next);
            ret.add(z);
            x = x_next;
        }

        return ret;
    }

    private void processMeas(KalmanFilter f,
                             List<BigMatrix> meas) {
        BigMatrix R = ImmutableBigMatrix.identityMatrix(measDOF);

        for (BigMatrix z : meas) {
            f.predict();
            f.update(z, R);
        }
    }


    public static BigMatrix createF(double T) {
        double[] a = new double[]{
                1, 0, 0, T, 0, 0, 0.5 * T * T, 0, 0,
                0, 1, 0, 0, T, 0, 0, 0.5 * T * T, 0,
                0, 0, 1, 0, 0, T, 0, 0, 0.5 * T * T,
                0, 0, 0, 1, 0, 0, T, 0, 0,
                0, 0, 0, 0, 1, 0, 0, T, 0,
                0, 0, 0, 0, 0, 1, 0, 0, T,
                0, 0, 0, 0, 0, 0, 1, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 1};

        return ImmutableBigMatrix.matrix(9, 9, a);
    }

    public static BigMatrix createQ(double T, double var) {
        MutableBigMatrix Q = MutableBigMatrix.matrix(9, 9);

        double a00 = (1.0 / 4.0) * T * T * T * T * var;
        double a01 = (1.0 / 2.0) * T * T * T * var;
        double a02 = (1.0 / 2.0) * T * T * var;
        double a11 = T * T * var;
        double a12 = T * var;
        double a22 = var;

        for (int i = 0; i < 3; i++) {
            Q.set(i, i, a00);
            Q.set(i, 3 + i, a01);
            Q.set(i, 6 + i, a02);
            Q.set(3 + i, 3 + i, a11);
            Q.set(3 + i, 6 + i, a12);
            Q.set(6 + i, 6 + i, a22);
        }

        for (int y = 1; y < 9; y++) {
            for (int x = 0; x < y; x++) {
                Q.set(y, x, Q.get(x, y));
            }
        }

        return Q;
    }

    public static BigMatrix createH() {
        MutableBigMatrix H = MutableBigMatrix.matrix(measDOF, 9);
        for (int i = 0; i < measDOF; i++) {
            H.set(i, i, 1.0);
        }

        return H;
    }

    public static void main(String[] args) {
        KalmanFilterExample kalmanFilterExample = new KalmanFilterExample();
        kalmanFilterExample.evaluate(new SimpleKalmanFilter());
    }
}