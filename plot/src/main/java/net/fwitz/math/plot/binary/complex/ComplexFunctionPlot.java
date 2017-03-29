package net.fwitz.math.plot.binary.complex;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.binary.BinaryNumberFunctionPlot;
import net.fwitz.math.plot.binary.complex.color.DomainColoringAdvanced;

public class ComplexFunctionPlot extends BinaryNumberFunctionPlot<
        ComplexFunctionPlot,
        ComplexFunctionPanel,
        Complex,
        Complex> {

    public ComplexFunctionPlot(String title) {
        super(title);
        colorFn(new DomainColoringAdvanced());
    }

    @Override
    public Complex value(double x, double y) {
        return Complex.complex(x, y);
    }

    @Override
    protected ComplexFunctionPanel createPanel() {
        return new ComplexFunctionPanel(this);
    }
}
