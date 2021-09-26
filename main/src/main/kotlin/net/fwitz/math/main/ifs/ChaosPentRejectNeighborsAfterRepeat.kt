package net.fwitz.math.main.ifs

import net.fwitz.math.plot.canvas.CanvasPlot
import net.fwitz.math.plot.ifs.DrawChaosGame.Mode.REJECT_NEIGHBORS_AFTER_REPEAT

object ChaosPentRejectNeighborsAfterRepeat {
    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot.chaos(
        title = "Pentagons (Chaos, r=0.5, mode=REJECT_NEIGHBORS_AFTER_REPEAT)",
        n = 5,
        r = 0.5,
        mode = REJECT_NEIGHBORS_AFTER_REPEAT
    ).render()
}