package net.fwitz.math.main.ifs

import net.fwitz.math.plot.canvas.CanvasPlot

object ChaosHendec {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.chaos(
        title = "Hendecagon (Chaos)",
        n = 11
    ).render()
}