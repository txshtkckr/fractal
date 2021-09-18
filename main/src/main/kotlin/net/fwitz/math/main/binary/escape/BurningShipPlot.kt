package net.fwitz.math.main.binary.escape

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.fractal.escape.EscapeTimeResult
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.EscapeTimePaletteFunction
import net.fwitz.math.plot.renderer.palette.PaletteVGA256
import java.util.function.Function
import java.util.function.Predicate

object BurningShipPlot {
    private const val P_MIN = -2.0
    private const val P_MAX = 1.0
    private const val Q_MIN = -1.5
    private const val Q_MAX = 1.5
    private const val ITERS = 1000

    private val BURNING_SHIP = EscapeFunction.builder<Complex>()
        .includeInit()
        .containmentTest { it.abs < 2 }
        .step { c, z -> z.rectify.pow2 + c }
        .maxIters(ITERS)
        .build()

    @JvmStatic
    fun main(args: Array<String>) = EscapeTimePlot.complex("Burning Ship (Escape time)")
        .computeFn(BURNING_SHIP)
        .colorFn(EscapeTimePaletteFunction.escapeTime(PaletteVGA256))
        .domainX(P_MIN, P_MAX)
        .domainY(Q_MIN, Q_MAX)
        .render()
}