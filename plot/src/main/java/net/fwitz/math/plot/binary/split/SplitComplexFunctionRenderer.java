package net.fwitz.math.plot.binary.split;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel;
import net.fwitz.math.plot.binary.BinaryNumberFunctionRenderer;
import net.fwitz.math.plot.renderer.filter.ValuesFilters;

import static net.fwitz.math.complex.SplitComplex.splitComplex;

public class SplitComplexFunctionRenderer<V> extends BinaryNumberFunctionRenderer<SplitComplex, V> {
    public SplitComplexFunctionRenderer(BinaryNumberFunctionPanel<SplitComplex, V> panel, Class<V> valueType,
                                        int width, int height,
                                        ValuesFilters<V> valuesFilters) {
        super(panel, valueType, width, height, valuesFilters);
    }

    @Override
    protected SplitComplex value(double x, double y) {
        return splitComplex(x, y);
    }
}
