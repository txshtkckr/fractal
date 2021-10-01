package net.fwitz.math.main.binary.escape.root.newton

import net.fwitz.math.binary.complex.Complex

object NewtonsMethodSin {
    @JvmStatic
    fun main(args: Array<String>) = NewtonsMethodPlot.render(
        name = "sin(z) (Newton's Method)",
        f = Complex::sin,
        df = Complex::cos
    )
}