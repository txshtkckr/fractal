package net.fwitz.math.main.binary.escape.barnsley

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.EscapeTimePaletteFunction
import net.fwitz.math.plot.renderer.palette.PaletteRandom

/**
 * "First Michael Barnsley Fractal"
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Some Barnsley Fractals" (pp. 151-153)
 */
object Barnsley1 {
    private const val P_MIN = 0.0
    private const val P_MAX = 1.0
    private const val Q_MIN = 0.0
    private const val Q_MAX = 1.0
    private const val ITERS = 1000

    private const val p = 0.6
    private const val q = 1.1

    private val c = Complex(p, q)

    private val BARNSLEY_1 = EscapeFunction.builder<Complex>()
        .containmentTest { it.abs2 < 4 }
        .step { _, z ->
            when {
                z.x >= 0 -> (z - 1) * c
                else -> (z + 1) * c
            }
        }
        .maxIters(ITERS)
        .build()

    @JvmStatic
    fun main(args: Array<String>) = EscapeTimePlot.complex("Barnsley 1 (Escape time)")
        .computeFn(BARNSLEY_1)
        .colorFn(EscapeTimePaletteFunction.escapeTime(PaletteRandom(1000)))
        .domainX(P_MIN, P_MAX)
        .domainY(Q_MIN, Q_MAX)
        .render()
}