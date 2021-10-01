package net.fwitz.math.main.binary.escape

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.plot.binary.escape.EscapeTimePanel
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.EscapeTimePaletteFunction.Companion.escapeTime
import net.fwitz.math.plot.renderer.ImageRendererPanel
import net.fwitz.math.plot.renderer.palette.EscapeTimePalette
import net.fwitz.math.plot.renderer.palette.Palette
import javax.swing.KeyStroke

/**
 * Provides the general tools for iterated function plots like the Mandelbrot Set where there is a notion
 * of a paired Julia Set. Specifically, given some iteration function "f(z)", the regular set is the one
 * given by iterating `z ← f(z) + c`, where `c` is the coordinate within the map. Upon selecting a coordinate
 * for the "Julia mode", we start with the coordinate value as the initial `z` and keep `c` at the selected
 * value everywhere.
 */
abstract class AbstractEscapeTimeMapPlot(
    private val iters: Int = DEFAULT_MAX_ITERS
) {
    companion object {
        private const val DEFAULT_MAX_ITERS = 500
    }

    protected abstract val title: String
    protected abstract val pMin: Double
    protected abstract val pMax: Double
    protected abstract val qMin: Double
    protected abstract val qMax: Double

    protected open val bailout = 100

    protected open fun init(c: Complex) = c
    protected open fun palette(): Palette = EscapeTimePalette
    protected abstract fun step(c: Complex, z: Complex): Complex

    private val mapFn = EscapeFunction.builder<Complex>()
        .includeInit()
        .step(::step)
        .escapeTest { z -> z.abs2 >= bailout }
        .maxIters(iters)
        .build()

    private fun juliaFn(c: Complex) = EscapeFunction.builder<Complex>()
        .step { _, z -> step(c, z) }  // using fixed 'c' instead of input coordinate
        .escapeTest { z -> z.abs2 >= bailout }
        .maxIters(iters)
        .build()

    @Volatile
    private var delegate: EscapeFunction<Complex> = mapFn

    private fun createPlot() = EscapeTimePlot.complex(title)
        .computeFn { c -> delegate(c) }
        .domainX(pMin, pMax)
        .domainY(qMin, qMax)
        .colorFn(escapeTime(palette()))
        .decoratePanel(::addListeners)

    fun render() = createPlot().render()

    private fun toggleJulia(panel: EscapeTimePanel<Complex>) {
        if (delegate !== mapFn) {
            // Switching from Julia to Mandelbrot, so it doesn't matter where the mouse is
            println("Mode change: Map ← Julia")
            delegate = mapFn
            panel.reset()
        } else {
            panel.mouseLocationAsValue?.let { c0 ->
                println("Mode change: Map → Julia @ $c0")
                delegate = juliaFn(c0)
                panel.reset()
            }
        }
    }

    protected open fun addListeners(panel: EscapeTimePanel<Complex>) {
        panel.inputMap.put(KeyStroke.getKeyStroke('j'), "toggleJulia")
        panel.actionMap.put("toggleJulia", ImageRendererPanel.action { toggleJulia(panel) })
    }
}