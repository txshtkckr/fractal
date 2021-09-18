package net.fwitz.math.main.binary.escape.halley

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.calculus.Derivation.derivative
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.fractal.escape.newton.HalleysMethod.Companion.halleysMethod
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.NewtonsMethodDarkZero

object HalleysMethodPlot {
    fun renderEscape(name: String, escapeFn: EscapeFunction<Complex>) {
        EscapeTimePlot.complex(name)
            .computeFn(escapeFn)
            .domainX(-3.5, 3.5)
            .domainY(-2.5, 2.5)
            .size(700, 500)
            .colorFn(NewtonsMethodDarkZero())
            .render()
    }

    fun render(
        name: String,
        f: (Complex) -> Complex
    ) {
        val df = derivative(f)
        val d2f = derivative(df)
        render(name, f, df, d2f)
    }

    fun render(
        name: String,
        f: (Complex) -> Complex,
        df: (Complex) -> Complex
    ) {
        val d2f = derivative(df)
        render(name, f, df, d2f)
    }

    fun render(
        name: String,
        f: (Complex) -> Complex,
        df: (Complex) -> Complex,
        d2f: (Complex) -> Complex
    ) = renderEscape(
        name = name,
        escapeFn = halleysMethod(f, df, d2f)
    )

    fun zPowMinus1(power: Double) {
        val power2mp = power * (power - 1)
        render(
            name = "z^$power - 1 (Halley's Method)",
            f = { z -> z.pow(power) - 1 },
            df = { z -> z.pow(power - 1) * power },
            d2f = { z -> z.pow(power - 2) * power2mp }
        )
    }

    fun zPowMinus1(power: Complex) {
        val power2mp: Complex = power * (power - 1)
        render("z^$power - 1",
            { z -> z.pow(power) - 1 },
            { z -> z.pow(power - 1) * power },
            { z -> z.pow(power - 2) * power2mp })
    }
}