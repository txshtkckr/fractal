package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object Z4p2Z3p3Z2p4Zp1 {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("f(z) = z^4 + 2z^3 + 3z^2 + 4z + 1")
        .domainX(-3.5, 3.5)
        .domainX(-2.5, 2.5)
        .size(700, 500)
        .computeFn(::f)
        .render()

    val n = 4.0

    fun f(z: Complex): Complex {
        val z2 = z.pow2
        val z3 = z2 * z
        val z4 = z2 * z2
        return z4 + z3 * 2 + z2 * 3 + z * 4 + 1
    }
    fun df(z: Complex): Complex {
        val z2 = z.pow2
        val z3 = z2 * z
        return z3 * 4 + z2 * 6 + z * 6 + 4
    }
    fun d2f(z: Complex) = z * (z + 1) * 12 + 6
}