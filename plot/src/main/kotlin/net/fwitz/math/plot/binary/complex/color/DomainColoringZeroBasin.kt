package net.fwitz.math.plot.binary.complex.color

import net.fwitz.math.binary.complex.Complex
import java.awt.Color
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow

object DomainColoringZeroBasin : ComplexColorFunction {
    private const val TWO_PI = Math.PI * 2.0
    private fun hue(z: Complex): Float {
        val hue: Double = z.arg / TWO_PI
        return (hue - floor(hue)).toFloat()
    }

    private fun mantissa(x: Double): Double {
        return x - floor(x)
    }

    private fun brightness(k: Double): Float {
        var brt = if (k < 0.5) k * 2 else 1 - (k - 0.5) * 2
        brt = 1 - brt
        brt = 1 - (1 - brt).pow(3.0)
        brt = 0.4 + brt * 0.6
        return brt.toFloat()
    }

    private fun saturation(k: Double): Float {
        var sat = if (k < 0.5) k * 2 else 1 - (k - 0.5) * 2
        sat = 1 - (1 - sat).pow(3.0)
        sat = 0.4 + sat * 0.6
        return sat.toFloat()
    }

    override fun invoke(c: Complex, z: Complex): Color {
        val logZ: Double = z.logabs
        val k = mantissa(logZ)
        val hue = hue(z)
        var sat = saturation(k)
        var brt = brightness(k)

        val absRe: Double = abs(z.x)
        if (absRe < 0.05) sat *= (absRe * absRe * 200 + 0.5).toFloat()

        val absIm: Double = abs(z.y)
        if (absIm < 0.05) brt *= (absIm * absIm * 200 + 0.5).toFloat()

        return Color.getHSBColor(hue, sat, brt)
    }
}