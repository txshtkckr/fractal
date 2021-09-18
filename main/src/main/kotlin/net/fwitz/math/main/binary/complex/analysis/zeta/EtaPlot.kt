package net.fwitz.math.main.binary.complex.analysis.zeta

import net.fwitz.math.binary.complex.analysis.DirichletEta
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringZeroBasin

object EtaPlot {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("eta(s)")
        .computeFn(DirichletEta::eta)
        .size(500, 1000)
        .domainX(-10.0, 10.0)
        .domainY(-20.0, 20.0)
        .colorFn(DomainColoringZeroBasin)
        .render()
}