package net.fwitz.math.main.binary.escape.root.newton

import net.fwitz.math.binary.complex.Complex

object NewtonsMethodTan {
    @JvmStatic
    fun main(args: Array<String>) = NewtonsMethodPlot.render(
        name = "tan(z) (Newton's Method)",
        f = Complex::tan,
        df = { z -> z.sec.pow2 }
    )
}