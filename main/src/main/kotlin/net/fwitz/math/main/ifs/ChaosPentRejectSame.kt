package net.fwitz.math.main.ifs

import net.fwitz.math.plot.canvas.CanvasPlot
import net.fwitz.math.plot.ifs.DrawChaosGame.Mode.REJECT_SAME

object ChaosPentRejectSame {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.chaos(
        title = "Pentagons (Chaos, r=0.5, mode=REJECT_SAME)",
        n = 5,
        r = 0.5,
        mode = REJECT_SAME
    ).render()
}