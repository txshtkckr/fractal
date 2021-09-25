package net.fwitz.math.plot.bifurc

import javax.swing.JFrame
import javax.swing.WindowConstants

class LogisticMapPlot(private val title: String) {
    companion object {
        private const val DEFAULT_HEIGHT = 600
        private const val DEFAULT_WIDTH = 600
        private const val DEFAULT_MINR = 0.0
        private const val DEFAULT_MAXR = 4.0
        private const val DEFAULT_MINXN = 0.0
        private const val DEFAULT_MAXXN = 1.0
    }

    protected var computeFn: ((Double) -> DoubleArray?)? = null
    protected var minr = DEFAULT_MINR
    protected var maxr = DEFAULT_MAXR
    protected var minxn = DEFAULT_MINXN
    protected var maxxn = DEFAULT_MAXXN
    protected var width = DEFAULT_WIDTH
    protected var height = DEFAULT_HEIGHT
    private var panelDecorator: (LogisticMapPanel) -> Unit = {}

    fun domainR(minr: Double, maxr: Double): LogisticMapPlot {
        this.minr = minr
        this.maxr = maxr
        return this
    }

    fun minr(minr: Double): LogisticMapPlot {
        this.minr = minr
        return this
    }

    fun maxr(maxr: Double): LogisticMapPlot {
        this.maxr = maxr
        return this
    }

    fun rangeXn(minxn: Double, maxxn: Double) = also {
        this.minxn = minxn
        this.maxxn = maxxn
    }

    fun minxn(minxn: Double): LogisticMapPlot = also { this.minxn = minxn }
    fun maxxn(maxxn: Double): LogisticMapPlot = also { this.maxxn = maxxn }

    fun width(width: Int): LogisticMapPlot = also {
        require(!(width < 100 || width > 16384)) { "width: $width" }
        this.width = width
    }

    fun height(height: Int): LogisticMapPlot {
        require(!(height < 100 || height > 16384)) { "height: $height" }
        this.height = height
        return this
    }

    fun size(width: Int, height: Int) = width(width).height(height)

    fun computeFn(computeFn: (Double) -> DoubleArray?): LogisticMapPlot {
        this.computeFn = computeFn
        return this
    }

    fun decoratePanel(decorator: (LogisticMapPanel) -> Unit): LogisticMapPlot {
        val prev = panelDecorator
        panelDecorator = { panel ->
            prev(panel)
            decorator(panel)
        }
        return this
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

    protected fun createPanel(): LogisticMapPanel {
        return LogisticMapPanel(minr, minxn, maxr, maxxn, computeFn!!)
    }
}