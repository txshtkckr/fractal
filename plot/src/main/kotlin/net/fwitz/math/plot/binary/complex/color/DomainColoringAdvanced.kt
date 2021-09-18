package net.fwitz.math.plot.binary.complex.color

import net.fwitz.math.binary.complex.Complex
import java.awt.Color
import kotlin.math.abs
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Advanced domain coloring algorithm based on a Mathematica example discussed on StackOverflow..
 * <blockquote>
 * <pre>
 * complexHSB = Compile[{{Z, _Complex, 2}}, Block[{h, s, b, b2},
 * h = Arg[Z]/(2 Pi);
 * s = Abs[Sin[2 Pi Abs[Z]]];
 * b = Sqrt[Sqrt[Abs[Sin[2 Pi Im[Z]] Sin[2 Pi Re[Z]]]]];
 * b2 = 0.5 ((1 - s) + b + Sqrt[(1 - s - b)^2 + 0.01]);
 * Transpose[{h, Sqrt[s], b2}, {3, 1, 2}]]];
</pre> *
 *
 *
 * <cite> [Simon Woods](http://mathematica.stackexchange.com/users/862/simon-woods) in
 * [this StackOverflow answer](http://mathematica.stackexchange.com/a/7359)</cite>
 *
</blockquote> *
 */
open class DomainColoringAdvanced : ComplexColorFunction {
    companion object {
        private const val TWO_PI = Math.PI * 2.0
        private fun hsb(h: Double, s: Double, b: Double): Color {
            return Color.getHSBColor(f(h), f(s), f(b))
        }

        private fun f(x: Double): Float {
            val f = x.toFloat()
            return if (f < 0.0f) 0.0f else if (f > 1.0f) 1.0f else f
        }
    }

    override fun invoke(c: Complex, z: Complex) = complexHSB(z)

    protected fun complexHSB(z: Complex): Color {
        var h: Double = z.arg / TWO_PI
        if (h < 0) {
            h += 1.0
        }
        val s = abs(sin(TWO_PI * z.abs))
        val b = sqrt(sqrt(abs(sin(TWO_PI * z.y) * sin(TWO_PI * z.x))))
        val oneMsMb = 1.0 - s - b
        val b2 = 0.5 * (1 - s + b + sqrt(oneMsMb * oneMsMb + 0.01))
        return hsb(h, sqrt(s), filterBrightness(b2, z))
    }

    protected open fun filterBrightness(b: Double, z: Complex) = b
}