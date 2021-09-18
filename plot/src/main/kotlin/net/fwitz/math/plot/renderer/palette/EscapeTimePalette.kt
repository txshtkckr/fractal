package net.fwitz.math.plot.renderer.palette

import java.awt.Color

object EscapeTimePalette : Palette {
    private val COLORS: List<Color> = run {
        val colors = ArrayList<Color>(256)

        colors.add(Color.BLACK)

        var b = 0x40
        while (b < 0x100) {
            colors.add(Color(0x00, 0x00, b))
            b += 0x10
        }

        colors.add(Color(0, 0, 0xFF))

        var rg = 0
        while (rg < 0x100) {
            colors.add(Color(rg, rg, 0xFF))
            rg += 0x10
        }

        colors.add(Color.WHITE)

        b = 0xF0
        while (b >= 0) {
            colors.add(Color(0xFF, 0xFF, b))
            b -= 0x10
        }

        var g = 0xF0
        while (g >= 0) {
            colors.add(Color(0xFF, g, 0x00))
            g -= 0x10
        }

        var r = 0xF0
        b = 0x04

        while (r > 0) {
            colors.add(Color(r, 0x00, b))
            r -= 0x10
            b += 0x04
        }

        colors
    }

    override val size = COLORS.size

    override operator fun get(index: Int) = COLORS[index]
}