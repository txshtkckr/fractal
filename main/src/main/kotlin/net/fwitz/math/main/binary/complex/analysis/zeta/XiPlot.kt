package net.fwitz.math.main.binary.complex.analysis.zeta

import net.fwitz.math.binary.complex.analysis.RiemannZeta
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object XiPlot {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("xi(s)")
        .domainX(-5.0, 5.0)
        .domainY(-5.0, 5.0)
        .size(500, 500)
        .computeFn(RiemannZeta::xi)
        .render()
}