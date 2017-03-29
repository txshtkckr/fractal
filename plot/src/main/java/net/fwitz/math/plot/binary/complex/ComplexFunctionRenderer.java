package net.fwitz.math.plot.binary.complex;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel;
import net.fwitz.math.plot.binary.BinaryNumberFunctionRenderer;
import net.fwitz.math.plot.renderer.filter.ValuesFilters;

import static net.fwitz.math.complex.Complex.complex;

public class ComplexFunctionRenderer<V> extends BinaryNumberFunctionRenderer<Complex, V> {
    public ComplexFunctionRenderer(BinaryNumberFunctionPanel<Complex, V> panel, Class<V> valueType,
                                   int width, int height,
                                   ValuesFilters<V> valuesFilters) {
        super(panel, valueType, width, height, valuesFilters);
    }

    @Override
    protected Complex value(double x, double y) {
        return complex(x, y);
    }
}
