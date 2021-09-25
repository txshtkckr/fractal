package net.fwitz.math.plot.bifurc

import net.fwitz.math.numth.numbers.Randomizer
import net.fwitz.math.plot.renderer.ImageRenderer
import java.awt.Color
import java.util.*
import java.util.concurrent.atomic.AtomicReferenceArray
import java.util.stream.DoubleStream
import kotlin.math.roundToInt

class LogisticMapRenderer(
    private val panel: LogisticMapPanel,
    width: Int,
    height: Int
) : ImageRenderer(width, height) {
    private val rValues: DoubleArray = partition(width, panel.minr, panel.maxr)
    private val blackColumn: IntArray
    private var minxn = 0.0
    private var maxxn = 0.0
    private val xnScale: Double
    private val values: AtomicReferenceArray<DoubleArray?>

    override fun render() {
        pipeline.flush()
        Randomizer.shuffledInts(width).forEach { x ->
            pipeline.execute { colRenderer(x) }
        }
    }

    private fun colRenderer(x: Int) {
        if (!pipeline.isShutdown) calculateColWithExceptionsHandled(x)
    }

    private fun calculateColWithExceptionsHandled(x: Int) {
        try {
            val values = calculateColValues(x)
            if (pipeline.isShutdown) return

            this.values[x] = values
            if (pipeline.isShutdown) return

            paintCol(x, values)
            if (pipeline.isShutdown) return

            panel.repaint()
        } catch (e: Exception) {
            println("Unexpected exception while rendering xa$x=; r=${rValues[x]}")
            e.printStackTrace()
        }
    }

    private fun calculateColValues(x: Int) = panel.computeFn(rValues[x])

    protected fun paintCol(x: Int, values: DoubleArray?) {
        if (values == null) return

        image.setRGB(x, 0, 1, height, blackColumn, 0, 0)
        if (values.isEmpty()) return

        val value = 1.0f / values.size
        val color = Color(1 - value, value, value / 2).rgb
        DoubleStream.of(*values)
            .mapToInt { xn: Double -> height - ((xn - minxn) * xnScale).roundToInt() }
            .filter { y: Int -> y in 0 until height }
            .forEach { y: Int -> image.setRGB(x, y, color) }
    }

    override fun toString(): String {
        return "ComplexFunctionRenderer[" + super.toString() + ']'
    }

    init {
        if (panel.minxn > panel.maxxn) {
            minxn = panel.maxxn
            maxxn = panel.minxn
        } else {
            minxn = panel.minxn
            maxxn = panel.maxxn
        }
        xnScale = height / (maxxn - minxn)
        values = AtomicReferenceArray(width)
        blackColumn = IntArray(height)
        Arrays.fill(blackColumn, Color.BLACK.rgb)
    }
}