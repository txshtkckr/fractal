package net.fwitz.math.main.binary.escape.root.halley

import net.fwitz.math.binary.complex.Complex

object HalleysMethodSin {
    @JvmStatic
    fun main(args: Array<String>) = HalleysMethodPlot.render(
        name = "sin  (Halley's Method)",
        f = Complex::sin,
        df = Complex::cos,
        d2f = { z -> z.sin.negative }
    )
}