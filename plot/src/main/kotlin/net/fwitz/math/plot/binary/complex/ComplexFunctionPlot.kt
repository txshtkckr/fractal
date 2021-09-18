package net.fwitz.math.plot.binary.complex

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.BinaryNumberFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringAdvanced

class ComplexFunctionPlot(
    title: String
) : BinaryNumberFunctionPlot<ComplexFunctionPlot, ComplexFunctionPanel, Complex, Complex>(title) {
    override fun value(x: Double, y: Double): Complex {
        return Complex.complex(x, y)
    }

    override fun createPanel() = ComplexFunctionPanel(this)

    init {
        colorFn(DomainColoringAdvanced())
    }
}