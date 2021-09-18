package net.fwitz.math.main.binary.complex.analysis.zeta

import net.fwitz.math.binary.complex.analysis.RiemannZeta
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringContour
import net.fwitz.math.plot.binary.complex.color.DomainColoringZeroBasin

object ZetaPlot {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("zeta(s)")
        .size(500, 500)
        .domainX(-20.0, 20.0)
        .domainY(-20.0, 20.0)
        .computeFn(RiemannZeta::zeta)
        .colorFn(DomainColoringContour)
        .render()
}