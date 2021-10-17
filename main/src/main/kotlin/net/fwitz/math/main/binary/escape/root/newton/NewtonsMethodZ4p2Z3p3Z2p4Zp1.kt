package net.fwitz.math.main.binary.escape.root.newton

import net.fwitz.math.main.binary.complex.functions.examples.simple.Z4p2Z3p3Z2p4Zp1

object NewtonsMethodZ4p2Z3p3Z2p4Zp1 {
    @JvmStatic
    fun main(args: Array<String>) = NewtonsMethodPlot.render(
        name = "z^4 + 2z^3 + 3z^2 + 4z + 1  (Newton's Method)",
        f = Z4p2Z3p3Z2p4Zp1::f,
        df = Z4p2Z3p3Z2p4Zp1::df
    )
}