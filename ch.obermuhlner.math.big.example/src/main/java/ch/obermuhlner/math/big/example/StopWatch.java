package ch.obermuhlner.math.big.example;

public class StopWatch {
	private long startNanos;

	public StopWatch() {
		start();
	}

	public void start() {
		//startMillis = System.currentTimeMillis();
		startNanos = System.nanoTime();
	}

	public double getElapsedSeconds() {
		return getElapsedNanos() / 1000_000.0;
	}

	public long getElapsedMillis() {
		return getElapsedNanos() / 1000;
	}

	public long getElapsedNanos() {
		long endNanos = System.nanoTime();
		return (endNanos - startNanos) / 1000;
	}

	@Override
	public String toString() {
		return getElapsedSeconds() + " s";
	}

}
