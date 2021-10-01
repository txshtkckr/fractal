package net.fwitz.math.main.binary.escape.root.newton

import net.fwitz.math.main.binary.complex.functions.examples.simple.WikipediaExample

object NewtonsMethodWikipediaExample {
    @JvmStatic
    fun main(args: Array<String>) = NewtonsMethodPlot.render(
        name = WikipediaExample.FN_NAME + " (Newton's Method)",
        f = WikipediaExample::fn
    )
}