package net.fwitz.math.main.binary.escape.root.newton

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.main.binary.escape.root.newton.NewtonsMethodPlot.zPowMinus1

object NewtonsMethodZPower4Plus3i {
    @JvmStatic
    fun main(args: Array<String>) = zPowMinus1(Complex(4, 3))
}