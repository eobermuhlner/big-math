package ch.obermuhlner.math.big.example;

public class StopWatch {
	private long startMillis;

	public StopWatch() {
		start();
	}

	public void start() {
		startMillis = System.currentTimeMillis();
	}
	
	public long getElapsedMillis() {
		long endMillis = System.currentTimeMillis();
		return endMillis - startMillis;
	}
	
	@Override
	public String toString() {
		return getElapsedMillis() + " ms";
	}
	
}
