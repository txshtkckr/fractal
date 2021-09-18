package net.fwitz.math.calculus

import net.fwitz.math.binary.BinaryNumber

object Derivation {
    private const val EPSILON = 1e-7
    private const val ONE_OVER_TWO_EPSILON = 0.5 / EPSILON

    inline fun <reified T: BinaryNumber<T>> derivative(h: Double, zv: Array<T>): Array<T> {
        if (zv.size < 3) return zv

        val twoH = h * 2
        val last = zv.lastIndex
        return Array(zv.size) { i ->
            when {
                i == 0 -> (zv[1] - zv[0]) / h
                i == last -> (zv[last] - zv[last - 1]) / h
                else -> (zv[i + 1] - zv[i - 1]) / twoH
            }
        }
    }

    // Poor man's approximate derivative by taking the slope over a small segment.
    @JvmName("derivativeDoubleFn")
    fun derivative(f: (Double) -> Double): (Double) -> Double {
        return { x ->
            val fxph = f(x + EPSILON)
            val fxmh = f(x - EPSILON)
            (fxph - fxmh) * ONE_OVER_TWO_EPSILON
       }
    }

    // Poor man's approximate derivative by taking the slope over a small segment.
    // Provided the derivative exists, it does not matter which direction we take it in,
    // so we'll just vary the real component for simplicity.
    @JvmName("derivativeBinaryNumberFn")
    fun <T : BinaryNumber<T>> derivative(f: (T) -> T): (T) -> T {
        return { z ->
            val fzph = f(z + EPSILON)
            val fzmh = f(z - EPSILON)
            (fzph - fzmh) * ONE_OVER_TWO_EPSILON
        }
    }
}