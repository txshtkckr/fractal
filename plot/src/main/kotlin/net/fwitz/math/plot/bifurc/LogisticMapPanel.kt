package net.fwitz.math.plot.bifurc

import net.fwitz.math.plot.renderer.ImageRendererPanel
import net.fwitz.math.plot.renderer.ImageRendererPanel.ActionHandler
import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import java.awt.geom.Point2D
import java.util.*
import java.util.function.DoubleFunction
import java.util.stream.DoubleStream
import javax.swing.KeyStroke
import kotlin.system.exitProcess

class LogisticMapPanel(
    minr: Double,
    minxn: Double,
    maxr: Double,
    maxxn: Double,
    val computeFn: (Double) -> DoubleArray?
) : ImageRendererPanel<LogisticMapRenderer>() {
    companion object {
        private fun valid(vararg values: Double): Boolean {
            return DoubleStream.of(*values).allMatch { d: Double -> java.lang.Double.isFinite(d) }
        }
    }

    init {
        setBounds(minr, minxn, maxr, maxxn)
        addListeners()
    }

    @Volatile
    var minr = 0.0

    @Volatile
    var maxr = 0.0

    @Volatile
    var minxn = 0.0

    @Volatile
    var maxxn = 0.0

    override fun createRenderer(width: Int, height: Int): LogisticMapRenderer {
        return LogisticMapRenderer(this, width, height)
    }

    private fun addListeners() {
        val keyMap = inputMap
        keyMap.put(KeyStroke.getKeyStroke('q'), "quit")
        keyMap.put(KeyStroke.getKeyStroke('\u001B'), "quit")
        keyMap.put(KeyStroke.getKeyStroke(']'), "zoomIn")
        keyMap.put(KeyStroke.getKeyStroke('['), "zoomOut")

        val handlers = actionMap
        handlers.put("quit", action {
            renderer?.shutdown()
            exitProcess(0)
        })
        handlers.put("zoomIn", action { zoomIn() })
        handlers.put("zoomOut", action { zoomOut() })
    }

    fun zoomIn() {
        val rAdjust = (maxr - minr) / 4
        val xnAdjust = (maxxn - minxn) / 4
        setBounds(minr + rAdjust, minxn + xnAdjust, maxr - rAdjust, maxxn - xnAdjust)
        reset()
    }

    fun zoomOut() {
        val rAdjust = (maxr - minr) / 2
        val xnAdjust = (maxxn - minxn) / 2
        setBounds(minr - rAdjust, minxn - xnAdjust, maxr + rAdjust, maxxn + xnAdjust)
        reset()
    }

    fun center(location: Point2D.Double) {
        val rDelta = maxr - minr
        val xnDelta = maxxn - minxn
        val rOffset = location.getX() - rDelta / 2
        val xnOffset = location.getY() - xnDelta / 2
        setBounds(rOffset, xnOffset, rOffset + rDelta, xnOffset + xnDelta)
        reset()
    }

    private fun setBounds(minr: Double, minxn: Double, maxr: Double, maxxn: Double) {
        require(valid(minr, minxn, maxr, maxxn)) {
            "Invalid bounds: (" + minr + ", " + minxn + ") - (" +
                    maxr + ", " + maxxn + ')'
        }
        if (minr > maxr) {
            this.minr = maxr
            this.maxr = minr
        } else {
            this.minr = minr
            this.maxr = maxr
        }
        if (minxn > maxxn) {
            this.minxn = maxxn
            this.maxxn = minxn
        } else {
            this.minxn = minxn
            this.maxxn = maxxn
        }
    }

    // Glue code to make AbstractAction suck less by looking like a functional interface
    val mouseLocationInMap: Point2D.Double? get() = getMouseLocationInMap(null)

    fun getMouseLocationInMap(e: MouseEvent?): Point2D.Double? {
        val loc = getMouseLocationAsRelativePoint(e) ?: return null
        return Point2D.Double(
            scaleToBounds(loc.getX(), minr, maxr),
            scaleToBounds(1 - loc.getY(), minxn, maxxn)
        )
    }

    override fun mousePressed(e: MouseEvent) {
        if (e.button == 1) getMouseLocationInMap(e)?.let { center(it) }
    }
}