package net.fwitz.math.plot.binary.escape.color

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.fractal.escape.EscapeTimeResult
import net.fwitz.math.plot.renderer.palette.Palette

class EscapeTimePaletteFunction<T : BinaryNumber<T>> private constructor(
    private val palette: Palette
) : EscapeTimeColorFunction<T> {
    companion object {
        fun <T : BinaryNumber<T>> escapeTime(palette: Palette) = EscapeTimePaletteFunction<T>(palette)
    }

    override fun invoke(c: T, result: EscapeTimeResult<T>) = when {
        result.escaped -> palette.indexExcluding0(result.iters)
        else -> palette[0]
    }
}