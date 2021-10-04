package net.fwitz.math.plot.binary

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.plot.renderer.ImageRendererPanel
import java.awt.Color
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.ActionMap
import javax.swing.InputMap
import javax.swing.KeyStroke
import kotlin.system.exitProcess

abstract class BinaryNumberFunctionPanel<T : BinaryNumber<T>, V>(
    protected val binaryNumberType: Class<T>,
    protected val valueType: Class<V>,
    protected val plot: BinaryNumberFunctionPlot<*, *, T, V>
) : ImageRendererPanel<BinaryNumberFunctionRenderer<T, V>?>(), MouseListener {
    val computeFunction: (T) -> V = plot.computeFn!!
    val colorFunction: (T, V) -> Color = plot.colorFn!!

    @Volatile
    var minX = 0.0

    @Volatile
    var maxX = 0.0

    @Volatile
    var minY = 0.0

    @Volatile
    var maxY = 0.0

    abstract override fun createRenderer(width: Int, height: Int): BinaryNumberFunctionRenderer<T, V>

    val mouseLocationAsValue: T? get() = getMouseLocationAsValue(null)

    fun getMouseLocationAsValue(event: MouseEvent?): T? {
        val loc = getMouseLocationAsRelativePoint(event) ?: return null
        return plot.value(
            x = scaleToBounds(loc.getX(), minX, maxX),
            y = scaleToBounds(1 - loc.getY(), minY, maxY)
        )
    }

    private fun setBounds(x1: Double, y1: Double, x2: Double, y2: Double) {
        require(valid(x1, y1, x2, y2)) { "Invalid bounds: ($x1, $y1) - ($x2, $y2)" }
        if (x1 > x2) {
            minX = x2
            maxX = x1
        } else {
            minX = x1
            maxX = x2
        }
        if (y1 > y2) {
            minY = y2
            maxY = y1
        } else {
            minY = y1
            maxY = y2
        }
        println("New bounds: ($minX, $minY) - ($maxX, $maxY)")
    }

    fun zoomIn() {
        val xAdjust = (maxX - minX) / 4
        val yAdjust = (maxY - minY) / 4
        setBounds(minX + xAdjust, minY + yAdjust, maxX - xAdjust, maxY - yAdjust)
        reset()
    }

    fun zoomOut() {
        val xAdjust = (maxX - minX) / 2
        val yAdjust = (maxY - minY) / 2
        setBounds(minX - xAdjust, minY - yAdjust, maxX + xAdjust, maxY + yAdjust)
        reset()
    }

    fun center(z: T) {
        println("Centering on $z")
        val xDelta = maxX - minX
        val yDelta = maxY - minY
        val xOffset: Double = z.x - xDelta / 2
        val yOffset: Double = z.y - yDelta / 2
        setBounds(xOffset, yOffset, xOffset + xDelta, yOffset + yDelta)
        reset()
    }

    private fun addListeners() {
        val keyMap: InputMap = inputMap
        keyMap.put(KeyStroke.getKeyStroke('q'), "quit")
        keyMap.put(KeyStroke.getKeyStroke('\u001B'), "quit")
        keyMap.put(KeyStroke.getKeyStroke('m'), "filterModeForward")
        keyMap.put(KeyStroke.getKeyStroke('M'), "filterModeBackward")
        keyMap.put(KeyStroke.getKeyStroke(']'), "zoomIn")
        keyMap.put(KeyStroke.getKeyStroke('['), "zoomOut")
        keyMap.put(KeyStroke.getKeyStroke('0'), "resetBounds")

        val handlers: ActionMap = actionMap
        handlers.put("quit", action {
            renderer?.shutdown()
            exitProcess(0)
        })
        handlers.put("filterModeForward", action { renderer?.filterModeForward() })
        handlers.put("filterModeBackward", action { renderer?.filterModeBackward() })
        handlers.put("zoomIn", action { zoomIn() })
        handlers.put("zoomOut", action { zoomOut() })
        handlers.put("resetBounds", action { resetBounds() })
    }

    override fun mousePressed(e: MouseEvent) {
        if (e.button == 1) getMouseLocationAsValue(e)?.let { z -> center(z) }
    }

    private fun resetBounds() {
        setBounds(plot.x1, plot.y1, plot.x2, plot.y2)
        reset()
    }

    companion object {
        private fun valid(vararg values: Double) = values.all { it.isFinite() }
    }

    init {
        setBounds(plot.x1, plot.y1, plot.x2, plot.y2)
        addListeners()
    }
}