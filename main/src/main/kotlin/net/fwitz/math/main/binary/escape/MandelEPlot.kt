package net.fwitz.math.main.binary.escape

object MandelEPlot : MandelbrotPlot(
    name = "MandelE (Escape Time)",
    power = Math.E
) {
    @JvmStatic
    fun main(args: Array<String>) = MandelEPlot.render()
}