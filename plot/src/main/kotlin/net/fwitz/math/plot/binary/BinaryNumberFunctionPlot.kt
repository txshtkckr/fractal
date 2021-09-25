package net.fwitz.math.plot.binary

import net.fwitz.math.binary.BinaryNumber
import java.awt.Color
import java.util.function.Consumer
import java.util.function.Function
import javax.swing.JFrame
import javax.swing.WindowConstants

/**
 * @param <L> the plot type (sub-classes should specify themselves)
 * @param <P> the panel type
 * @param <T> the binary number type
 * @param <V> the computed value type
*/
abstract class BinaryNumberFunctionPlot<
        L : BinaryNumberFunctionPlot<L, P, T, V>,
        P : BinaryNumberFunctionPanel<T, V>,
        T : BinaryNumber<T>,
        V
> protected constructor(
    private val title: String
) {
    companion object {
        private const val DEFAULT_HEIGHT = 600
        private const val DEFAULT_WIDTH = 600
    }

    protected var width = DEFAULT_WIDTH
    protected var height = DEFAULT_HEIGHT

    var x1 = -5.0
    var y1 = -5.0
    var x2 = 5.0
    var y2 = 5.0
    var computeFn: ((T) -> V)? = null
    var colorFn: ((T, V) -> Color)? = null

    private var panelDecorator : (P) -> Unit = {}
    abstract fun value(x: Double, y: Double): T

    fun width(width: Int): L {
        require(!(width < 100 || width > 16384)) { "width: $width" }
        this.width = width
        return self
    }

    fun height(height: Int): L {
        require(!(height < 100 || height > 16384)) { "height: $height" }
        this.height = height
        return self
    }

    fun size(width: Int, height: Int): L {
        return width(width).height(height)
    }

    fun domainX(x1: Double, x2: Double): L {
        this.x1 = x1
        this.x2 = x2
        return self
    }

    fun domainY(y1: Double, y2: Double): L {
        this.y1 = y1
        this.y2 = y2
        return self
    }

    fun computeFn(computeFn: (T) -> V): L {
        this.computeFn = computeFn
        return self
    }

    fun colorFn(colorFn: (T, V) -> Color): L {
        this.colorFn = colorFn
        return self
    }

    fun decoratePanel(decorator: (P) -> Unit): L {
        val prev = panelDecorator
        panelDecorator = { panel ->
            prev(panel)
            decorator(panel)
        }
        return self
    }

    // Compiler is too stupid to understand that "this" is always <L>.
    @Suppress("UNCHECKED_CAST")
    protected val self: L = this as L

    fun render() {
        val frame = JFrame(title)
        frame.setSize(width, height)
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        val panel = createPanel()
        frame.setContentPane(panel)
        panelDecorator(panel)
        frame.setVisible(true)
    }

    protected abstract fun createPanel(): P
}