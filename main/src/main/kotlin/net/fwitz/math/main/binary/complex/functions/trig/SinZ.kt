package net.fwitz.math.main.binary.complex.functions.trig

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringAdvanced

object SinZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("sin(z)")
        .computeFn(Complex::sin)
        .domainX(-Math.PI, Math.PI)
        .domainY(-Math.PI, Math.PI)
        .render()
}