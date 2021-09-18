package net.fwitz.math.plot.binary.complex.color

import net.fwitz.math.binary.complex.Complex
import java.awt.Color
import kotlin.math.abs

object QuadrantComplexColor : ComplexColorFunction {
    override fun invoke(c: Complex, z: Complex): Color = when {
        abs(z.x) == 0.0 || abs(z.y) == 0.0 -> Color.BLACK
        z.y > 0.0 -> if (z.x > 0.0) Color.BLUE else Color.GREEN
        z.x > 0.0 -> Color.RED
        else -> Color.GRAY
    }
}