package net.fwitz.math.main.binary.complex.functions.trig

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object CosZ {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("cos(z)")
        .computeFn(Complex::cos)
        .domainX(-Math.PI, Math.PI)
        .domainY(-Math.PI, Math.PI)
        .render()
}