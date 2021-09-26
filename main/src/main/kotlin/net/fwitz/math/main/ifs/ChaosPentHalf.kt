package net.fwitz.math.main.ifs

import net.fwitz.math.plot.canvas.CanvasPlot

object ChaosPentHalf {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.chaos(
        title = "Pentagons (Chaos, r=0.5)",
        n = 5,
        r = 0.5
    ).render()
}