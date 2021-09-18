package net.fwitz.math.plot.binary.complex.color

import net.fwitz.math.binary.complex.Complex
import java.awt.Color
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow

object ComplexColorWheel : ComplexColorFunction {
    private const val TWO_PI = Math.PI * 2.0

    private fun hue(z: Complex): Float {
        val hue: Double = z.arg / TWO_PI
        return (hue - floor(hue)).toFloat()
    }

    private fun logScaleMantissa(z: Complex): Double {
        val m = ln(z.abs)
        return m - floor(m)
    }

    private fun saturation(k: Double): Float {
        var sat = if (k < 0.5) k * 2 else 1 - (k - 0.5) * 2
        sat = 1 - (1 - sat).pow(3.0)
        sat = 0.4 + sat * 0.6
        return sat.toFloat()
    }

    private fun brightness(k: Double): Float {
        var brt = if (k < 0.5) k * 2 else 1 - (k - 0.5) * 2
        brt = 1 - brt
        brt = 1 - (1 - brt).pow(3.0)
        brt = 0.6 + brt * 0.4
        return brt.toFloat()
    }

    override fun invoke(c: Complex, z: Complex): Color {
        val k = logScaleMantissa(z)
        val hue = hue(z)
        val sat = saturation(k)
        val brt = brightness(k)
        return Color.getHSBColor(hue, sat, brt)
    }
}