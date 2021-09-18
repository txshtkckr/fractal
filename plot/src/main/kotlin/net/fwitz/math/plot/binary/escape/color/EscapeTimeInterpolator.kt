package net.fwitz.math.plot.binary.escape.color

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.fractal.escape.EscapeTimeResult
import net.fwitz.math.plot.renderer.palette.Interpolate
import net.fwitz.math.plot.renderer.palette.Palette
import java.awt.Color
import kotlin.math.floor
import kotlin.math.ln

class EscapeTimeInterpolator<T : BinaryNumber<T>>(
    power: Double,
    radius: Double,
    private val color0: Color,
    private val lookup: (EscapeTimeResult<T>, Int) -> Color
) : EscapeTimeColorFunction<T> {
    // nu = n - log(log(|z|) / log(N)) / log(P)
    // nu = n - log(log(|z|) * mN) * mP
    private val mN = 1 / ln(radius)
    private val mP = 1 / ln(power)

    /**
     *
     * @param power the largest power used during iteration, such as 2 for the standard Mandelbrot set
     * @param radius the bailout radius; 2 on the normal Mandelbrot set
     * @param palette the color palette to apply
     */
    constructor(power: Double, radius: Double, palette: Palette) : this(
        power,
        radius,
        palette[0],
        { _, n -> palette.indexExcluding0(n) }
    )

    override fun invoke(c: T, result: EscapeTimeResult<T>): Color {
        if (result.contained) {
            return color0
        }
        val iters: Int
        val mantissa: Double
        if (result.smoothing != null) {
            iters = result.iters
            mantissa = result.smoothing!!
        } else {
            val nu: Double = result.iters + 1 - ln(result.z.logabs * mN) * mP
            iters = floor(nu).toInt()
            mantissa = nu - iters
        }
        if (iters < 1) {
            return lookup(result, 1)
        }
        val c1: Color = lookup(result, iters)
        val c2: Color = lookup(result, iters + 1)
        return Interpolate.interpolate(c1, c2, mantissa)
    }
}