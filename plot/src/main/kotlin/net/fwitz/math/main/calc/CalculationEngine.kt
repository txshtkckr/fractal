package net.fwitz.math.main.calc

import java.util.concurrent.LinkedBlockingQueue
import java.lang.Runnable
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger
import net.fwitz.math.plot.renderer.RenderingPipeline
import java.util.function.Supplier
import java.util.concurrent.CompletionStage
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit
import java.time.Duration

class CalculationEngine private constructor() {
    companion object {
        private val THREAD_POOL_MIN = Runtime.getRuntime().availableProcessors()
        private val THREAD_POOL_MAX = THREAD_POOL_MIN * 4
        private val KEEP_ALIVE = Duration.ofMinutes(1)
        private const val QUEUE_SIZE = 10000
        private val INSTANCE = CalculationEngine()
        fun instance(): CalculationEngine {
            return INSTANCE
        }

        fun shutdown() {
            INSTANCE.executor.shutdown()
        }

        fun flush() {
            INSTANCE.queue.clear()
        }

        fun <T> calculate(pipeline: RenderingPipeline, calculation: Supplier<T>): CompletionStage<T> {
            val future = CompletableFuture<T>()
            INSTANCE.executor.execute {
                if (pipeline.isShutdown) {
                    val value = calculation.get()
                    future.complete(value)
                } else {
                    future.cancel(true)
                }
            }
            return future
        }
    }

    private val queue = LinkedBlockingQueue<Runnable>(QUEUE_SIZE)
    private val executor: ThreadPoolExecutor = ThreadPoolExecutor(
        THREAD_POOL_MIN,
        THREAD_POOL_MAX,
        KEEP_ALIVE.toMillis(),
        TimeUnit.MILLISECONDS,
        queue,
        CalculationThreadFactory()
    )

    private class CalculationThreadFactory : ThreadFactory {
        private val counter = AtomicInteger()
        override fun newThread(r: Runnable): Thread {
            val name = "CalculationEngine-" + counter.incrementAndGet()
            val thd = Thread(r, name)
            thd.isDaemon = true
            return thd
        }
    }
}