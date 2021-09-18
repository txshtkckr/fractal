package net.fwitz.math.plot.renderer.palette

import java.awt.Color
import kotlin.math.roundToInt

object Interpolate {
    fun interpolate(c1: Color, c2: Color, x: Double): Color {
        return Color(
            interpolate(c1.red, c2.red, x),
            interpolate(c1.green, c2.green, x),
            interpolate(c1.blue, c2.blue, x)
        )
    }

    fun interpolate(c1: Double, c2: Double, x: Double): Double {
        return c1 + (c2 - c1) * x
    }

    fun interpolate(c1: Int, c2: Int, x: Double): Int {
        return c1 + ((c2 - c1) * x).roundToInt()
    }
}