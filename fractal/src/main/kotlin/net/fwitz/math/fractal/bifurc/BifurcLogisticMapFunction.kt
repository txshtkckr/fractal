package net.fwitz.math.fractal.bifurc

import java.util.*
import java.util.stream.IntStream
import kotlin.math.floor

object BifurcLogisticMapFunction {
    private const val WARM_UP = 900
    private const val SAMPLES = 100

    fun evaluate(r: Double): DoubleArray? {
        if (r < 0 || r > 4) return null

        var x = 0.1
        for (i in 1..WARM_UP) {
            x *= r * (1 - x)
        }
        val set: MutableSet<Double> = HashSet(SAMPLES)
        for (i in 1..SAMPLES) {
            x *= r * (1 - x)
            set.add(floor(1e6 * x) / 1e6)
        }

        return set.stream()
            .mapToDouble { obj: Double -> obj }
            .toArray()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        IntStream.range(0, 410)
            .mapToDouble { r100: Int -> r100.toDouble() / 100 }
            .forEach { r: Double ->
                val values = evaluate(r)
                if (values == null) {
                    System.out.format("%3.2f: (out-of-bounds)", r)
                } else {
                    System.out.format("%3.2f: %4d: ", r, values.size)
                    Arrays.stream(values)
                        .limit(10)
                        .forEach { x: Double -> System.out.format("  %8.6f", x) }
                }
                println()
            }
    }
}