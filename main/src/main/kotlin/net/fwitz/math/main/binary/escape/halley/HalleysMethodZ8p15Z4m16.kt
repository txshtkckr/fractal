package net.fwitz.math.main.binary.escape.halley

import net.fwitz.math.main.binary.complex.functions.examples.simple.Z8p15Z4m16
import net.fwitz.math.main.binary.escape.newton.NewtonsMethodZ8p15Z4m16

object HalleysMethodZ8p15Z4m16 : NewtonsMethodZ8p15Z4m16() {
    @JvmStatic
    fun main(args: Array<String>) = HalleysMethodPlot.render(
        name = "z^8 + 15z^4 - 16  (Halley's Method)",
        f = Z8p15Z4m16::f,
        df = Z8p15Z4m16::df,
        d2f = Z8p15Z4m16::d2f
    )
}