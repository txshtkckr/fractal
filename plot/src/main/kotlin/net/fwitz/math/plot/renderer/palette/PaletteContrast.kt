package net.fwitz.math.plot.renderer.palette

import java.awt.Color

object PaletteContrast : Palette {
    private val COLORS = arrayOf(
        Color(0x00, 0x00, 0x00),
        Color(0xC0, 0x00, 0x60),
        Color(0x00, 0xD0, 0x00),
        Color(0x60, 0x40, 0xD0),
        Color(0xB0, 0xA0, 0x00),
        Color(0x00, 0x60, 0xD0),
        Color(0xD0, 0x00, 0x00),
        Color(0x00, 0xC0, 0x70),
        Color(0x90, 0x58, 0xC0),
        Color(0xC0, 0xC0, 0x00),
        Color(0x20, 0x20, 0xFF),
        Color(0xC0, 0x40, 0x00),
        Color(0x00, 0xC0, 0xC0),
        Color(0x90, 0x00, 0xA0),
        Color(0x80, 0xC0, 0x00),
        Color(0x40, 0x30, 0xE0),
        Color(0xC0, 0x80, 0x00),
        Color(0x00, 0x90, 0xC8),
    )

    override val size = COLORS.size

    override fun get(index: Int) = COLORS[index]
}