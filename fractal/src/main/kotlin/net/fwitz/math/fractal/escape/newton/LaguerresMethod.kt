package net.fwitz.math.fractal.escape.newton

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.calculus.Derivation
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.fractal.escape.EscapeTimeResult
import net.fwitz.math.fractal.escape.EscapeTimeResult.Companion.contained

class LaguerresMethod<T : BinaryNumber<T>> private constructor(
    val n: Double,
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
            val ef = laguerresMethod<Complex>(
                n = 3.0,
                f = { z -> z.pow3 - 1.0 },
                df = { z -> z.pow2 * 3.0 },
                d2f = { z -> z * 6.0 }
            )
            val c = Complex(re, im);
            val result = ef(c);
            println("c=$c => $result");
        }

        fun <T : BinaryNumber<T>> laguerresMethod(
            n: Double,
            f: (T) -> T,
        ) = Derivation.derivative(f).let { df -> laguerresMethod(n, f, df, Derivation.derivative(df), DEFAULT_MAX_ITERS) }

        fun <T : BinaryNumber<T>> laguerresMethod(
            n: Double,
            f: (T) -> T,
            df: (T) -> T = Derivation.derivative(f),
            d2f: (T) -> T = Derivation.derivative(df),
            maxIters: Int = DEFAULT_MAX_ITERS
        ): EscapeFunction<T> = LaguerresMethod(n, f, df, d2f, maxIters)
    }

    init {
        require(maxIters >= 1) { "maxIters < 1: $maxIters" }
    }

    override fun invoke(c: T): EscapeTimeResult<T> {
        var z = c
        var i = 1
        while (i < maxIters) {
            if (!z.x.isFinite() || !z.y.isFinite()) {
                return contained(z, null)
            }

            val fz = f(z)
            val dfz = df(z)
            val d2fz = d2f(z)
            val g = dfz / fz
            val g2 = g.pow2
            val h = g2 - d2fz / fz
            val rad = ((h * n - g2) * (n - 1)).sqrt
            val d1 = g + rad
            val d2 = g - rad
            val d1abs = d1.abs
            val d2abs = d2.abs
            val denom = when {
                d1abs >= d2abs -> {
                    if (d1abs < EPSILON) return contained(z, null)
                    d1
                }
                d2abs < EPSILON -> return contained(z, null)
                else -> d2
            }
            val a = denom.inverse * n
            val zPrev = z
            z -= a
            if ((z - zPrev).abs <= TOLERANCE * z.abs) {
                return EscapeTimeResult.escaped(i, z, null)
            }
            ++i
        }

        return contained(z, null)
    }
}