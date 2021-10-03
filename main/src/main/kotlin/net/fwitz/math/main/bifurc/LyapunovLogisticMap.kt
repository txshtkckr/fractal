package net.fwitz.math.main.bifurc

import net.fwitz.math.numth.numbers.Randomizer
import net.fwitz.math.numth.numbers.Randomizer.RANDOM
import net.fwitz.math.plot.canvas.CanvasPanel
import net.fwitz.math.plot.canvas.CanvasPlot
import net.fwitz.math.plot.canvas.CanvasRenderer
import net.fwitz.math.plot.renderer.ImageRendererPanel
import net.fwitz.math.plot.renderer.ImageRendererPanel.Companion.action
import java.awt.Color
import java.awt.event.MouseEvent
import java.awt.geom.Point2D
import java.util.stream.DoubleStream
import javax.swing.KeyStroke
import kotlin.math.absoluteValue
import kotlin.math.ln
import kotlin.system.exitProcess

class LyapunovLogisticMap(val s: String) {
    companion object {
        private const val MIN_LEN = 2
        private const val MAX_LEN = 16
        private const val X0 = 0.5
        private const val EPSILON = 1e-3
        private const val ITERS = 600

        private const val DEFAULT_MIN_A = 0.0
        private const val DEFAULT_MAX_A = 4.0
        private const val DEFAULT_MIN_B = 0.0
        private const val DEFAULT_MAX_B = 4.0

        private const val FILL_STEP = 4
        private const val REDRAW_GEARING = 17

        private fun valid(vararg values: Double): Boolean {
            return DoubleStream.of(*values).allMatch { d: Double -> d.isFinite() }
        }

        private fun generateLyapunovString(): String {
            val len = RANDOM.nextInt(MIN_LEN, MAX_LEN + 1)
            while (true) {
                val x = RANDOM.nextLong()
                val s = (0 until len).map { if (x.shr(it).and(1L) == 1L) 'B' else 'A' }.joinToString("")
                if (s.contains('A') && s.contains('B')) return s
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val s = if (args.isEmpty()) generateLyapunovString() else args[0]
            val map = LyapunovLogisticMap(s)
            CanvasPlot("Lyapunov $s")
                .draw(map::draw)
                .onClick(map::onClick)
                .decoratePanel(map::addListeners)
                .render()
        }
    }

    init {
        require(s.length in MIN_LEN..MAX_LEN) { "s.length=${s.length} not in [$MIN_LEN, $MAX_LEN]" }
        val rejected = s.find { it != 'A' && it != 'B' }
        require(rejected == null) { "Only 'A' and 'B' are allowed, but found '$rejected' in '$s'" }
    }

    @Volatile
    private var minA = DEFAULT_MIN_A

    @Volatile
    private var maxA = DEFAULT_MAX_A

    @Volatile
    private var minB = DEFAULT_MIN_B

    @Volatile
    private var maxB = DEFAULT_MAX_B

    private fun draw(renderer: CanvasRenderer) = Draw(renderer).draw()

    private fun addListeners(panel: CanvasPanel) {
        val keyMap = panel.inputMap
        keyMap.put(KeyStroke.getKeyStroke('q'), "quit")
        keyMap.put(KeyStroke.getKeyStroke('\u001B'), "quit")
        keyMap.put(KeyStroke.getKeyStroke(']'), "zoomIn")
        keyMap.put(KeyStroke.getKeyStroke('['), "zoomOut")
        keyMap.put(KeyStroke.getKeyStroke('0'), "reset")

        val handlers = panel.actionMap
        handlers.put("quit", action {
            panel.renderer?.shutdown()
            exitProcess(0)
        })
        handlers.put("zoomIn", action { zoomIn(panel) })
        handlers.put("zoomOut", action { zoomOut(panel) })
        handlers.put("reset", action { reset(panel) })
    }

    fun reset(panel: CanvasPanel) {
        setBounds(
            minA = DEFAULT_MIN_A,
            minB = DEFAULT_MIN_B,
            maxA = DEFAULT_MAX_A,
            maxB = DEFAULT_MAX_B
        )
        panel.reset()
    }

    fun zoomIn(panel: CanvasPanel) {
        val aAdjust = (maxA - minA) / 4
        val bAdjust = (maxB - minB) / 4
        setBounds(minA + aAdjust, minB + bAdjust, maxA - aAdjust, maxB - bAdjust)
        panel.reset()
    }

    fun zoomOut(panel: CanvasPanel) {
        val aAdjust = (maxA - minA) / 2
        val bAdjust = (maxB - minB) / 2
        setBounds(minA - aAdjust, minB - bAdjust, maxA + aAdjust, maxB + bAdjust)
        panel.reset()
    }

    fun center(location: Point2D.Double, panel: CanvasPanel) {
        val aDelta = maxA - minA
        val bDelta = maxB - minB
        val aOffset = location.getX() - aDelta / 2
        val bOffset = location.getY() - bDelta / 2
        setBounds(aOffset, bOffset, aOffset + aDelta, bOffset + bDelta)
        panel.reset()
    }

    private fun setBounds(minA: Double, minB: Double, maxA: Double, maxB: Double) {
        require(valid(minA, minB, maxA, maxB)) {
            "Invalid bounds: (" + minA + ", " + minB + ") - (" +
                    maxA + ", " + maxB + ')'
        }
        if (minA > maxA) {
            this.minA = maxA
            this.maxA = minA
        } else {
            this.minA = minA
            this.maxA = maxA
        }
        if (minB > maxB) {
            this.minB = maxB
            this.maxB = minB
        } else {
            this.minB = minB
            this.maxB = maxB
        }
    }

    fun getMouseLocationInMap(panel: CanvasPanel, e: MouseEvent): Point2D.Double? {
        val loc = panel.getMouseLocationAsRelativePoint(e) ?: return null
        return Point2D.Double(
            ImageRendererPanel.scaleToBounds(loc.getX(), minA, maxA),
            ImageRendererPanel.scaleToBounds(1 - loc.getY(), minB, maxB)
        )
    }

    private fun onClick(panel: CanvasPanel, e: MouseEvent) {
        if (e.button == 1) getMouseLocationInMap(panel, e)?.let { center(it, panel) }
    }


    private inner class Draw(val renderer: CanvasRenderer) {
        private val image = renderer.image
        private val abort get() = renderer.pipeline.isShutdown
        private val async = renderer::async

        private val width = image.width
        private val height = image.height

        private val minA = this@LyapunovLogisticMap.minA
        private val maxA = this@LyapunovLogisticMap.maxA
        private val scaleA = (maxA - minA) / width

        private val minB = this@LyapunovLogisticMap.minB
        private val maxB = this@LyapunovLogisticMap.maxB
        private val scaleB = (maxB - minB) / height

        private val aVals = (0 until width).map { minA + scaleA * it }.toList()
        private val bVals = (0 until height).map { maxB - scaleB * it }.toList()

        fun draw() {
            val offsets = (0 until FILL_STEP).flatMap { xOff ->
                (0 until FILL_STEP).map { yOff ->
                    xOff to yOff
                }
            }.toList()
            Randomizer.shuffle(offsets).forEach { (pxOff, pyOff) ->
                async {
                    draw(pxOff, pyOff)
                }
            }
        }

        private fun draw(pxOff: Int, pyOff: Int) {
            val rand = RANDOM.nextInt(8)
            when (rand and 4) {
                0 -> drawRowMajor(pxOff, pyOff, rand)
                else -> drawColMajor(pxOff, pyOff, rand)
            }
            renderer.panel.repaint()
        }

        private fun drawColMajor(pxOff: Int, pyOff: Int, rand: Int) {
            var counter = 0
            for (px in pxSeq(pxOff, rand)) {
                if (abort) return
                for (py in pySeq(pyOff, rand)) drawPoint(px, py)
                if (++counter == REDRAW_GEARING) {
                    counter = 0
                    renderer.panel.repaint()
                }
            }
        }

        private fun drawRowMajor(pxOff: Int, pyOff: Int, rand: Int) {
            var counter = 0
            for (py in pySeq(pyOff, rand)) {
                if (abort) return
                for (px in pxSeq(pxOff, rand)) drawPoint(px, py)
                if (++counter == REDRAW_GEARING) {
                    counter = 0
                    renderer.panel.repaint()
                }
            }
        }

        private fun drawPoint(px: Int, py: Int) {
            val a = aVals[px]
            val b = bVals[py]
            val lyapunov = calculateLyapunovExponent(a, b)
            val color = selectColor(lyapunov)
            image.setRGB(px, py, color.rgb)
        }

        private fun pxSeq(pxOff: Int, rand: Int) = pointSeq(pxOff, width, (rand and 2) == 0)
        private fun pySeq(pyOff: Int, rand: Int) = pointSeq(pyOff, height, (rand and 1) == 0)

        private fun pointSeq(offset: Int, bound: Int, invert: Boolean) = when (invert) {
            false -> offset until bound step FILL_STEP
            else -> {
                var start = bound - (bound % FILL_STEP) + offset
                if (start >= bound) start -= FILL_STEP
                start downTo 0 step FILL_STEP
            }
        }

        private fun calculateLyapunovExponent(a: Double, b: Double): Double {
            val rn = generateSequence(0) { if (it == s.lastIndex) 0 else it + 1 }
                .map { s[it] }
                .map { if (it == 'A') a else b }
                .iterator()


            var x = X0
            var r = rn.next()
            x *= r * (1 - x)

            // If the first iteration didn't go anywhere, then deliberately nudge it off the stable point to
            // get rid of the artifact that will otherwise show up from the lucky direct hit.
            if (x == X0) {
                x += EPSILON
                x *= r * (1 - x)
            }

            var accumulator = 0.0
            for (k in 1 until ITERS) {
                r = rn.next()
                x *= r * (1 - x)
                val err = (r * (1.0 - 2.0 * x)).absoluteValue
                val term = ln(err)
                if (term.isFinite()) accumulator += (term - accumulator) / (k + 1)
            }
            return accumulator
        }

        private fun selectColor(lyapunov: Double) = when {
            lyapunov.isNaN() -> Color.WHITE
            lyapunov.isInfinite() -> if (lyapunov > 0) Color.YELLOW else Color.BLACK
            lyapunov > 0.0 -> c(
                r = 0.4 + lyapunov * 1.5,
                g = lyapunov * 1.5,
                b = 0.0
            )
            lyapunov <= 0.0 -> c(
                r = 1.0 + lyapunov,
                g = 1.0 + lyapunov,
                b = 0.2 + (1.0 + lyapunov).coerceAtLeast(0.0)
            )
            else -> Color.GREEN
        }

        private fun c(r: Double, g: Double, b: Double) = Color(
            r.coerceAtLeast(0.0).coerceAtMost(1.0).toFloat(),
            g.coerceAtLeast(0.0).coerceAtMost(1.0).toFloat(),
            b.coerceAtLeast(0.0).coerceAtMost(1.0).toFloat()
        )
    }
}
