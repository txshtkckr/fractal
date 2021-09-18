package net.fwitz.math.plot.binary.escape.color

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.fractal.escape.EscapeTimeResult
import java.awt.Color
import kotlin.math.floor

open class NewtonsMethodDarkZero<T : BinaryNumber<T>> : EscapeTimeColorFunction<T> {
    companion object {
        private const val ONE_OVER_TWO_PI = 0.5 / Math.PI
        private const val GRADES = 10
        private fun <T : BinaryNumber<T>> hue(z: T): Float {
            val hue: Double = z.arg * ONE_OVER_TWO_PI
            return (hue - floor(hue)).toFloat()
        }
    }

    override fun invoke(c: T, result: EscapeTimeResult<T>): Color {
        return if (result.contained) Color.BLACK else escaped(result)
    }

    private fun escaped(result: EscapeTimeResult<T>): Color {
        val hue = hue(result.z)
        return color(hue, result.iters)
    }

    private fun color(hue: Float, n: Int): Color {
        val value = value(n).toFloat()
        return Color.getHSBColor(hue, 1.0f, value)
    }

    private fun value(n: Int): Double {
        val relativeCloseness = (GRADES - n % GRADES).toDouble() / GRADES
        return value(relativeCloseness)
    }

    open fun value(closeness: Double): Double {
        return 1 - 0.9 * closeness
    }

    fun interpolated(power: Double, radius: Double): EscapeTimeColorFunction<T> {
        return EscapeTimeInterpolator(
            power,
            radius,
            Color.BLACK
        ) { result, n ->
            val hue = hue(result.z)
            color(hue, n)
        }
    }

}