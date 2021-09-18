package net.fwitz.math.plot.renderer.palette

import java.awt.Color

object PaletteVGA256 : Palette {
    // No idea how reliable it is, but used https://upload.wikimedia.org/wikipedia/commons/4/49/VGA_palette.svg
    // as my source for this.
    private val COLORS = arrayOf(
        Color(0x00, 0x00, 0x00),
        Color(0x00, 0x00, 0xAA),
        Color(0x00, 0xAA, 0x00),
        Color(0x00, 0xAA, 0xAA),
        Color(0xAA, 0x00, 0x00),
        Color(0xAA, 0x00, 0xAA),
        Color(0xAA, 0x55, 0x00),
        Color(0xAA, 0xAA, 0xAA),
        Color(0x55, 0x55, 0x55),
        Color(0x55, 0x55, 0xFF),
        Color(0x55, 0xFF, 0x55),
        Color(0x55, 0xFF, 0xFF),
        Color(0xFF, 0x55, 0x55),
        Color(0xFF, 0x55, 0xFF),
        Color(0xFF, 0xFF, 0x55),
        Color(0xFF, 0xFF, 0xFF),
        Color(0x00, 0x00, 0x00),
        Color(0x10, 0x10, 0x10),
        Color(0x20, 0x20, 0x20),
        Color(0x35, 0x35, 0x35),
        Color(0x45, 0x45, 0x45),
        Color(0x55, 0x55, 0x55),
        Color(0x65, 0x65, 0x65),
        Color(0x75, 0x75, 0x75),
        Color(0x8A, 0x8A, 0x8A),
        Color(0x9A, 0x9A, 0x9A),
        Color(0xAA, 0xAA, 0xAA),
        Color(0xBA, 0xBA, 0xBA),
        Color(0xCA, 0xCA, 0xCA),
        Color(0xDF, 0xDF, 0xDF),
        Color(0xEF, 0xEF, 0xEF),
        Color(0xFF, 0xFF, 0xFF),
        Color(0x00, 0x00, 0xFF),
        Color(0x41, 0x00, 0xFF),
        Color(0x82, 0x00, 0xFF),
        Color(0xBE, 0x00, 0xFF),
        Color(0xFF, 0x00, 0xFF),
        Color(0xFF, 0x00, 0xBE),
        Color(0xFF, 0x00, 0x82),
        Color(0xFF, 0x00, 0x41),
        Color(0xFF, 0x00, 0x00),
        Color(0xFF, 0x41, 0x00),
        Color(0xFF, 0x82, 0x00),
        Color(0xFF, 0xBE, 0x00),
        Color(0xFF, 0xFF, 0x00),
        Color(0xBE, 0xFF, 0x00),
        Color(0x82, 0xFF, 0x00),
        Color(0x41, 0xFF, 0x00),
        Color(0x00, 0xFF, 0x00),
        Color(0x00, 0xFF, 0x41),
        Color(0x00, 0xFF, 0x82),
        Color(0x00, 0xFF, 0xBE),
        Color(0x00, 0xFF, 0xFF),
        Color(0x00, 0xBE, 0xFF),
        Color(0x00, 0x82, 0xFF),
        Color(0x00, 0x41, 0xFF),
        Color(0x82, 0x82, 0xFF),
        Color(0x9E, 0x82, 0xFF),
        Color(0xBE, 0x82, 0xFF),
        Color(0xDF, 0x82, 0xFF),
        Color(0xFF, 0x82, 0xFF),
        Color(0xFF, 0x82, 0xDF),
        Color(0xFF, 0x82, 0xBE),
        Color(0xFF, 0x82, 0x9E),
        Color(0xFF, 0x82, 0x82),
        Color(0xFF, 0x9E, 0x82),
        Color(0xFF, 0xBE, 0x82),
        Color(0xFF, 0xDF, 0x82),
        Color(0xFF, 0xFF, 0x82),
        Color(0xDF, 0xFF, 0x82),
        Color(0xBE, 0xFF, 0x82),
        Color(0x9E, 0xFF, 0x82),
        Color(0x82, 0xFF, 0x82),
        Color(0x82, 0xFF, 0x9E),
        Color(0x82, 0xFF, 0xBE),
        Color(0x82, 0xFF, 0xDF),
        Color(0x82, 0xFF, 0xFF),
        Color(0x82, 0xDF, 0xFF),
        Color(0x82, 0xBE, 0xFF),
        Color(0x82, 0x9E, 0xFF),
        Color(0xBA, 0xBA, 0xFF),
        Color(0xCA, 0xBA, 0xFF),
        Color(0xDF, 0xBA, 0xFF),
        Color(0xEF, 0xBA, 0xFF),
        Color(0xFF, 0xBA, 0xFF),
        Color(0xFF, 0xBA, 0xEF),
        Color(0xFF, 0xBA, 0xDF),
        Color(0xFF, 0xBA, 0xCA),
        Color(0xFF, 0xBA, 0xBA),
        Color(0xFF, 0xCA, 0xBA),
        Color(0xFF, 0xDF, 0xBA),
        Color(0xFF, 0xEF, 0xBA),
        Color(0xFF, 0xFF, 0xBA),
        Color(0xEF, 0xFF, 0xBA),
        Color(0xDF, 0xFF, 0xBA),
        Color(0xCA, 0xFF, 0xBA),
        Color(0xBA, 0xFF, 0xBA),
        Color(0xBA, 0xFF, 0xCA),
        Color(0xBA, 0xFF, 0xDF),
        Color(0xBA, 0xFF, 0xEF),
        Color(0xBA, 0xFF, 0xFF),
        Color(0xBA, 0xEF, 0xFF),
        Color(0xBA, 0xDF, 0xFF),
        Color(0xBA, 0xCA, 0xFF),
        Color(0x00, 0x00, 0x71),
        Color(0x1C, 0x00, 0x71),
        Color(0x39, 0x00, 0x71),
        Color(0x55, 0x00, 0x71),
        Color(0x71, 0x00, 0x71),
        Color(0x71, 0x00, 0x55),
        Color(0x71, 0x00, 0x39),
        Color(0x71, 0x00, 0x1C),
        Color(0x71, 0x00, 0x00),
        Color(0x71, 0x1C, 0x00),
        Color(0x71, 0x39, 0x00),
        Color(0x71, 0x55, 0x00),
        Color(0x71, 0x71, 0x00),
        Color(0x55, 0x71, 0x00),
        Color(0x39, 0x71, 0x00),
        Color(0x1C, 0x71, 0x00),
        Color(0x00, 0x71, 0x00),
        Color(0x00, 0x71, 0x1C),
        Color(0x00, 0x71, 0x39),
        Color(0x00, 0x71, 0x55),
        Color(0x00, 0x71, 0x71),
        Color(0x00, 0x55, 0x71),
        Color(0x00, 0x39, 0x71),
        Color(0x00, 0x1C, 0x71),
        Color(0x39, 0x39, 0x71),
        Color(0x45, 0x39, 0x71),
        Color(0x55, 0x39, 0x71),
        Color(0x61, 0x39, 0x71),
        Color(0x71, 0x39, 0x71),
        Color(0x71, 0x39, 0x61),
        Color(0x71, 0x39, 0x55),
        Color(0x71, 0x39, 0x45),
        Color(0x71, 0x39, 0x39),
        Color(0x71, 0x45, 0x39),
        Color(0x71, 0x55, 0x39),
        Color(0x71, 0x61, 0x39),
        Color(0x71, 0x71, 0x39),
        Color(0x61, 0x71, 0x39),
        Color(0x55, 0x71, 0x39),
        Color(0x45, 0x71, 0x39),
        Color(0x39, 0x71, 0x39),
        Color(0x39, 0x71, 0x45),
        Color(0x39, 0x71, 0x55),
        Color(0x39, 0x71, 0x61),
        Color(0x39, 0x71, 0x71),
        Color(0x39, 0x61, 0x71),
        Color(0x39, 0x55, 0x71),
        Color(0x39, 0x45, 0x71),
        Color(0x51, 0x51, 0x71),
        Color(0x59, 0x51, 0x71),
        Color(0x61, 0x51, 0x71),
        Color(0x69, 0x51, 0x71),
        Color(0x71, 0x51, 0x71),
        Color(0x71, 0x51, 0x69),
        Color(0x71, 0x51, 0x61),
        Color(0x71, 0x51, 0x59),
        Color(0x71, 0x51, 0x51),
        Color(0x71, 0x59, 0x51),
        Color(0x71, 0x61, 0x51),
        Color(0x71, 0x69, 0x51),
        Color(0x71, 0x71, 0x51),
        Color(0x69, 0x71, 0x51),
        Color(0x61, 0x71, 0x51),
        Color(0x59, 0x71, 0x51),
        Color(0x51, 0x71, 0x51),
        Color(0x51, 0x71, 0x59),
        Color(0x51, 0x71, 0x61),
        Color(0x51, 0x71, 0x69),
        Color(0x51, 0x71, 0x71),
        Color(0x51, 0x69, 0x71),
        Color(0x51, 0x61, 0x71),
        Color(0x51, 0x59, 0x71),
        Color(0x00, 0x00, 0x41),
        Color(0x10, 0x00, 0x41),
        Color(0x20, 0x00, 0x41),
        Color(0x31, 0x00, 0x41),
        Color(0x41, 0x00, 0x41),
        Color(0x41, 0x00, 0x31),
        Color(0x41, 0x00, 0x20),
        Color(0x41, 0x00, 0x10),
        Color(0x41, 0x00, 0x00),
        Color(0x41, 0x10, 0x00),
        Color(0x41, 0x20, 0x00),
        Color(0x41, 0x31, 0x00),
        Color(0x41, 0x41, 0x00),
        Color(0x31, 0x41, 0x00),
        Color(0x20, 0x41, 0x00),
        Color(0x10, 0x41, 0x00),
        Color(0x00, 0x41, 0x00),
        Color(0x00, 0x41, 0x10),
        Color(0x00, 0x41, 0x20),
        Color(0x00, 0x41, 0x31),
        Color(0x00, 0x41, 0x41),
        Color(0x00, 0x31, 0x41),
        Color(0x00, 0x20, 0x41),
        Color(0x00, 0x10, 0x41),
        Color(0x20, 0x20, 0x41),
        Color(0x28, 0x20, 0x41),
        Color(0x31, 0x20, 0x41),
        Color(0x39, 0x20, 0x41),
        Color(0x41, 0x20, 0x41),
        Color(0x41, 0x20, 0x39),
        Color(0x41, 0x20, 0x31),
        Color(0x41, 0x20, 0x28),
        Color(0x41, 0x20, 0x20),
        Color(0x41, 0x28, 0x20),
        Color(0x41, 0x31, 0x20),
        Color(0x41, 0x39, 0x20),
        Color(0x41, 0x41, 0x20),
        Color(0x39, 0x41, 0x20),
        Color(0x31, 0x41, 0x20),
        Color(0x28, 0x41, 0x20),
        Color(0x20, 0x41, 0x20),
        Color(0x20, 0x41, 0x28),
        Color(0x20, 0x41, 0x31),
        Color(0x20, 0x41, 0x39),
        Color(0x20, 0x41, 0x41),
        Color(0x20, 0x39, 0x41),
        Color(0x20, 0x31, 0x41),
        Color(0x20, 0x28, 0x41),
        Color(0x2D, 0x2D, 0x41),
        Color(0x31, 0x2D, 0x41),
        Color(0x35, 0x2D, 0x41),
        Color(0x3D, 0x2D, 0x41),
        Color(0x41, 0x2D, 0x41),
        Color(0x41, 0x2D, 0x3D),
        Color(0x41, 0x2D, 0x35),
        Color(0x41, 0x2D, 0x31),
        Color(0x41, 0x2D, 0x2D),
        Color(0x41, 0x31, 0x2D),
        Color(0x41, 0x35, 0x2D),
        Color(0x41, 0x3D, 0x2D),
        Color(0x41, 0x41, 0x2D),
        Color(0x3D, 0x41, 0x2D),
        Color(0x35, 0x41, 0x2D),
        Color(0x31, 0x41, 0x2D),
        Color(0x2D, 0x41, 0x2D),
        Color(0x2D, 0x41, 0x31),
        Color(0x2D, 0x41, 0x35),
        Color(0x2D, 0x41, 0x3D),
        Color(0x2D, 0x41, 0x41),
        Color(0x2D, 0x3D, 0x41),
        Color(0x2D, 0x35, 0x41),
        Color(0x2D, 0x31, 0x41),
        Color(0x00, 0x00, 0x00),
        Color(0x00, 0x00, 0x00),
        Color(0x00, 0x00, 0x00),
        Color(0x00, 0x00, 0x00),
        Color(0x00, 0x00, 0x00),
        Color(0x00, 0x00, 0x00),
        Color(0x00, 0x00, 0x00),
        Color(0x00, 0x00, 0x00)
    )

    override val size = COLORS.size
    override operator fun get(index: Int) = COLORS[index]

    object WithoutLast8 : Palette {
        override val size = COLORS.size - 8

        override operator fun get(index: Int) = when {
            index < size -> COLORS[index]
            else -> throw ArrayIndexOutOfBoundsException(index)
        }
    }
}