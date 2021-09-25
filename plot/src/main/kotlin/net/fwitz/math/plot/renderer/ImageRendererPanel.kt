package net.fwitz.math.plot.renderer

import java.awt.event.ActionEvent
import java.awt.geom.Point2D
import java.util.Optional
import java.awt.event.MouseListener
import java.awt.Graphics
import javax.swing.JPanel
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.atomic.AtomicBoolean
import java.awt.Image
import java.awt.MouseInfo
import java.awt.event.MouseEvent
import javax.swing.AbstractAction

abstract class ImageRendererPanel<R : ImageRenderer?> protected constructor() : JPanel(), MouseListener {
    private val rendererRef = AtomicReference<R?>()
    private val registeredMouseListener = AtomicBoolean()
    override fun paintComponent(g: Graphics) {
        renderer?.paint(g, this)
    }

    override fun imageUpdate(img: Image, infoflags: Int, x: Int, y: Int, w: Int, h: Int): Boolean {
        val incomplete = renderer?.isWorking ?: true
        return super.imageUpdate(img, infoflags, x, y, w, h) || incomplete
    }

    val renderer: R?
        get() {
            while (true) {
                val width = width
                val height = height
                if (width == 0 || height == 0) return null

                var renderer = rendererRef.get()
                if (renderer != null && renderer.width == width && renderer.height == height && !renderer.cancelled()) {
                    return renderer
                }

                val oldRenderer = renderer
                renderer = createRenderer(width, height)
                if (rendererRef.compareAndSet(oldRenderer, renderer)) {
                    oldRenderer?.shutdown()
                    if (!registeredMouseListener.getAndSet(true)) addMouseListener(this)
                    renderer!!.render()
                    return renderer
                }

                // Oops... lost the race, so make sure this renderer gets shutdown properly before we abandon it
                renderer!!.shutdown()
            }
        }

    protected abstract fun createRenderer(width: Int, height: Int): R
    val mouseLocationAsRelativePoint: Point2D.Double?
        get() = getMouseLocationAsRelativePoint(null)

    fun getMouseLocationAsRelativePoint(e: MouseEvent?): Point2D.Double? {
        val width = width
        val height = height
        if (width <= 0 || height <= 0) {
            // Panel dimensions unknown (hidden?)
            System.err.println("ImageRendererPanel: Panel dimensions unknown")
            return null
        }
        val mouseLoc = when (e) {
            null -> MouseInfo.getPointerInfo().location
            else -> e.locationOnScreen
        }
        // Mouse location unknown (moving it too fast?)
        if (mouseLoc == null) {
            println("ImageRendererPanel: Mouse location unknown")
            return null
        }
        val panelLoc = locationOnScreen
        val x = (mouseLoc.getX() - panelLoc.getX()) / width
        val y = (mouseLoc.getY() - panelLoc.getY()) / height
        if (x < 0 || x > 1 || y < 0 || y > 1) {
            // Mouse was somewhere else
            println("ImageRendererPanel: Mouse location out-of-bounds: ($x, $y)")
            return null
        }
        return Point2D.Double(x, y)
    }

    fun reset() {
        val newRenderer = createRenderer(width, height)
        val oldRenderer = rendererRef.getAndSet(newRenderer)
        oldRenderer?.shutdown()
        newRenderer!!.render()
    }

    override fun mouseClicked(e: MouseEvent) {}
    override fun mousePressed(e: MouseEvent) {}
    override fun mouseReleased(e: MouseEvent) {}
    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}
    fun interface ActionHandler {
        fun actionPerformed(e: ActionEvent?)
    }

    companion object {
        fun scaleToBounds(value: Double, min: Double, max: Double): Double {
            return value * max - value * min + min
        }

        // Glue code to make AbstractAction suck less by looking like a functional interface
        fun action(handler: ActionHandler): AbstractAction {
            return object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent) {
                    handler.actionPerformed(e)
                }
            }
        }
    }
}