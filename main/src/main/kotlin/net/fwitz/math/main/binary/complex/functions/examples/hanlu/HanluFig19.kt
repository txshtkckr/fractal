package net.fwitz.math.main.binary.complex.functions.examples.hanlu

import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringHanlu

object HanluFig19 {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("sin(1/z)")
        .computeFn { z -> z.inverse.sin }
        .domainX(-1.0, 1.0)
        .domainY(-1.0, 1.0)
        .colorFn(DomainColoringHanlu)
        .render()
}