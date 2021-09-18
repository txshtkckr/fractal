package net.fwitz.math.plot.binary.dual

import net.fwitz.math.binary.dual.DualNumber
import net.fwitz.math.calculus.Derivation
import net.fwitz.math.plot.renderer.filter.ValuesFilter
import net.fwitz.math.plot.renderer.filter.ValuesFilters

object DualNumberFunctionValuesFilters : ValuesFilters<DualNumber>(
    DualNumberValueFunctions.reOnly,
    DualNumberValueFunctions.imOnly,
    DualNumberValueFunctions.dz,
    DualNumberValueFunctions.dz2
)

private object DualNumberValueFunctions {
    fun mapAll(zv: Array<DualNumber>, f: (DualNumber) -> DualNumber) = Array(zv.size) { i -> f(zv[i]) }

    val reOnly = ValuesFilter("X only") { _, _, zv: Array<DualNumber> -> mapAll(zv) { it.y(0.0) } }
    val imOnly = ValuesFilter("Y only") { _, _, zv: Array<DualNumber> -> mapAll(zv) { it.x(0.0) } }
    val dz = ValuesFilter("dz/dx") { rv, _, zv: Array<DualNumber> -> derivative(rv, zv) }
    val dz2 = ValuesFilter("d²z/dx²") { rv, _, zv: Array<DualNumber> -> derivative(rv, derivative(rv, zv)) }

    private fun derivative(rv: DoubleArray, zv: Array<DualNumber>) = when {
        rv.size < 2 -> zv
        else -> Derivation.derivative(rv[1] - rv[0], zv)
    }
}
