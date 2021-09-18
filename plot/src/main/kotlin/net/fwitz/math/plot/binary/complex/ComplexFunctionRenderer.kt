package net.fwitz.math.plot.binary.complex

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel
import net.fwitz.math.plot.binary.BinaryNumberFunctionRenderer
import net.fwitz.math.plot.renderer.filter.ValuesFilters

class ComplexFunctionRenderer<V>(
    panel: BinaryNumberFunctionPanel<Complex, V>,
    valueType: Class<V>,
    width: Int, height: Int,
    valuesFilters: ValuesFilters<V>
) : BinaryNumberFunctionRenderer<Complex, V>(panel, valueType, width, height, valuesFilters) {
    override fun value(x: Double, y: Double) = Complex(x, y)
}