package net.fwitz.math.plot;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RenderingPipeline {
    private static final int MIN_THREADS = 4;
    private static final int MAX_THREADS = 16;
    private static final int KEEP_ALIVE = 5;
    private static final int CAPACITY = 10000;

    private static final AtomicInteger PIPELINE_COUNTER = new AtomicInteger();

    private final AtomicInteger threadCounter = new AtomicInteger();
    private final ReentrantLock pendingLock = new ReentrantLock();
    private final Condition idle = pendingLock.newCondition();
    private final int pipelineId = PIPELINE_COUNTER.incrementAndGet();

    private int pending = 0;

    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(CAPACITY);
    private final ThreadFactory threadFactory = r -> {
        final Thread thd = new Thread(r, "RenderingPipeline-" + pipelineId + '-' + threadCounter.incrementAndGet());
        thd.setDaemon(true);
        return thd;
    };

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(MIN_THREADS, MAX_THREADS, KEEP_ALIVE,
            TimeUnit.SECONDS, queue, threadFactory);

    private RenderingPipeline() {
    }

    public static RenderingPipeline create() {
        return new RenderingPipeline();
    }

    public void execute(Runnable runnable) {
        try {
            taskSubmitted();
            executor.execute(() -> {
                try {
                    runnable.run();
                } finally {
                    taskFinished();
                }
            });
        } catch (RejectedExecutionException e) {
            taskFinished();
        }
    }

    synchronized private void taskSubmitted() {
        pendingLock.lock();
        try {
            ++pending;
        } finally {
            pendingLock.unlock();
        }
    }

    synchronized private void taskFinished() {
        pendingLock.lock();
        try {
            if (--pending <= 0) {
                pending = 0;  // paranoid
                idle.signalAll();
            }
        } finally {
            pendingLock.unlock();
        }
    }

    private void awaitIdle() {
        boolean interrupted = Thread.interrupted();
        pendingLock.lock();
        try {
            while (pending > 0) {
                try {
                    idle.await();
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            pendingLock.unlock();
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (executor.awaitTermination(1, TimeUnit.SECONDS)) {
                return;
            }
        } catch (InterruptedException e) {
            // ignore
        }
        executor.shutdownNow();
    }

    public boolean isWorking() {
        pendingLock.lock();
        try {
            return pending > 0 && !executor.isShutdown();
        } finally {
            pendingLock.unlock();
        }
    }

    public boolean isShutdown() {
        return executor.isShutdown();
    }
    
    public void flush() {
        pendingLock.lock();
        try {
            queue.clear();
            awaitIdle();
            pending = 0;
        } finally {
            pendingLock.unlock();
        }
    }
}
