package net.fwitz.math.plot.ifs

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.numth.numbers.Randomizer.RANDOM
import net.fwitz.math.plot.canvas.CanvasRenderer
import net.fwitz.math.plot.renderer.palette.Palette
import net.fwitz.math.plot.renderer.palette.PaletteContrast

class DrawChaosGame(
    val n: Int,
    val r: Double,
    val iters: Int = DEFAULT_ITERS,
    val palette: Palette = PaletteContrast,
    val mode: Mode = Mode.NORMAL
) : (CanvasRenderer) -> Unit {
    companion object {
        const val DEFAULT_ITERS = 100_000
        const val SKIP_ITERS = 10
        const val REPAINT_EVERY = 1000
    }

    init {
        require(n >= 3) { "We need n >= 3 to make it interesting" }
        require(r != 0.0) { "Moving a distance of 0 doesn't make any sense" }
    }

    override fun invoke(renderer: CanvasRenderer) {
        val image = renderer.image
        val attractors = generateAttractors()
        val select = createSelector()

        val halfWidth = image.width / 2.0
        val halfHeight = image.height / 2.0

        var x = 0.0
        var y = 0.0
        for (i in 1..iters) {
            val attractor = select()
            x += (attractors[attractor].x - x) * r
            y += (attractors[attractor].y - y) * r

            val px = ((x + 1) * halfWidth).toInt()
            val py = ((y + 1) * halfHeight).toInt()
            if (px in 0 until image.width && py in 0 until image.height && i > SKIP_ITERS) {
                val color = palette.indexExcluding0(attractor + 1)
                image.setRGB(px, py, color.rgb)
            }
            if (i % REPAINT_EVERY == 0) renderer.panel.repaint()
        }

        if (iters % REPAINT_EVERY != 0) renderer.panel.repaint()
    }

    private fun generateAttractors(): List<Complex> {
        val start = when (n.and(1)) {
            0 -> Math.PI / n
            else -> Math.PI / -2
        }
        val step = Math.PI * 2 / n
        return (0 until n).asSequence()
            .map { it * step + start }
            .map { Complex.polar(1.0, it) }
            .toList()
    }

    private fun selectAny() = RANDOM.nextInt(n)

    private fun selectAnyExcept(excluded : Int): Int {
        val i = RANDOM.nextInt(n - 1)
        return when {
            i >= excluded -> i + 1
            else -> i
        }
    }

    private fun selectAnyExceptNeighbors(anchor : Int): Int {
        val i = RANDOM.nextInt(n - 2)
        return when (i) {
            0 -> anchor
            else -> (anchor + i + 1) % n  // Skip the next spot, wrapping around if needed
        }
    }

    private fun createSelector(): () -> Int {
        when (mode) {
            Mode.NORMAL -> return ::selectAny

            Mode.REJECT_SAME -> {
                var prev = RANDOM.nextInt(n)
                return {
                    selectAnyExcept(prev).also { prev = it }
                }
            }

            Mode.REJECT_NEIGHBORS -> {
                require(n >= 5) { "Need at least 5 points for this mode" }
                var prev = RANDOM.nextInt(n)
                return {
                    selectAnyExceptNeighbors(prev).also { prev = it }
                }
            }

            Mode.REJECT_NEIGHBORS_AFTER_REPEAT -> {
                require(n >= 4) { "Need at least 4 points for this mode" }
                var prev = RANDOM.nextInt(n)
                var repeat = false
                return {
                    when (repeat) {
                        false -> selectAny()
                        else -> selectAnyExceptNeighbors(prev)
                    }.also {
                        repeat = prev == it
                        prev = it
                    }
                }
            }
        }
    }

    enum class Mode {
        NORMAL,
        REJECT_SAME,
        REJECT_NEIGHBORS,
        REJECT_NEIGHBORS_AFTER_REPEAT
    }
}