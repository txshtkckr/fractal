package net.fwitz.math.main.binary.escape

object MandelPiPlot : MandelbrotPlot(
    name = "MandelPi (Escape Time)",
    power = Math.PI
) {
    @JvmStatic
    fun main(args: Array<String>) = MandelPiPlot.render()
}