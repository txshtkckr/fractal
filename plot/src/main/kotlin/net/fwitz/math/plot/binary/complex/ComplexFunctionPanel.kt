package net.fwitz.math.plot.binary.complex

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel

class ComplexFunctionPanel internal constructor(
    plot: ComplexFunctionPlot
) : BinaryNumberFunctionPanel<Complex, Complex>(
    Complex::class.java,
    Complex::class.java,
    plot
) {
    override fun createRenderer(width: Int, height: Int): ComplexFunctionRenderer<Complex> {
        return ComplexFunctionRenderer(
            this, valueType,
            width, height,
            ComplexFunctionValuesFilters
        )
    }
}