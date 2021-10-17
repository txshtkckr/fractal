package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot

object Z8p15Z4m16 {
    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("f(z) = z^8 + 15z^4 - 16")
        .domainX(-3.5, 3.5)
        .domainX(-2.5, 2.5)
        .size(700, 500)
        .computeFn(::f)
        .render()

    val n = 8.0
    fun f(z: Complex) = z.pow(8.0) + (z.pow(4.0) * 15) - 16
    fun df(z: Complex) = z.pow(7.0) * 8 + z.pow3 * 60
    fun d2f(z: Complex) = z.pow(6.0).times(56) + z.pow2.times(180)
}