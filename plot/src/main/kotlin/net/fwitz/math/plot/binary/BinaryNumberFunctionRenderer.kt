package net.fwitz.math.plot.binary

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.numth.numbers.Randomizer
import net.fwitz.math.plot.renderer.ImageRenderer
import net.fwitz.math.plot.renderer.newArray
import net.fwitz.math.plot.renderer.filter.ValuesFilter
import net.fwitz.math.plot.renderer.filter.ValuesFilters
import java.util.*
import java.util.concurrent.atomic.AtomicReferenceArray

abstract class BinaryNumberFunctionRenderer<T : BinaryNumber<T>, V>(
    protected val panel: BinaryNumberFunctionPanel<T, V>,
    protected val valueType: Class<V>,
    width: Int,
    height: Int,
    private val valuesFilters: ValuesFilters<V>
) : ImageRenderer(width, height) {

    private val xScale = partition(width, panel.minX, panel.maxX)
    private val yScale = partition(height, panel.maxY, panel.minY) // inverted
    private val values: AtomicReferenceArray<Array<V>> = AtomicReferenceArray<Array<V>>(height)

    @Volatile
    private var valuesFilterIndex = -1

    @Volatile
    private var valuesFilter: ValuesFilter<V> = ValuesFilter.identity()

    override fun render() {
        pipeline.flush()
        Arrays.stream(Randomizer.shuffledInts(height))
            .mapToObj { y: Int -> rowRenderer(y) }
            .forEach { runnable: Runnable -> pipeline.execute(runnable) }
    }

    private fun rowRenderer(y: Int) = Runnable {
        if (!pipeline.isShutdown) calculateRowWithExceptionsHandled(y)
    }

    private fun calculateRowWithExceptionsHandled(y: Int) {
        try {
            val values = calculateRowValues(y)
            if (pipeline.isShutdown) return

            this.values.set(y, values)
            val colors = calculateRowColors(y, values)
            if (pipeline.isShutdown) return

            applyRowColors(y, colors)
            if (pipeline.isShutdown) return

            panel.repaint()
        } catch (e: Exception) {
            System.err.println("Unexpected exception while rendering y=$y")
            e.printStackTrace()
        }
    }

    protected abstract fun value(x: Double, y: Double): T

    private fun calculateRowValues(y: Int): Array<V> {
        val yPart = yScale[y]
        return valueType.newArray(xScale.size) { x ->
            val xPart = xScale[x]
            val z = value(xPart, yPart)
            panel.computeFunction(z)
        }
    }

    private fun calculateRowColors(y: Int, values: Array<V>): IntArray {
        val yPart = yScale[y]
        val filteredValues: Array<V> = valuesFilter(xScale, yPart, values)
        val colors = IntArray(xScale.size)
        for (x in xScale.indices) {
            val xPart = xScale[x]
            val c = value(xPart, yPart)
            val value = filteredValues[x]
            colors[x] = panel.colorFunction(c, value).rgb
        }
        return colors
    }

    private fun onColorFunctionChanged() {
        println("Filter: ${valuesFilter.name}")
        yScale.indices.forEach { y ->
            values.get(y)?.let {
                val colors = calculateRowColors(y, it)
                applyRowColors(y, colors)
            }
        }
        panel.repaint()
    }

    fun filterModeForward() {
        val size: Int = valuesFilters.size
        if (size == 0) {
            return
        }
        val index = valuesFilterIndex + 1
        if (index >= size) {
            valuesFilterIndex = -1
            valuesFilter = ValuesFilter.identity()
        } else {
            valuesFilterIndex = index
            valuesFilter = valuesFilters[index]
        }
        onColorFunctionChanged()
    }

    fun filterModeBackward() {
        val size: Int = valuesFilters.size
        if (size == 0) {
            return
        }
        var index = valuesFilterIndex - 1
        if (index == -1) {
            valuesFilterIndex = -1
            valuesFilter = ValuesFilter.identity()
        } else {
            if (index < 0) {
                index = size - 1
            }
            valuesFilterIndex = index
            valuesFilter = valuesFilters[index]
        }
        onColorFunctionChanged()
    }

    override fun applyRowColors(y: Int, colors: IntArray) {
        image.setRGB(0, y, colors.size, 1, colors, 0, 0)
    }
}