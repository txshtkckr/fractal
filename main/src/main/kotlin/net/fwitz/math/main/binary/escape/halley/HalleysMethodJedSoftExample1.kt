package net.fwitz.math.main.binary.escape.halley

import net.fwitz.math.main.binary.complex.functions.examples.simple.JedSoftExample1

object HalleysMethodJedSoftExample1 {
    @JvmStatic
    fun main(args: Array<String>) = HalleysMethodPlot.render(
        name = JedSoftExample1.FN_NAME + " (Halley's Method)",
        f = JedSoftExample1::fn
    )
}