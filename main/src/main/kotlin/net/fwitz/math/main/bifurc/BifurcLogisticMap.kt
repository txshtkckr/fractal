package net.fwitz.math.main.bifurc

import net.fwitz.math.fractal.bifurc.BifurcLogisticMapFunction
import net.fwitz.math.plot.bifurc.LogisticMapPlot

object BifurcLogisticMap {
    @JvmStatic
    fun main(args: Array<String>) = LogisticMapPlot("rx(1-x)")
        .computeFn { x -> BifurcLogisticMapFunction.evaluate(x) }
        .render()
}