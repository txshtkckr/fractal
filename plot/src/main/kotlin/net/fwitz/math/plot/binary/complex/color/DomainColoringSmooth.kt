package net.fwitz.math.plot.binary.complex.color

import net.fwitz.math.binary.complex.Complex
import java.awt.Color
import kotlin.math.floor
import kotlin.math.ln

object DomainColoringSmooth : ComplexColorFunction {
    private const val TWO_PI = Math.PI * 2.0

    private fun hue(arg: Double): Float {
        val hue = arg / TWO_PI
        return (hue - floor(hue)).toFloat()
    }

    private fun logScale(z: Complex): Double {
        return ln(z.abs)
    }

    private fun mantissa(x: Double): Double {
        return x - floor(x)
    }

    private fun brightness(hue: Float, k: Double): Float {
        var x = mantissa((6 * hue).toDouble())
        x *= 1 - x
        return when {
            x < 0.02 -> (0.2 + 0.7 * mantissa(k)).toFloat()
            else -> (0.3 + 0.7 * mantissa(k)).toFloat()
        }
    }

    override fun invoke(c: Complex, z: Complex): Color {
        val arg: Double = z.arg
        val k = logScale(z)
        val hue = hue(arg)
        val brt = brightness(hue, k)
        return Color.getHSBColor(hue, 1.0f, brt)
    }
}