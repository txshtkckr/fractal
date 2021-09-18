package net.fwitz.math.plot.renderer.palette

import java.awt.Color

// rrrgggbb
object PaletteVGA8bitRGB : Palette {
    private val BITS2 = intArrayOf(
        0x00, 0x48, 0x90, 0xFF
    )

    private val BITS3 = intArrayOf(
        0x00, 0x24, 0x48, 0x6C,
        0x90, 0xB4, 0xD8, 0xFF
    )

    private val COLORS = Array(256) { x ->
        val r = BITS3[x shr 5 and 7]
        val g = BITS3[x shr 2 and 7]
        val b = BITS2[x and 3]
        Color(r, g, b)
    }

    override val size = COLORS.size

    override operator fun get(i: Int) = COLORS[i]
}