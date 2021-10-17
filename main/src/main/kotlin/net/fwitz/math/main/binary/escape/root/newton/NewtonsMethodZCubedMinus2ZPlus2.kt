package net.fwitz.math.main.binary.escape.root.newton

object NewtonsMethodZCubedMinus2ZPlus2 {
    @JvmStatic
    fun main(args: Array<String>) = NewtonsMethodPlot.render(
        name = "z^3 - 2z + 2  (Newton's Method)",
        f = { z -> z.pow3 - z * 2 + 2 },
        df = { z -> z.pow2 * 3 - 2 }
    )
}