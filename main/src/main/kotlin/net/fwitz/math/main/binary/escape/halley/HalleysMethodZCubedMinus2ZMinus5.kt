package net.fwitz.math.main.binary.escape.halley

object HalleysMethodZCubedMinus2ZMinus5 {
    @JvmStatic
    fun main(args: Array<String>) = HalleysMethodPlot.render(
        name = "z^3 -2z - 5  (Halley's Method)",
        f = { z -> z.pow3 - z * 2 - 5 },
        df = { z -> z.pow3 * 3 - 2 },
        d2f = { z -> z * 6 }
    )
}