package net.fwitz.math.main.binary.complex.analysis.gamma

import net.fwitz.math.binary.complex.analysis.Gamma
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringHanlu

object LnGammaPlot {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("lnGamma(z)")
        .computeFn(Gamma::lnGamma)
        .colorFn(DomainColoringHanlu)
        .render()
}