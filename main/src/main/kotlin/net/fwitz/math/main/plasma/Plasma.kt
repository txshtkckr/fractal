package net.fwitz.math.main.plasma

import net.fwitz.math.numth.numbers.Randomizer.RANDOM
import net.fwitz.math.numth.numbers.Randomizer.randomRGB
import net.fwitz.math.plot.canvas.CanvasPlot
import net.fwitz.math.plot.canvas.CanvasRenderer
import java.awt.Color
import kotlin.math.sqrt

object Plasma {
    private val MAX_NUDGE = 256
    private val INITIAL_STEP = 256
    private val ONE_OVER_SQRT_2 = 1.0 / sqrt(2.0)

    @JvmStatic
    fun main(args: Array<String>) = CanvasPlot("plasma")
        .draw(::draw)
        .render()

    private fun draw(renderer: CanvasRenderer) = Draw(renderer).draw()

    private class Draw(val renderer: CanvasRenderer) {
        private val image = renderer.image
        private val abort get() = renderer.pipeline.isShutdown
        private val async = renderer::async

        fun draw() {
            val image = renderer.image
            for (y in 0 until image.height step INITIAL_STEP) {
                for (x in 0 until image.width step INITIAL_STEP) {
                    image.setRGB(x, y, randomRGB())
                }
                image.setRGB(image.width - 1, y, randomRGB())
            }

            for (x in 0 until image.width step INITIAL_STEP) {
                image.setRGB(x, image.height - 1, randomRGB())
            }

            image.setRGB(image.width - 1, image.height - 1, randomRGB())

            async {
                diamond(INITIAL_STEP, MAX_NUDGE)
            }
        }

        private fun diamond(step: Int, maxNudge: Int) {
            val half = (step + 1) shr 1
            if (half == 0) return

            for (y in half until image.height - 1 step step) {
                for (x in half until image.width - 1 step step) {
                    val color = average(
                        Point(x - half, y - half),
                        Point(x + half, y - half),
                        Point(x - half, y + half),
                        Point(x + half, y + half)
                    )
                    val nudged = nudge(color, maxNudge)
                    image.setRGB(x, y, nudged.rgb)
                }

                if (abort) return
            }

            async {
                square(step, reduceNudge(maxNudge))
            }
        }

        private fun square(step: Int, maxNudge: Int) {
            val half = step shr 1
            if (half == 0) return

            var toggle = true
            for (y in 0 until image.height step half) {
                val xStart = if (toggle) half else 0
                for (x in xStart until image.width step step) {
                    val color = average(
                        Point(x - half, y),
                        Point(x, y - half),
                        Point(x, y + half),
                        Point(x + half, y)
                    )
                    val nudged = nudge(color, maxNudge)
                    image.setRGB(x, y, nudged.rgb)
                }

                if (abort) return
                toggle = !toggle
            }

            async {
                diamond(half, reduceNudge(maxNudge))
            }
        }

        private fun average(vararg points: Point): Color {
            var count = 0
            var red = 0
            var green = 0
            var blue = 0
            points.asSequence()
                .filter { it.x in 0 until image.width }
                .filter { it.y in 0 until image.height }
                .map { point -> Color(image.getRGB(point.x, point.y)) }
                .forEach {
                    red += it.red
                    green += it.green
                    blue += it.blue
                    ++count
                }
            require(count > 0) { "All points were out of range: $points" }
            return Color(red / count, green / count, blue / count)
        }

        private fun average(colors: Array<Color>): Color {
            val r = colors.sumOf { it.red } / colors.size
            val g = colors.sumOf { it.green } / colors.size
            val b = colors.sumOf { it.blue } / colors.size
            return Color(r, g, b)
        }

        private fun nudge(color: Color, maxNudge: Int): Color {
            if (maxNudge <= 0) return color
            val r = nudge(color.red, maxNudge)
            val g = nudge(color.green, maxNudge)
            val b = nudge(color.blue, maxNudge)
            return Color(r, g, b)
        }

        private fun nudge(component: Int, maxNudge: Int): Int {
            val rand = RANDOM.nextInt(maxNudge * 2 + 1) - maxNudge  // range=[-maxNudge, +maxNudge]
            return (component + rand).coerceAtLeast(0).coerceAtMost(255)
        }


        private fun reduceNudge(maxNudge: Int) = when {
            maxNudge > 1 -> maxNudge.toDouble().times(ONE_OVER_SQRT_2).toInt()
            else -> 0
        }

    }

    data class Point(val x: Int, val y: Int)
}