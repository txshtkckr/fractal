package net.fwitz.math.plot.renderer

import java.util.concurrent.LinkedBlockingQueue
import java.lang.Runnable
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.BlockingQueue
import java.util.concurrent.RejectedExecutionException
import kotlin.jvm.Synchronized
import java.lang.InterruptedException

class RenderingPipeline private constructor() {
    companion object {
        private const val MIN_THREADS = 4
        private const val MAX_THREADS = 16
        private const val KEEP_ALIVE = 5L
        private const val CAPACITY = 10000
        private val PIPELINE_COUNTER = AtomicInteger()

        fun create(): RenderingPipeline {
            return RenderingPipeline()
        }
    }

    private val threadCounter = AtomicInteger()
    private val pendingLock = ReentrantLock()
    private val idle = pendingLock.newCondition()
    private val pipelineId = PIPELINE_COUNTER.incrementAndGet()
    private var pending = 0
    private val queue: BlockingQueue<Runnable> = LinkedBlockingQueue(CAPACITY)
    private val threadFactory = ThreadFactory { r: Runnable? ->
        val thd = Thread(r, "RenderingPipeline-" + pipelineId + '-' + threadCounter.incrementAndGet())
        thd.isDaemon = true
        thd
    }
    private val executor: ThreadPoolExecutor = ThreadPoolExecutor(
        MIN_THREADS, MAX_THREADS, KEEP_ALIVE,
        TimeUnit.SECONDS, queue, threadFactory
    )

    fun execute(runnable: Runnable) {
        try {
            taskSubmitted()
            executor.execute {
                try {
                    runnable.run()
                } finally {
                    taskFinished()
                }
            }
        } catch (e: RejectedExecutionException) {
            taskFinished()
        }
    }

    @Synchronized
    private fun taskSubmitted() {
        pendingLock.lock()
        try {
            ++pending
        } finally {
            pendingLock.unlock()
        }
    }

    @Synchronized
    private fun taskFinished() {
        pendingLock.lock()
        try {
            if (--pending <= 0) {
                pending = 0 // paranoid
                idle.signalAll()
            }
        } finally {
            pendingLock.unlock()
        }
    }

    private fun awaitIdle() {
        var interrupted = Thread.interrupted()
        pendingLock.lock()
        try {
            while (pending > 0) {
                try {
                    idle.await()
                } catch (e: InterruptedException) {
                    interrupted = true
                }
            }
        } finally {
            pendingLock.unlock()
        }
        if (interrupted) {
            Thread.currentThread().interrupt()
        }
    }

    fun shutdown() {
        executor.shutdown()
        try {
            if (executor.awaitTermination(1, TimeUnit.SECONDS)) {
                return
            }
        } catch (e: InterruptedException) {
            // ignore
        }
        executor.shutdownNow()
    }

    val isWorking: Boolean
        get() {
            pendingLock.lock()
            return try {
                pending > 0 && !executor.isShutdown
            } finally {
                pendingLock.unlock()
            }
        }

    val isShutdown: Boolean get() = executor.isShutdown

    fun flush() {
        pendingLock.lock()
        pending = try {
            queue.clear()
            awaitIdle()
            0
        } finally {
            pendingLock.unlock()
        }
    }
}