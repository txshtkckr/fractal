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
        private val MIN_LEN = 2
        private val MAX_LEN = 20
        private val X0 = 0.5
        private val ITERS = 600

        private val DEFAULT_MIN_A = 2.0
        private val DEFAULT_MAX_A = 4.0
        private val DEFAULT_MIN_B = 2.0
        private val DEFAULT_MAX_B = 4.0

        private fun valid(vararg values: Double): Boolean {
            return DoubleStream.of(*values).allMatch { d: Double -> d.isFinite() }
        }

        private fun generateLyapunovString(): String {
            while (true) {
                val len = RANDOM.nextInt(MIN_LEN, MAX_LEN + 1)
                val s = (1..len).map { if (RANDOM.nextBoolean()) 'B' else 'A' }.joinToString("")
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
        handlers.put("quit", ImageRendererPanel.action {
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

        fun draw() {
            Randomizer.shuffledInts(width).forEach { x -> async { drawColumn(x) } }
        }

        private fun drawColumn(x: Int) {
            if (abort) return
            for (y in 0 until height) drawValue(x, y)
            renderer.panel.repaint()
        }

        private fun drawValue(px: Int, py: Int) {
            if (abort) return
            val lyapunov = calculateLyapunovExponent(px, py)
            image.setRGB(px, py, selectColor(lyapunov).rgb)
        }

        private fun calculateLyapunovExponent(px: Int, py: Int): Double {
            val a = minA + scaleA * px
            val b = maxB - scaleB * py
            val rn = generateSequence(0) { if (it == s.lastIndex) 0 else it + 1 }
                .map { s[it] }
                .map { if (it == 'A') a else b }
                .iterator()

            var x = rn.next() * X0 * (1 - X0)
            var accumulator = 0.0
            for (k in 1 until ITERS) {
                val r = rn.next()
                x *= r * (1 - x)
                val term = ln((r * (1.0 - 2.0 * x)).absoluteValue)
                accumulator += (term - accumulator) / (k + 1)
            }
            return accumulator
        }

        private fun selectColor(lyapunov: Double) = when {
            lyapunov.isNaN() -> Color.WHITE
            lyapunov.isInfinite() -> if (lyapunov > 0) Color.YELLOW else Color.BLACK
            lyapunov > 0 -> c(
                r = 0.4 + lyapunov * 1.5,
                g = lyapunov * 1.5,
                b = 0.0
            )
            lyapunov < 0 -> c(
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
