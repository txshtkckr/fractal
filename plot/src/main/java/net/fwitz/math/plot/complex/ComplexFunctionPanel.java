package net.fwitz.math.plot.complex;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.color.filter.ComplexFunctionValuesFilters;

public class ComplexFunctionPanel extends AbstractComplexFunctionPanel<Complex> {
    ComplexFunctionPanel(ComplexFunctionPlot plot) {
        super(Complex.class, plot);
    }

    @Override
    protected ComplexFunctionRenderer<Complex> createRenderer(int width, int height) {
        return new ComplexFunctionRenderer<>(this, valueType, width, height, ComplexFunctionValuesFilters.getInstance());
    }
}
