package net.fwitz.math.main.binary.escape.root.laguerre

import net.fwitz.math.main.binary.complex.functions.examples.simple.Z8p3Z4m4

object LaguerresMethodZ8p3Z4m4 {
    @JvmStatic
    fun main(args: Array<String>) = LaguerresMethodPlot.render(
        name = "z^8 + 3z^4 - 4  (Laguerre's Method)",
        n = Z8p3Z4m4.n,
        f = Z8p3Z4m4::f,
        df = Z8p3Z4m4::df,
        d2f = Z8p3Z4m4::d2f
    )
}