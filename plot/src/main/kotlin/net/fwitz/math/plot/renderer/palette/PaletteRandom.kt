package net.fwitz.math.plot.renderer.palette

import net.fwitz.math.numth.numbers.Randomizer
import java.awt.Color

class PaletteRandom(override val size: Int = 256): Palette {
    val colors = Array<Color>(size) {
        when (it) {
            0 -> Color.BLACK
            else -> Randomizer.randomColor()
        }
    }
    override fun get(index: Int) = colors[index]
}