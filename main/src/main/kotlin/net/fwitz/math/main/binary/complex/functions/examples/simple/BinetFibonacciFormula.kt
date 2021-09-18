package net.fwitz.math.main.binary.complex.functions.examples.simple

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.binary.complex.Complex.Companion.real
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot
import net.fwitz.math.plot.binary.complex.color.DomainColoringAdvanced
import java.util.function.Function
import kotlin.math.sqrt

/**
 * `Fib(z) = [ Phi^z - (-phi)^z ] / sqrt(5)`
 */
object BinetFibonacciFormula {
    private val SQRT_5 = sqrt(5.0)
    private val PHI = (SQRT_5 + 1) * 0.5
    private val C_PHI: Complex = real(PHI)
    private val C_Mphi: Complex = real(1 - PHI)

    @JvmStatic
    fun main(args: Array<String>) = ComplexFunctionPlot("Fib(z)")
        .computeFn { z -> (C_PHI.pow(z) - C_Mphi.pow(z)) / SQRT_5 }
        .colorFn(DomainColoringAdvanced())
        .render()
}