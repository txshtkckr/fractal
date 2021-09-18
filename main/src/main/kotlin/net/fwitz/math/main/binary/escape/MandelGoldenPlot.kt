package net.fwitz.math.main.binary.escape

import kotlin.math.sqrt

object MandelGoldenPlot : MandelbrotPlot(
    name = "MandelPhi (Escape Time)",
    power = 0.5 + sqrt(5.0) / 2
) {
    @JvmStatic
    fun main(args: Array<String>) = MandelGoldenPlot.render()
}