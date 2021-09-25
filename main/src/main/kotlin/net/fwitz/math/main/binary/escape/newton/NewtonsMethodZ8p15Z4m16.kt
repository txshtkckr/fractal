package net.fwitz.math.main.binary.escape.newton

import net.fwitz.math.main.binary.complex.functions.examples.simple.Z8p15Z4m16

object NewtonsMethodZ8p15Z4m16 {
    @JvmStatic
    fun main(args: Array<String>) = NewtonsMethodPlot.render(
        name = "z^8 + 15z^4 - 16  (Newton's Method)",
        f = Z8p15Z4m16::f,
        df = Z8p15Z4m16::df
    )
}