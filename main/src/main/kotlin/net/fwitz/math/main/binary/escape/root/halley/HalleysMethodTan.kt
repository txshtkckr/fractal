package net.fwitz.math.main.binary.escape.root.halley

import net.fwitz.math.binary.complex.Complex

object HalleysMethodTan {
    @JvmStatic
    fun main(args: Array<String>) = HalleysMethodPlot.render(
        name = "tan  (Halley's Method)",
        f = Complex::tan,
        df = { z -> z.sec.pow2 },
        d2f = { z -> z.tan * z.sec.pow2 * 2 }
    )
}