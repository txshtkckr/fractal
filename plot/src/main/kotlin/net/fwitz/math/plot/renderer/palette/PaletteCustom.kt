package net.fwitz.math.plot.renderer.palette

import java.awt.Color

class PaletteCustom(vararg colors: Color) : Palette {
    private val colors = colors.toList()
    override val size = colors.size
    override fun get(index: Int) = colors[index]
}