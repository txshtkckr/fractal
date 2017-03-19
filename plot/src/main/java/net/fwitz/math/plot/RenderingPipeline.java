package net.fwitz.math.plot;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RenderingPipeline {
    private static final int MIN_THREADS = 4;
    private static final int MAX_THREADS = 16;
    private static final int KEEP_ALIVE = 5;
    private static final int CAPACITY = 10000;

    private static final AtomicInteger COUNTER = new AtomicInteger();
    private static final RenderingPipeline INSTANCE = new RenderingPipeline();

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(CAPACITY);
    private final ThreadFactory threadFactory = r -> {
        final Thread thd = new Thread(r, "RenderingPipeline-" + COUNTER.incrementAndGet());
        thd.setDaemon(true);
        return thd;
    };
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(MIN_THREADS, MAX_THREADS, KEEP_ALIVE,
            TimeUnit.SECONDS, queue, threadFactory);

    private RenderingPipeline() {
    }

    public static void shutdown() {
        INSTANCE.executor.shutdown();
    }
    
    public static void execute(Runnable r) {
        INSTANCE.executor.execute(r);
    }
}
