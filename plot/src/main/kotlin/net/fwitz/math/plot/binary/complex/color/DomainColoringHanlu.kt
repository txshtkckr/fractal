package net.fwitz.math.plot.binary.complex.color

import net.fwitz.math.binary.complex.Complex
import java.awt.Color
import kotlin.math.floor
import kotlin.math.ln

/**
 * See http://users.mai.liu.se/hanlu09/complex/domain-coloring.scm
 */
object DomainColoringHanlu : ComplexColorFunction {
    private const val TWO_PI = Math.PI * 2
    private const val DELTA = 0.05
    private val LN_2 = ln(2.0)

    private fun frac(x: Double) = x - floor(x)

    private fun closeToInt(x: Double): Boolean {
        var frac = frac(x)
        if (frac > 0.5) {
            frac = 1.0 - frac
        }
        return frac <= DELTA
    }

    private fun isOnGrid(z: Complex) = closeToInt(z.x) || closeToInt(z.y)

    private fun fnNoGrid(phase: Double, logAbs: Double): Color {
        val logGradient = frac(logAbs)
        if (phase < 0.5) {
            val redMax = phase * 2
            val redMin = redMax * redMax
            return Color(value(redMin, redMax, logGradient), 0, 0)
        }
        val grnMax = phase * 2 - 1
        val grnMin = grnMax * grnMax
        return Color(255, value(grnMin, grnMax, logGradient), 0)
    }

    private fun value(min: Double, max: Double, logGradient: Double): Int {
        val value = min + (max - min) * logGradient
        val x = (value * 256).toInt()
        return if (x < 0) 0 else if (x > 255) 255 else x
    }

    override fun invoke(c: Complex, z: Complex): Color {
        val phase = frac(z.arg / TWO_PI)
        val logAbs = ln(z.abs / LN_2)
        val clr: Color = fnNoGrid(phase, logAbs)
        return when {
            isOnGrid(z) -> Color(clr.getRed() / 2, clr.getGreen() / 2, clr.getBlue() / 2)
            else -> clr
        }
    }
}