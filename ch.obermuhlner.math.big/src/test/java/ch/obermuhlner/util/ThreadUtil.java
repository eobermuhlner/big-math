package ch.obermuhlner.util;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadUtil {
    public static void runMultiThreaded(Runnable runnable) throws Throwable {
        runMultiThreaded(10, runnable);
    }

    public static void runMultiThreaded(int threadCount, Runnable runnable) throws Throwable {
        Callable<Void> callable = () -> {
            runnable.run();
            return null;
        };

        AtomicReference<Throwable> exception = new AtomicReference<>();
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int id = i;
            Thread thread = new Thread(() -> {
                System.out.println("STARTED " + id);
                try {
                    callable.call();
                } catch (Throwable e) {
                    System.out.println("EXCEPTION " + e);
                    exception.set(e);
                } finally {
                    System.out.println("FINISHED " + id);
                    countDownLatch.countDown();
                }
            });
            thread.start();
        }
        countDownLatch.await();

        if (exception.get() != null) {
            throw exception.get();
        }
    }
}
