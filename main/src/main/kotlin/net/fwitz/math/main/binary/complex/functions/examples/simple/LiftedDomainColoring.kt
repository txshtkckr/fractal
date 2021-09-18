package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringZeroBasin

/**
 * Tasked from Figure 3 of
 * [this](http://www.mi.fu-berlin.de/en/math/groups/ag-geom/publications/db/lifteddomaincoloring.pdf)
 * article.  `[(z−1)(z+1)^2] / [(z+i)(z−i)^2]`
 */
object LiftedDomainColoring {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("[(z−1)(z+1)^2] / [(z+i)(z−i)^2]")
        .computeFn(::fn)
        .colorFn(DomainColoringZeroBasin)
        .domainX(-3.0, 3.0)
        .domainY(-3.0, 3.0)
        .render()

    private fun fn(z: Complex) = (z - 1) * ((z + 1).pow2) / (z.plusY(1) * (z.minusY(1).pow2))
}