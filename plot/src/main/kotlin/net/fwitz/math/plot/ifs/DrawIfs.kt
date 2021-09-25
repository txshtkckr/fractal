package net.fwitz.math.plot.ifs

import net.fwitz.math.fractal.ifs.IfsParams
import net.fwitz.math.fractal.ifs.IfsTransform
import net.fwitz.math.numth.numbers.Randomizer.RANDOM
import net.fwitz.math.plot.canvas.CanvasRenderer
import net.fwitz.math.plot.renderer.palette.PaletteEGA16

class DrawIfs(params: IfsParams, val iters: Int = DEFAULT_ITERS) : (CanvasRenderer) -> Unit {
    companion object {
        const val DEFAULT_ITERS = 1_000_000
        const val REPAINT_EVERY = 100
    }

    private val ifs = params.transforms
    private val xOff = params.xOff
    private val yOff = params.yOff
    private val scale = params.scale
    private val pTotal = ifs.sumOf { it.p }

    override fun invoke(renderer: CanvasRenderer) {
        val image = renderer.image

        var x = 0.0
        var y = 0.0
        for (i in 1..iters) {
            val attractor = selectAttractor()
            val params: IfsTransform = ifs[attractor]
            val newX = params.a * x + params.b * y + params.e
            y = params.c * x + params.d * y + params.f
            x = newX

            val px = ((x * scale + xOff) * image.width).toInt()
            val py = ((y * scale + yOff) * image.height).toInt()
            if (px in 0 until image.width && py in 0 until image.height) {
                val color = PaletteEGA16.indexExcluding0(attractor + 1)
                image.setRGB(px, py, color.rgb)
            }
            if (i % REPAINT_EVERY == 0) renderer.panel.repaint()
        }

        if (iters % REPAINT_EVERY != 0) renderer.panel.repaint()
    }

    private fun selectAttractor(): Int {
        var j = RANDOM.nextInt(pTotal)
        for (i in ifs.indices) {
            j -= ifs[i].p
            if (j < 0) return i
        }
        return ifs.lastIndex
    }
}