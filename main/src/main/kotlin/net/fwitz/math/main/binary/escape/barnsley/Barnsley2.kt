package net.fwitz.math.main.binary.escape.barnsley

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.EscapeTimePaletteFunction
import net.fwitz.math.plot.renderer.palette.PaletteRandom

/**
 * "Second Michael Barnsley Fractal"
 *
 * Stevens, Roger T. (1990). _Advanced Fractal Programming in C_. "Some Barnsley Fractals" (pp. 153-155)
 */
object Barnsley2 {
    private const val P_MIN = 0.0
    private const val P_MAX = 1.0
    private const val Q_MIN = 0.0
    private const val Q_MAX = 0.6
    private const val ITERS = 1000

    private const val p = 0.6
    private const val q = 1.1

    private val c = Complex(p, q)

    private val BARNSLEY_2 = EscapeFunction.builder<Complex>()
        .containmentTest { it.abs2 < 4 }
        .step { _, z ->
            val cz = c * z
            when {
                cz.y >= 0 -> cz - c
                else -> cz + c
            }
        }
        .maxIters(ITERS)
        .build()

    @JvmStatic
    fun main(args: Array<String>) = EscapeTimePlot.complex("Barnsley 2 (Escape time)")
        .computeFn(BARNSLEY_2)
        .colorFn(EscapeTimePaletteFunction.escapeTime(PaletteRandom(1000)))
        .domainX(P_MIN, P_MAX)
        .domainY(Q_MIN, Q_MAX)
        .render()
}