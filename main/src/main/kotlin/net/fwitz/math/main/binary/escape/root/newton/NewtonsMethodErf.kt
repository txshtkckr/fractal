package net.fwitz.math.main.binary.escape.root.newton

import net.fwitz.math.binary.complex.functions.Erf

object NewtonsMethodErf {
    @JvmStatic
    fun main(args: Array<String>) = NewtonsMethodPlot.render(
        name = "erf(z) (Newton's Method)",
        f = Erf::erf
    )
}