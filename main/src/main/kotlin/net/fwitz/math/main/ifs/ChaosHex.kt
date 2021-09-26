package net.fwitz.math.main.ifs

import net.fwitz.math.plot.canvas.CanvasPlot

object ChaosHex {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.chaos(
        title = "Hexagons (Chaos)",
        n = 6
    ).render()
}