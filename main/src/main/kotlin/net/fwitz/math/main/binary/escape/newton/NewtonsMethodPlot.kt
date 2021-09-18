package net.fwitz.math.main.binary.escape.newton

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.fractal.escape.newton.NewtonsMethod
import net.fwitz.math.fractal.escape.newton.NewtonsMethod.newtonsMethod
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.NewtonsMethodDarkZero
import java.util.function.Function

object NewtonsMethodPlot {
    fun renderEscape(name: String, escapeFn: EscapeFunction<Complex>) {
        EscapeTimePlot.complex(name)
            .computeFn(escapeFn)
            .domainX(-3.5, 3.5)
            .domainY(-2.5, 2.5)
            .size(700, 500)
            .colorFn(NewtonsMethodDarkZero())
            .render()
    }

    fun render(name: String, f: (Complex) -> Complex) {
        renderEscape(name, newtonsMethod(f))
    }

    fun render(name: String, f: (Complex) -> Complex, df: (Complex) -> Complex) {
        renderEscape(name, newtonsMethod(f, df))
    }

    fun zPowMinus1(power: Double) = render(
        name = "z^$power - 1 (Newton's Method)",
        f = { z -> z.pow(power) - 1 },
        df = { z -> z.pow(power - 1) * power }
    )

    fun zPowMinus1(power: Complex) = render(
        name = "z^$power - 1",
        f = { z -> z.pow(power) - 1 },
        df = { z -> z.pow(power - 1) * power }
    )
}