package net.fwitz.math.main.binary.escape.root.laguerre

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.calculus.Derivation.derivative
import net.fwitz.math.fractal.escape.EscapeFunction
import net.fwitz.math.fractal.escape.newton.LaguerresMethod.Companion.laguerresMethod
import net.fwitz.math.plot.binary.escape.EscapeTimePlot
import net.fwitz.math.plot.binary.escape.color.NewtonsMethodDarkZero

object LaguerresMethodPlot {
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
            n: Double,
            f: (Complex) -> Complex
        ) {
            val df = derivative(f)
            val d2f = derivative(df)
            render(name, n, f, df, d2f)
        }

        fun render(
            name: String,
            n: Double,
            f: (Complex) -> Complex,
            df: (Complex) -> Complex
        ) {
            val d2f = derivative(df)
            render(name, n, f, df, d2f)
        }

        fun render(
            name: String,
            n: Double,
            f: (Complex) -> Complex,
            df: (Complex) -> Complex,
            d2f: (Complex) -> Complex
        ) = renderEscape(
            name = name,
            escapeFn = laguerresMethod(n, f, df, d2f)
        )

        fun zPowMinus1(power: Double) {
            val power2mp = power * (power - 1)
            render(
                name = "z^$power - 1 (Halley's Method)",
                n = power,
                f = { z -> z.pow(power) - 1 },
                df = { z -> z.pow(power - 1) * power },
                d2f = { z -> z.pow(power - 2) * power2mp }
            )
        }
}