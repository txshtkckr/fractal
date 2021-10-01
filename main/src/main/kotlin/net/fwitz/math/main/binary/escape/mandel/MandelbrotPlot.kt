package net.fwitz.math.main.binary.escape.mandel

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.plot.binary.escape.EscapeTimePanel
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.EscapeTimeInterpolator
import net.fwitz.math.plot.renderer.ImageRendererPanel.Companion.action
import net.fwitz.math.plot.renderer.palette.EscapeTimePalette
import javax.swing.KeyStroke.getKeyStroke

open class MandelbrotPlot private constructor(
    name: String,
    private val power: Double,
    useShortcut: Boolean
) {
    companion object {
        private const val P_MIN = -2.0
        private const val P_MAX = 1.0
        private const val Q_MIN = -1.5
        private const val Q_MAX = 1.5
        private const val ITERS = 1000
        private const val BAILOUT_RADIUS = 5 //1 << 8;
        private const val BAILOUT = BAILOUT_RADIUS * BAILOUT_RADIUS

        @JvmStatic
        fun main(args: Array<String>) = MandelbrotPlot().render()

        private fun julia(c: Complex, power: Double) = EscapeFunction.builder<Complex>()
            .step { _, z -> z.pow(power) + c }
            .escapeTest { z -> z.abs2 >= BAILOUT }
            .maxIters(ITERS)
            .build()

        // Returns true if c is in the main cardioid or the largest circular bulb to avoid wasting iterations on them.
        // This is an optimization that greatly reduces the time to render the set when these portions of the set are
        // included in the picture.
        private fun inMainCardioidOrCircle(c: Complex): Boolean {
            val re = c.x
            if (re <= -0.75) {
                // Check the main circle
                var q = re + 1
                q = q * q + c.y * c.y
                return q < 0.0625
            }

            // Check the main cardioid
            val im = c.y
            var q = re - 0.25
            q = q * q + im * im
            q *= (q + re - 0.25)
            return q < 0.25 * im * im
        }
    }

    private val plot = EscapeTimePlot.complex(name)
        .computeFn { c -> delegate(c) }
        .domainX(P_MIN, P_MAX)
        .domainY(Q_MIN, Q_MAX)
        .colorFn(EscapeTimeInterpolator(power, BAILOUT_RADIUS.toDouble(), EscapeTimePalette))
        .decoratePanel(::addListeners)

    private val mandel = EscapeFunction.builder<Complex>()
        .includeInit()
        .step { c, z -> z.pow(power) + c }
        .escapeTest { z -> z.abs2 >= BAILOUT }
        .maxIters(ITERS)
        .also {
            if (useShortcut) it.shortcutContainmentTest { c -> inMainCardioidOrCircle(c) }
        }
        .build()

    @Volatile
    private var delegate: EscapeFunction<Complex> = mandel

    constructor() : this("Mandelbrot (Escape time)", 2.0, true)
    protected constructor(name: String, power: Double) : this(name, power, false)

    fun render() = plot.render()

    private fun toggleJulia(panel: EscapeTimePanel<Complex>) {
        if (delegate !== mandel) {
            // Switching from Julia to Mandelbrot, so it doesn't matter where the mouse is
            println("Mode change: Mandelbrot ← Julia")
            delegate = mandel
            panel.reset()
        } else {
            panel.mouseLocationAsValue?.let { c0 ->
                println("Mode change: Mandelbrot → Julia @ $c0")
                delegate = julia(c0, power)
                panel.reset()
            }
        }
    }

    private fun addListeners(panel: EscapeTimePanel<Complex>) {
        panel.inputMap.put(getKeyStroke('j'), "toggleJulia")
        panel.actionMap.put("toggleJulia", action { toggleJulia(panel) })
    }
}