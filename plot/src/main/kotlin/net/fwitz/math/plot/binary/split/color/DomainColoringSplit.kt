package net.fwitz.math.plot.binary.split.color

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.binary.split.SplitComplex.Classification.*
import net.fwitz.math.binary.split.SplitComplex.Companion.splitComplex
import java.awt.Color
import kotlin.math.*

object DomainColoringSplit : SplitComplexColorFunction {
    private const val TWO_PI = Math.PI * 2.0

    private fun complexHSB(z: SplitComplex): Color {
        var invert = false
        val hueOffset: Double
        val classification = z.classify()
        when (classification) {
            NAN -> return Color.DARK_GRAY
            ZERO -> return Color.BLACK
            POS_NULL_VECTOR, NEG_NULL_VECTOR -> return Color.LIGHT_GRAY
            REGION_I -> hueOffset = 0.875
            REGION_II -> {
                hueOffset = 0.125
                invert = true
            }
            REGION_III -> hueOffset = 0.375
            REGION_IV -> {
                hueOffset = 0.625
                invert = true
            }
            else -> throw IllegalStateException("Unexpected Unit type: $classification")
        }
        if (z.isInfinite) return Color.WHITE

        val h = hue(z, invert) + hueOffset
        val s = abs(sin(TWO_PI * z.abs))
        val b = sqrt(sqrt(abs(sin(TWO_PI * z.y) * sin(TWO_PI * z.x))))
        val oneMsMb = 1.0 - s - b
        val b2 = 0.5 * (1 - s + b + sqrt(oneMsMb * oneMsMb + 0.01))
        return hsb(h, sqrt(s), b2)
    }

    // Hrmmm... how to smash  [-Inf, 0, +Inf] into [0.0, 0.5, 1.0]...
    private fun hue(z: SplitComplex, invert: Boolean): Double {
        val hue = 0.25 / (1 + exp(-z.arg))
        return if (invert) 0.25 - hue else hue
    }

    private fun hsb(h: Double, s: Double, b: Double): Color {
        return Color.getHSBColor(f(h), f(s), f(b))
    }

    private fun f(x: Double): Float {
        val f = x.toFloat()
        return if (f < 0.0f) 0.0f else min(f, 1.0f)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        debug(5.0, -5.0)
        debug(5.0, -4.999)
        debug(5.0, -4.9)
        debug(5.0, -1.0)
        debug(5.0, 0.0)
        debug(5.0, 1.0)
        debug(5.0, 4.9)
        debug(5.0, 4.999)
        debug(5.0, 5.0)
    }

    private fun debug(x: Double, y: Double) {
        val z = splitComplex(x, y)
        System.out.printf(
            "z=(%10.7f, %10.7f)  abs=%10.7f  arg=%10.7f  color=%s\n",
            x, y, z.abs, z.arg, complexHSB(z)
        )
    }

    override fun invoke(c: SplitComplex, z: SplitComplex) = complexHSB(z)
}