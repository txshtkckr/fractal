package net.fwitz.math.main.binary.escape.root.halley

object HalleysMethodZCubedMinus2ZPlus2 {
    @JvmStatic
    fun main(args: Array<String>) = HalleysMethodPlot.render(
        name = "z^3 - 2z + 2  (Halley's Method)",
        f = { z -> z.pow3 - z * 2 + 2 },
        df = { z -> z.pow2 * 3 - 2 },
        d2f = { z -> z * 6 }
    )
}