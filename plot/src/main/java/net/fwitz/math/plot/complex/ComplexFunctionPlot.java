package net.fwitz.math.plot.complex;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.color.complex.domain.DomainColoringAdvanced;

public class ComplexFunctionPlot
        extends AbstractComplexFunctionPlot<ComplexFunctionPlot, ComplexFunctionPanel, Complex> {

    public ComplexFunctionPlot(String title) {
        super(title);
        colorFn(new DomainColoringAdvanced());
    }

    @Override
    protected ComplexFunctionPanel createPanel() {
        return new ComplexFunctionPanel(this);
    }
}
