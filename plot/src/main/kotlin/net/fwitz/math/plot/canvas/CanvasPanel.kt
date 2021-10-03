package net.fwitz.math.plot.canvas

import net.fwitz.math.plot.renderer.ImageRendererPanel
import java.awt.Color
import java.awt.event.MouseEvent
import javax.swing.KeyStroke
import kotlin.system.exitProcess

class CanvasPanel(
    val drawStepFn: (CanvasRenderer) -> Unit,
    val backgroundColor: Color = Color.DARK_GRAY,
    val onClick: (CanvasPanel, MouseEvent) -> Unit = { _, _ -> }
) : ImageRendererPanel<CanvasRenderer>() {
    init {
        addListeners()
    }

    override fun createRenderer(width: Int, height: Int): CanvasRenderer {
        return CanvasRenderer(this, width, height, backgroundColor)
    }

    private fun addListeners() {
        val keyMap = inputMap
        keyMap.put(KeyStroke.getKeyStroke('q'), "quit")
        keyMap.put(KeyStroke.getKeyStroke('\u001B'), "quit")

        val handlers = actionMap
        handlers.put("quit", action {
            renderer?.shutdown()
            exitProcess(0)
        })
    }

    override fun mousePressed(e: MouseEvent) = onClick(this, e)
}