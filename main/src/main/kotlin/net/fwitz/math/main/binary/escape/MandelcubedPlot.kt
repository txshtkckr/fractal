package net.fwitz.math.main.binary.escape

object MandelcubedPlot : MandelbrotPlot(
    name = "Mandelcubed (Escape Time)",
    power = 3.0
) {
    @JvmStatic
    fun main(args: Array<String>) = MandelcubedPlot.render()
}