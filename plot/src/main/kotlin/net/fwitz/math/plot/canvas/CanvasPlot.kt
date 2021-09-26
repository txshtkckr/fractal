package net.fwitz.math.plot.canvas

import net.fwitz.math.fractal.ifs.IfsParams
import net.fwitz.math.plot.ifs.DrawChaosGame
import net.fwitz.math.plot.ifs.DrawChaosGame.Mode
import net.fwitz.math.plot.ifs.DrawIfs
import net.fwitz.math.plot.renderer.palette.Palette
import net.fwitz.math.plot.renderer.palette.PaletteContrast
import java.awt.Color
import javax.swing.JFrame
import javax.swing.WindowConstants

class CanvasPlot(private val title: String) {
    companion object {
        private const val DEFAULT_HEIGHT = 600
        private const val DEFAULT_WIDTH = 600

        fun ifs(params: IfsParams, palette: Palette = PaletteContrast) = CanvasPlot(params.title)
            .background(Color.BLACK)
            .draw(DrawIfs(params, palette))

        fun chaos(
            title: String,
            n: Int,
            r: Double = n.toDouble() / (n + 3),
            iters: Int = DrawChaosGame.DEFAULT_ITERS,
            palette: Palette = PaletteContrast,
            mode: Mode = Mode.NORMAL
        ) = CanvasPlot(title)
            .background(Color.BLACK)
            .draw(DrawChaosGame(
                n = n,
                r = r,
                iters = iters,
                palette = palette,
                mode = mode
            ))
    }

    protected var drawFn: ((CanvasRenderer) -> Unit)? = null
    protected var width = DEFAULT_WIDTH
    protected var height = DEFAULT_HEIGHT
    protected var background = Color.DARK_GRAY
    private var panelDecorator: (CanvasPanel) -> Unit = {}

    fun width(width: Int): CanvasPlot = also {
        require(!(width < 100 || width > 16384)) { "width: $width" }
        this.width = width
    }

    fun height(height: Int) = also {
        require(!(height < 100 || height > 16384)) { "height: $height" }
        this.height = height
    }

    fun size(width: Int, height: Int) = width(width).height(height)
    fun draw(drawFn: (CanvasRenderer) -> Unit) = also { this.drawFn = drawFn }
    fun background(background: Color) = also { this.background = background }

    fun decoratePanel(decorator: (CanvasPanel) -> Unit) = also {
        val prev = panelDecorator
        panelDecorator = { panel ->
            prev(panel)
            decorator(panel)
        }
    }

    fun render() {
        val frame = JFrame(title)
        frame.setSize(width, height)
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        val panel = createPanel()
        frame.contentPane = panel
        panelDecorator(panel)
        frame.isVisible = true
    }

    protected fun createPanel() = CanvasPanel(drawFn!!, background)
}
