package net.fwitz.math.main.binary.escape

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.fractal.escape.collatz.Collatz.collatz
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.EscapeTimeInterpolator
import net.fwitz.math.plot.renderer.palette.EscapeTimePalette

object CollatzPlot {
    private const val P_MIN = -20.0
    private const val P_MAX = 20.0
    private const val Q_MIN = -20.0
    private const val Q_MAX = 20.0
    private const val ITERS = 1000
    private const val BAILOUT_RADIUS = 1 shl 8
    private const val BAILOUT = BAILOUT_RADIUS * BAILOUT_RADIUS

    private val COLLATZ = EscapeFunction.builder<Complex>()
        .step { _, z -> collatz(z) }
        .escapeTest { it.abs2 > BAILOUT }
        .maxIters(ITERS)
        .build()

    @JvmStatic
    fun main(args: Array<String>) = EscapeTimePlot.complex("Collatz (Escape time)")
        .computeFn(COLLATZ)
        .domainX(P_MIN, P_MAX)
        .domainY(Q_MIN, Q_MAX)
        .colorFn(EscapeTimeInterpolator(100.0, BAILOUT_RADIUS.toDouble(), EscapeTimePalette))
        .render()
}