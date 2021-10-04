package net.fwitz.math.main.binary.escape

import net.fwitz.math.binary.complex.Complex

/**
 * The "Tricorn" or "Mandelbar" set is generated in the same way as the standard Mandelbrot set, except that
 * the iterations use the complex conjugate of the square instead of the square.
 */
object TricornPlot : AbstractEscapeTimeMapPlot(iters = 1000) {
    override val title = "z(n) = conj((z(n-1)^2) + c  (Escape time)"
    override val pMin = -2.0
    override val pMax = 1.0
    override val qMin = -1.5
    override val qMax = 1.5

    override fun init(c: Complex) = Complex.ZERO
    override fun step(c: Complex, z: Complex) = z.pow2.conjugate + c

    @JvmStatic
    fun main(args: Array<String>) = render()
}