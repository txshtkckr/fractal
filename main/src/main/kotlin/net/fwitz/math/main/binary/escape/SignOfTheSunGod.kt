package net.fwitz.math.main.binary.escape

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.EscapeTimePaletteFunction
import net.fwitz.math.plot.renderer.palette.PaletteVGA256

// https://www.flickr.com/photos/fractal_ken/3476756762
object SignOfTheSunGod {
    private const val P_MIN = -2.0
    private const val P_MAX = 1.0
    private const val Q_MIN = -1.5
    private const val Q_MAX = 1.5
    private const val ITERS = 50

    @JvmStatic
    fun main(args: Array<String>) = EscapeTimePlot
        .complex("z(n) = 1 / (z(n-1) + z(0))^2 (Escape time)")
        .computeFn(
            EscapeFunction.builder<Complex>()
                .init { Complex.ZERO }
                .step { c, z -> (z + c).pow(-2.0) }
                .escapeTest { z -> z.abs > 10.0 }
                .maxIters(ITERS)
                .build()
        )
        .domainX(P_MIN, P_MAX)
        .domainY(Q_MIN, Q_MAX)
        .colorFn(EscapeTimePaletteFunction.escapeTime(PaletteVGA256.WithoutLast8))
        .render()
}