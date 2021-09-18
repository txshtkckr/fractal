package net.fwitz.math.main.binary.complex.analysis.gamma

import net.fwitz.math.binary.complex.analysis.Gamma
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringSmooth

object GammaPlot {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("gamma(z)")
        .computeFn(Gamma::gamma)
        .colorFn(DomainColoringSmooth)
        .render()
}