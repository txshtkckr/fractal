package net.fwitz.math.main.binary.escape.root.halley

import net.fwitz.math.main.binary.complex.functions.examples.simple.Z8p3Z4m4

object HalleysMethodZ8p3Z4m4 {
    @JvmStatic
    fun main(args: Array<String>) = HalleysMethodPlot.render(
        name = "z^8 + 3z^4 - 4  (Newton's Method)",
        f = Z8p3Z4m4::f,
        df = Z8p3Z4m4::df,
        d2f = Z8p3Z4m4::d2f
    )
}