package net.fwitz.math.main.ifs

import net.fwitz.math.plot.canvas.CanvasPlot

object ChaosSierpinskiTriangle {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.chaos(
        title = "Sierpinski Triangle (Chaos)",
        n = 3
    ).render()
}