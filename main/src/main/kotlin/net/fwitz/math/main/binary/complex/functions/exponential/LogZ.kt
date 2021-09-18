package net.fwitz.math.main.binary.complex.functions.exponential

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringAdvancedPlusLogScale

object LogZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("log(z)")
        .computeFn(Complex::log)
        .colorFn(DomainColoringAdvancedPlusLogScale)
        .render()
}