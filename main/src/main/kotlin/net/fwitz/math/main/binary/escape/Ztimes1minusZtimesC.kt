package net.fwitz.math.main.binary.escape

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.binary.complex.Complex.Companion.real

object Ztimes1minusZtimesC : AbstractEscapeTimeMapPlot(iters = 1000) {
    override val title = "z(n+1) = c * z * (1 - z)  (Escape time)"
    override val pMin = -2.0
    override val pMax = 1.0
    override val qMin = -1.5
    override val qMax = 1.5

    override fun init(c: Complex) = real(0.5)
    override fun step(c: Complex, z: Complex) = c * (z - z.pow2)

    @JvmStatic
    fun main(args: Array<String>) = render()
}