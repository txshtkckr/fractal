package net.fwitz.math.plot.canvas

import net.fwitz.math.plot.renderer.ImageRenderer
import java.awt.Color

class CanvasRenderer(
    val panel: CanvasPanel,
    width: Int,
    height: Int,
    background: Color
) : ImageRenderer(width, height, background) {
    override fun render() {
        pipeline.flush()
        async(panel.drawStepFn)
    }

    fun async(block: (CanvasRenderer) -> Unit) = pipeline.execute {
        if (!pipeline.isShutdown) {
            block(this)
            if (!pipeline.isShutdown) panel.repaint()
        }
    }

}