package net.fwitz.math.plot.binary.complex.color

import net.fwitz.math.binary.complex.Complex
import java.awt.Color
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow

object DomainColoringContour : ComplexColorFunction {
    private const val TWO_PI = Math.PI * 2.0
    private val LN_2_RECIP = 1.0 / ln(2.0)

    private fun hue(z: Complex): Float {
        val hue: Double = z.arg / TWO_PI
        return (hue - floor(hue)).toFloat()
    }

    private fun logScaleMantissa(z: Complex): Double {
        val m: Double = z.logabs * LN_2_RECIP
        return m - floor(m)
    }

    private fun brightness(k: Double): Float {
        //return (float) (0.4 + 0.6 * k);
        return k.pow(0.2).toFloat()
    }

    override fun invoke(c: Complex, z: Complex): Color {
        val k = logScaleMantissa(z)
        val hue = hue(z)
        val brt = brightness(k)
        return Color.getHSBColor(hue, 1.0f, brt)
    }
}