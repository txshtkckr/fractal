package net.fwitz.math.main.binary.escape.newton

import net.fwitz.math.main.binary.complex.functions.examples.simple.JedSoftExample1

object NewtonsMethodJedSoftExample1 {
    @JvmStatic
    fun main(args: Array<String>) = NewtonsMethodPlot.render(
        name = JedSoftExample1.FN_NAME + " (Newton's Method)",
        f = JedSoftExample1::fn
    )
}