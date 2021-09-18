package net.fwitz.math.plot.binary.dual.color

import net.fwitz.math.binary.dual.DualNumber
import java.awt.Color
import kotlin.math.*

object DomainColoringDual : DualNumberColorFunction {
    private const val TWO_PI = Math.PI * 2.0

    private fun complexHSB(z: DualNumber): Color = when {
        z.isNaN -> Color.DARK_GRAY
        z.isInfinite -> Color.WHITE
        z.x == 0.0 -> if (z.y == 0.0) Color.BLACK else Color.LIGHT_GRAY
        else -> {
            val h = hue(z)
            val s = abs(sin(TWO_PI * z.abs))
            val b = sqrt(sqrt(abs(sin(TWO_PI * z.y) * sin(TWO_PI * z.x))))
            val oneMsMb = 1.0 - s - b
            val b2 = 0.5 * (1 - s + b + sqrt(oneMsMb * oneMsMb + 0.01))
            hsb(h, sqrt(s), b2)
        }
    }

    // Hrmmm... how to smash [-Inf, 0, +Inf] into [0.0, 0.5, 1.0]...
    private fun hue(z: DualNumber): Double {
        var hue = 0.5 / (1 + exp(-z.arg))
        hue = if (z.x < 0) hue + 0.25 else hue + 0.75
        return hue - floor(hue)
    }

    private fun hsb(h: Double, s: Double, b: Double) = Color.getHSBColor(f(h), f(s), f(b))

    private fun f(x: Double): Float {
        val f = x.toFloat()
        return when {
            f < 0.0f -> 0.0f
            f > 1.0f -> 1.0f
            else -> f
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        debug(5.0, 0.01)
        debug(4.0, 3.0)
        debug(3.0, 4.0)
        debug(0.0, 5.0)
        debug(-3.0, 4.0)
        debug(-4.0, 3.0)
        debug(-5.0, 0.0)
        debug(-4.0, -3.0)
        debug(-3.0, -4.0)
        debug(0.01, -5.0)
        debug(3.0, -4.0)
        debug(4.0, -3.0)
    }

    private fun debug(x: Double, y: Double) {
        val z = DualNumber(x, y)
        println(
            "z=(%10.7f, %10.7f)  abs=%10.7f  arg=%10.7f  hue=%10.7f  color=%s\n".format(
                x, y, z.abs, z.arg, hue(z), complexHSB(z)
            )
        )
    }

    override fun invoke(c: DualNumber, z: DualNumber) = complexHSB(z)
}