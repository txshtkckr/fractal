package net.fwitz.math.plot.binary.split

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.binary.split.SplitComplex.Companion.splitComplex
import net.fwitz.math.plot.binary.BinaryNumberFunctionPlot
import net.fwitz.math.plot.binary.split.color.DomainColoringSplit

class SplitComplexFunctionPlot(
    title: String
) : BinaryNumberFunctionPlot<SplitComplexFunctionPlot, SplitComplexFunctionPanel, SplitComplex, SplitComplex>(
    title
) {
    init {
        colorFn(DomainColoringSplit)
    }

    override fun value(x: Double, y: Double): SplitComplex {
        return splitComplex(x, y)
    }

    override fun createPanel(): SplitComplexFunctionPanel {
        return SplitComplexFunctionPanel(this)
    }
}