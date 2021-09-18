package net.fwitz.math.plot.binary.dual

import net.fwitz.math.binary.dual.DualNumber
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel

class DualNumberFunctionPanel internal constructor(
    plot: DualNumberFunctionPlot
) : BinaryNumberFunctionPanel<DualNumber, DualNumber>(
    DualNumber::class.java,
    DualNumber::class.java,
    plot
) {
    override fun createRenderer(width: Int, height: Int) = DualNumberFunctionRenderer(
        this, valueType,
        width, height,
        DualNumberFunctionValuesFilters
    )
}