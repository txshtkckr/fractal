package net.fwitz.math.main.binary.escape.mandel

object Mandels16Plot : MandelbrotPlot(
    name = "Mandel16 (Escape Time)",
    power = 16.0
) {
    @JvmStatic
    fun main(args: Array<String>) = Mandels16Plot.render()
}