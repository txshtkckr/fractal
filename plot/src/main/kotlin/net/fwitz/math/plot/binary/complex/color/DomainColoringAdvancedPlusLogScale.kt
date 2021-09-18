package net.fwitz.math.plot.binary.complex.color

import net.fwitz.math.binary.complex.Complex
import kotlin.math.floor
import kotlin.math.ln

object DomainColoringAdvancedPlusLogScale : DomainColoringAdvanced() {
    private val LN_2 = ln(2.0)
    private val SCALE = 0.25 / LN_2

    override fun filterBrightness(b: Double, z: Complex): Double {
        var x: Double = z.logabs * SCALE
        x = 0.3 + 0.7 * (x - floor(x))
        x *= b
        return if (x < 0.0) 0.0 else if (x > 1.0) 1.0 else x
    }
}