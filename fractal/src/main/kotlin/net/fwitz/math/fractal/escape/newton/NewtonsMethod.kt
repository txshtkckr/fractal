package net.fwitz.math.fractal.escape.newton

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.calculus.Derivation.derivative
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.fractal.escape.EscapeTimeResult
import kotlin.math.ln

object NewtonsMethod {
    private const val DEFAULT_MAX_ITERS = 100
    private const val EPSILON = 1e-14
    private const val TOLERANCE = 1e-3
    private val LOG_TOLERANCE = ln(TOLERANCE)

    fun <T : BinaryNumber<T>> newtonsMethod(f: (T) -> T) = newtonsMethod(f, derivative(f))

    fun <T : BinaryNumber<T>> newtonsMethod(
        f: (T) -> T,
        df: (T) -> T = derivative(f),
        maxIters: Int = DEFAULT_MAX_ITERS
    ): EscapeFunction<T> = Impl(f, df, maxIters)

    private class Impl<T : BinaryNumber<T>>(
        val f: (T) -> T,
        val df: (T) -> T,
        val maxIters: Int
    ) : EscapeFunction<T> {
        init {
            require(maxIters >= 1) { "maxIters < 1: $maxIters" }
        }

        override fun invoke(c: T): EscapeTimeResult<T> {
            var z = c
            var logDistPrev = Double.POSITIVE_INFINITY
            var i = 1
            while (i < maxIters) {
                if (!z.x.isFinite() || !z.y.isFinite()) return EscapeTimeResult.contained(z, null)  //TODO ???

                val dfz = df(z)
                if (dfz.abs < EPSILON) {
                    // Assume non-convergence rather than divide by such a tiny value
                    return EscapeTimeResult.contained(z, null)
                }

                val fz = f(z)
                val zPrev = z
                z -= fz / dfz
                val delta = z - zPrev
                val logDist = delta.logabs
                if (delta.abs <= TOLERANCE) {
                    val numer = TOLERANCE - logDistPrev
                    val denom = logDist - logDistPrev
                    return EscapeTimeResult.escaped(i, z, numer / denom)
                }
                logDistPrev = logDist
                ++i
            }
            return EscapeTimeResult.contained(z, null)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        debug(2.0, 0.0)
        debug(1.0, 0.0)
        debug(2.0, 0.0)
        debug(-1.0, 0.9)
        debug(-1.0, 0.01)
        debug(-1.0, 0.0)
        debug(-27.0, 0.00000001)
        debug(Double.NaN, 0.0)
        debug(0.0, Double.POSITIVE_INFINITY)
    }

    private fun debug(re: Double, im: Double) {
        // Supply the actual derivative
        val ef1 = newtonsMethod<Complex>(
            f = { z -> z.pow3 - 1 },
            df = { z -> z.pow2 * 3 }
        )

        // Use a numerical approximation for the derivative
        val ef2 = newtonsMethod<Complex> { z -> z.pow3 - 1 }
        val c = Complex(re, im)
        println("c=$c [1] => " + ef1(c))
        println("c=$c [2] => " + ef2(c))
    }
}