package net.fwitz.math.plot.binary.escape

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.fractal.escape.EscapeTimeResult
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel
import net.fwitz.math.plot.binary.BinaryNumberFunctionRenderer

@Suppress("UNCHECKED_CAST")
class EscapeTimePanel<T : BinaryNumber<T>> internal constructor(
    binaryNumberType: Class<T>,
    plot: EscapeTimePlot<T>
) : BinaryNumberFunctionPanel<T, EscapeTimeResult<T>>(
    binaryNumberType,
    EscapeTimeResult::class.java as Class<EscapeTimeResult<T>>,
    plot
) {
    override fun createRenderer(
        width: Int,
        height: Int
    ): BinaryNumberFunctionRenderer<T, EscapeTimeResult<T>> = Renderer(this, width, height)

    internal inner class Renderer(
        panel: BinaryNumberFunctionPanel<T, EscapeTimeResult<T>>,
        width: Int,
        height: Int
    ) : BinaryNumberFunctionRenderer<T, EscapeTimeResult<T>>(
        panel,
        EscapeTimeResult::class.java as Class<EscapeTimeResult<T>>,
        width,
        height,
        EscapeTimeValuesFilters()
    ) {
        override fun value(x: Double, y: Double) = plot.value(x, y)
    }
}