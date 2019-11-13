package ch.obermuhlner.math.big.example.matrix;

import ch.obermuhlner.math.big.matrix.BigMatrix;

public interface KalmanFilter {
    void configure(BigMatrix F, BigMatrix Q, BigMatrix H);

    void setState(BigMatrix x, BigMatrix P);

    void update(BigMatrix z, BigMatrix R);

    void predict();

    BigMatrix getState();

    BigMatrix getCovariance();
}
