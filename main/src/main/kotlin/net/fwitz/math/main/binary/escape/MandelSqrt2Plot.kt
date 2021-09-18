package net.fwitz.math.main.binary.escape

import kotlin.math.sqrt

object MandelSqrt2Plot : MandelbrotPlot(
    name = "MandleSqrt2 (Escape Time)",
    power = sqrt(2.0)
) {
    @JvmStatic
    fun main(args: Array<String>) = MandelSqrt2Plot.render()
}