package net.fwitz.math.main.binary.escape.root.laguerre

import net.fwitz.math.main.binary.complex.functions.examples.simple.Z4p2Z3p3Z2p4Zp1

object LaguerresMethodZ4p2Z3p3Z2p4Zp1 {
    @JvmStatic
    fun main(args: Array<String>) = LaguerresMethodPlot.render(
        name = "z^4 + 2z^3 + 3z^2 + 4z + 1  (Laguerre's Method)",
        n = Z4p2Z3p3Z2p4Zp1.n,
        f = Z4p2Z3p3Z2p4Zp1::f,
        df = Z4p2Z3p3Z2p4Zp1::df,
        d2f = Z4p2Z3p3Z2p4Zp1::d2f
    )
}