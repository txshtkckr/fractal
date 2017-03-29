package net.fwitz.math.plot.binary.complex;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel;

public class ComplexFunctionPanel extends BinaryNumberFunctionPanel<Complex, Complex> {
    ComplexFunctionPanel(ComplexFunctionPlot plot) {
        super(Complex.class, Complex.class, plot);
    }

    @Override
    protected ComplexFunctionRenderer<Complex> createRenderer(int width, int height) {
        return new ComplexFunctionRenderer<>(
                this, valueType,
                width, height,
                ComplexFunctionValuesFilters.getInstance());
    }
}
