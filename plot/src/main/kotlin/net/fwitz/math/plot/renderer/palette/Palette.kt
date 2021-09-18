package net.fwitz.math.plot.renderer.palette

import java.awt.Color
import kotlin.math.floor

interface Palette {
    /** The number of colors included in this palette.  */
    val size: Int

    /**
     * Selects a color by index, without wrapping.
     *
     * @param index the color index, which must be in the range `[0, size)`; that is, it must be non-negative
     * and less than [.size].
     * @return the selected color
     * @throws IndexOutOfBoundsException if the supplied index is out of range
     */
    operator fun get(index: Int): Color

    /**
     * Returns a color from the palette, using all available colors and wrapping as needed.
     *
     * @param index the (non-negative) color index
     * @return the corresponding color
     */
    fun indexAny(index: Int) = get(index % size)

    /**
     * Returns a color from the palette, using all available colors except the one at index `0` and wrapping
     * as needed.
     *
     * @param index the (non-negative) color index
     * @return the corresponding color
     */
    fun indexExcluding0(index: Int): Color {
        val max = size - 1
        val n = index % max
        return get(if (n != 0) n else max)
    }

    /**
     * Returns a color constructed from the palette by interpolating between the color that would be selected
     * by [.indexAny] using [floor(x)][Math.floor] and the next color in the palette,
     * wrapping back around to index `0` if necessary.
     *
     * @param x the color index, with the integer portion representing the first color's index as per
     * [.index] and the mantissa indicating the amount of the next color that should be mixed in
     * @return the interpolated color value
     */
    fun interpolateAny(x: Double): Color {
        var n = floor(x).toInt()
        val mantissa = x - n
        n %= size
        val nPlus1 = n + 1
        return Interpolate.interpolate(
            get(n),
            get(if (nPlus1 != size) nPlus1 else 0),
            mantissa
        )
    }

    /**
     * Returns a color constructed from the palette by interpolating between the color that would be selected
     * by [.indexExcluding0] (int)} using [floor(x)][Math.floor] and the next color
     * in the palette, wrapping back around to index `1` if necessary.
     *
     * @param x the color index, with the integer portion representing the first color's index as per
     * [.index] and the mantissa indicating the amount of the next color that should be mixed in
     * @return the interpolated color value
     */
    fun interpolateExcluding0(x: Double): Color? {
        val max = size - 1
        var n = floor(x).toInt()
        val mantissa = x - n
        n %= max
        return Interpolate.interpolate(
            get(if (n > 0) n else max),
            get(n + 1),
            mantissa
        )
    }
}