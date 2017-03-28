package net.fwitz.math.plot.complex.split;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.complex.color.DomainColoringAdvanced;

public class SplitComplexFunctionPlot
        extends AbstractSplitComplexFunctionPlot<SplitComplexFunctionPlot, SplitComplexFunctionPanel, SplitComplex> {

    public SplitComplexFunctionPlot(String title) {
        super(title);
        colorFn(new DomainColoringAdvanced());
    }

    @Override
    protected SplitComplexFunctionPanel createPanel() {
        return new SplitComplexFunctionPanel(this);
    }
}

