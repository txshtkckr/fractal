package net.fwitz.math.main.binary.escape

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.plot.binary.escape.EscapeTimePanel
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.EscapeTimeInterpolator
import net.fwitz.math.plot.renderer.ImageRendererPanel.Companion.action
import net.fwitz.math.plot.renderer.palette.EscapeTimePalette
import javax.swing.KeyStroke.getKeyStroke

class SplitbrotPlot {
    companion object {
        private const val P_MIN = -3.0
        private const val P_MAX = 3.0
        private const val Q_MIN = -3.0
        private const val Q_MAX = 3.0
        private const val ITERS = 1000
        private const val BAILOUT_RADIUS = 1 shl 8
        private const val BAILOUT = BAILOUT_RADIUS * BAILOUT_RADIUS

        @JvmStatic
        fun main(args: Array<String>) = SplitbrotPlot().render()

        private fun julia(c: SplitComplex) = EscapeFunction.builder<SplitComplex>()
            .step { _, z -> z * z + c }
            .escapeTest { z -> z.abs2 >= BAILOUT }
            .maxIters(ITERS)
            .build()
    }

    private val plot = EscapeTimePlot
        .splitComplex("Split-Complex Mandelbrot (Escape time)")
        .computeFn { c -> delegate(c) }
        .domainX(P_MIN, P_MAX)
        .domainY(Q_MIN, Q_MAX)
        .colorFn(EscapeTimeInterpolator(2.0, BAILOUT_RADIUS.toDouble(), EscapeTimePalette))
        .decoratePanel { panel -> addListeners(panel) }

    private val dualbrot = EscapeFunction.builder<SplitComplex>()
        .includeInit()
        .step { c, z -> z * z + c }
        .escapeTest { z -> z.abs2 >= BAILOUT }
        .maxIters(ITERS)
        .build()

    @Volatile
    private var delegate: EscapeFunction<SplitComplex> = dualbrot

    fun render() = plot.render()

    private fun toggleJulia(panel: EscapeTimePanel<SplitComplex>) {
        if (delegate !== dualbrot) {
            // Switching from Julia to Mandelbrot, so it doesn't matter where the mouse is
            println("Mode change: Splitbrot ← Split Julia")
            delegate = dualbrot
            panel.reset()
        } else {
            panel.mouseLocationAsValue?.let { c0: SplitComplex ->
                println("Mode change: Splitbrot → Split Julia @ $c0")
                delegate = julia(c0)
                panel.reset()
            }
        }
    }

    private fun addListeners(panel: EscapeTimePanel<SplitComplex>) {
        panel.inputMap.put(getKeyStroke('j'), "toggleJulia")
        panel.actionMap.put("toggleJulia", action { toggleJulia(panel) })
    }
}