package net.fwitz.math.main.binary.escape.newton

object NewtonsMethodZCubedMinus2ZMinus5 {
    @JvmStatic
    fun main(args: Array<String>) = NewtonsMethodPlot.render(
        name = "z^3 -2z - 5  (Newton's Method)",
        f = { z -> z.pow3 - z * 2 - 5 },
        df = { z -> z.pow2 * 3 - 2 }
    )
}