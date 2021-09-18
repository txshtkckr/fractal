package net.fwitz.math.plot.binary.complex

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.calculus.Derivation
import net.fwitz.math.plot.renderer.filter.ValuesFilter
import net.fwitz.math.plot.renderer.filter.ValuesFilters

object ComplexFunctionValuesFilters : ValuesFilters<Complex>(
    ComplexValueFunctions.reOnly,
    ComplexValueFunctions.imOnly,
    ComplexValueFunctions.dz,
    ComplexValueFunctions.dz2
)

private object ComplexValueFunctions {
    fun mapAll(zv: Array<Complex>, f: (Complex) -> Complex) = Array(zv.size) { i -> f(zv[i]) }

    val reOnly = ValuesFilter("Real only") { _, _, zv: Array<Complex> -> mapAll(zv) { it.y(0.0) } }
    val imOnly = ValuesFilter("Imaginary only") { _, _, zv: Array<Complex> -> mapAll(zv) { it.x(0.0) } }
    val dz = ValuesFilter("dz/dx") { rv, _, zv: Array<Complex> -> derivative(rv, zv) }
    val dz2 = ValuesFilter("d²z/dx²") { rv, _, zv: Array<Complex> -> derivative(rv, derivative(rv, zv)) }

    private fun derivative(rv: DoubleArray, zv: Array<Complex>) = when {
        rv.size < 2 -> zv
        else -> Derivation.derivative(rv[1] - rv[0], zv)
    }
}