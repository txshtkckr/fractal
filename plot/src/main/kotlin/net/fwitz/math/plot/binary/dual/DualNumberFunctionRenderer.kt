package net.fwitz.math.plot.binary.dual

import net.fwitz.math.binary.dual.DualNumber
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel
import net.fwitz.math.plot.binary.BinaryNumberFunctionRenderer
import net.fwitz.math.plot.renderer.filter.ValuesFilters

class DualNumberFunctionRenderer<V>(
    panel: BinaryNumberFunctionPanel<DualNumber, V>, valueType: Class<V>,
    width: Int, height: Int,
    valuesFilters: ValuesFilters<V>
) : BinaryNumberFunctionRenderer<DualNumber, V>(panel, valueType, width, height, valuesFilters) {
    override fun value(x: Double, y: Double) = DualNumber(x, y)
}