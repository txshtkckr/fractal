package net.fwitz.math.plot.binary.dual

import net.fwitz.math.binary.dual.DualNumber
import net.fwitz.math.plot.binary.dual.color.DomainColoringDual
import net.fwitz.math.plot.binary.BinaryNumberFunctionPlot

class DualNumberFunctionPlot(title: String) : BinaryNumberFunctionPlot<
        DualNumberFunctionPlot,
        DualNumberFunctionPanel,
        DualNumber,
        DualNumber
>(title) {
    init {
        colorFn(DomainColoringDual)
    }

    override fun value(x: Double, y: Double) = DualNumber(x, y)

    override fun createPanel() = DualNumberFunctionPanel(this)
}