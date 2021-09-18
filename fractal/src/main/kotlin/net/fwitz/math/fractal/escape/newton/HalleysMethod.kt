package net.fwitz.math.fractal.escape.newton

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.calculus.Derivation.derivative
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.fractal.escape.EscapeTimeResult

class HalleysMethod<T : BinaryNumber<T>> private constructor(
    val f: (T) -> T,
    val df: (T) -> T,
    val d2f: (T) -> T,
    val maxIters: Int
) : EscapeFunction<T> {
    companion object {
        private const val DEFAULT_MAX_ITERS = 100
        private const val EPSILON = 1e-14
        private const val TOLERANCE = 1e-7

        @JvmStatic
        fun main(args: Array<String>) {
            debug(2.0, 0.0);
            debug(1.0, 0.0);
            debug(2.0, 0.0);
            debug(-1.0, 0.9);
            debug(-1.0, 0.01);
            debug(-1.0, 0.0);
            debug(-27.0, 0.00000001);
            debug(Double.NaN, 0.0);
            debug(0.0, Double.POSITIVE_INFINITY);
        }

        private fun debug(re: Double, im: Double) {
            val ef = halleysMethod<Complex>(
                f = { z -> z.pow3 - 1.0 },
                df = { z -> z.pow2 * 3.0 },
                d2f = { z -> z * 6.0 }
            )
            val c = Complex(re, im);
            val result = ef(c);
            println("c=$c => $result");
        }

        fun <T : BinaryNumber<T>> halleysMethod(
            f: (T) -> T,
        ) = derivative(f).let { df -> halleysMethod(f, df, derivative(df), DEFAULT_MAX_ITERS) }

        fun <T : BinaryNumber<T>> halleysMethod(
            f: (T) -> T,
            df: (T) -> T = derivative(f),
            d2f: (T) -> T = derivative(df),
            maxIters: Int = DEFAULT_MAX_ITERS
        ): EscapeFunction<T> = HalleysMethod(f, df, d2f, maxIters)
    }

    init {
        require(maxIters >= 1) { "maxIters < 1: $maxIters" }
    }

    override fun invoke(c: T): EscapeTimeResult<T> {
        var z = c
        var i = 1
        while (i < maxIters) {
            if (!z.x.isFinite() || !z.y.isFinite()) {
                return EscapeTimeResult.contained(z, null)
            }

            val fz = f(z)
            val dfz = df(z)
            val d2fz = d2f(z)
            val denom: T = dfz.pow2 * 2.0 - (fz * d2fz)
            if (denom.abs < EPSILON) {
                // Assume non-convergence rather than divide by such a tiny value
                return EscapeTimeResult.contained(z, null)
            }

            val numer: T = fz * 2.0 * dfz
            val zPrev = z
            z -= (numer / denom)
            if ((z - zPrev).abs <= TOLERANCE * z.abs) {
                return EscapeTimeResult.escaped(i, z, null)
            }
            ++i
        }

        return EscapeTimeResult.contained(z, null)
    }
}