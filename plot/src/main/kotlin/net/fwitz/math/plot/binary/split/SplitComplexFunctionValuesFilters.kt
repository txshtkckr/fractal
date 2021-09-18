package net.fwitz.math.plot.binary.split

import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.calculus.Derivation
import net.fwitz.math.plot.renderer.filter.ValuesFilter
import net.fwitz.math.plot.renderer.filter.ValuesFilters

object SplitComplexFunctionValuesFilters : ValuesFilters<SplitComplex>(
    SplitComplexValueFunctions.reOnly,
    SplitComplexValueFunctions.imOnly,
    SplitComplexValueFunctions.dz,
    SplitComplexValueFunctions.dz2
)

private object SplitComplexValueFunctions {
    fun mapAll(zv: Array<SplitComplex>, f: (SplitComplex) -> SplitComplex) = Array(zv.size) { i -> f(zv[i]) }

    val reOnly = ValuesFilter("X only") { _, _, zv: Array<SplitComplex> -> mapAll(zv) { it.y(0.0) } }
    val imOnly = ValuesFilter("Y only") { _, _, zv: Array<SplitComplex> -> mapAll(zv) { it.x(0.0) } }
    val dz = ValuesFilter("dz/dx") { rv, _, zv: Array<SplitComplex> -> derivative(rv, zv) }
    val dz2 = ValuesFilter("d²z/dx²") { rv, _, zv: Array<SplitComplex> -> derivative(rv, derivative(rv, zv)) }

    private fun derivative(rv: DoubleArray, zv: Array<SplitComplex>) = when {
        rv.size < 2 -> zv
        else -> Derivation.derivative(rv[1] - rv[0], zv)
    }
}