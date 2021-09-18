package net.fwitz.math.plot.binary.split

import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel
import net.fwitz.math.plot.binary.BinaryNumberFunctionRenderer
import net.fwitz.math.binary.split.SplitComplex

class SplitComplexFunctionPanel internal constructor(plot: SplitComplexFunctionPlot) :
    BinaryNumberFunctionPanel<SplitComplex, SplitComplex>(
        SplitComplex::class.java, SplitComplex::class.java, plot
    ) {
    override fun createRenderer(width: Int, height: Int): BinaryNumberFunctionRenderer<SplitComplex, SplitComplex> {
        return SplitComplexFunctionRenderer(
            this, valueType,
            width, height,
            SplitComplexFunctionValuesFilters
        )
    }
}