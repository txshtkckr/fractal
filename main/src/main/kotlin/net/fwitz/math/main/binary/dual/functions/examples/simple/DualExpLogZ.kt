package net.fwitz.math.main.binary.dual.functions.examples.simple

import net.fwitz.math.binary.dual.DualNumber
import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot

object DualExpLogZ {
    @JvmStatic
    fun main(args: Array<String>) = DualNumberFunctionPlot("e^(log z)")
        .computeFn { w: DualNumber -> w.region1mapped { z -> z.log.exp } }
        .render()
}