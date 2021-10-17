package net.fwitz.math.main.binary.escape.root.newton

import net.fwitz.math.binary.complex.analysis.RiemannZeta
import net.fwitz.math.fractal.escape.newton.NewtonsMethod.newtonsMethod
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.NewtonsMethodDarkZero

object NewtonsMethodZeta {
    private val NAME = "zeta(z) (Newton's Method)"

    @JvmStatic
    fun main(args: Array<String>) = EscapeTimePlot.complex(NAME)
        .size(500, 500)
        .domainX(-20.0, 20.0)
        .domainY(-20.0, 20.0)
        .computeFn(newtonsMethod(f = RiemannZeta::zeta))
        .colorFn(NewtonsMethodDarkZero())
        .render()
}