package net.fwitz.math.plot.binary.split

import net.fwitz.math.binary.split.SplitComplex.Companion.splitComplex
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel
import net.fwitz.math.plot.renderer.filter.ValuesFilters
import net.fwitz.math.plot.binary.BinaryNumberFunctionRenderer
import net.fwitz.math.binary.split.SplitComplex

class SplitComplexFunctionRenderer<V>(
    panel: BinaryNumberFunctionPanel<SplitComplex, V>, valueType: Class<V>,
    width: Int, height: Int,
    valuesFilters: ValuesFilters<V>
) : BinaryNumberFunctionRenderer<SplitComplex, V>(panel, valueType, width, height, valuesFilters) {
    override fun value(x: Double, y: Double): SplitComplex {
        return splitComplex(x, y)
    }
}