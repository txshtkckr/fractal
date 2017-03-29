package net.fwitz.math.plot.complex.split;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.complex.split.color.DomainColoringSplit;

public class SplitComplexFunctionPlot
        extends AbstractSplitComplexFunctionPlot<SplitComplexFunctionPlot, SplitComplexFunctionPanel, SplitComplex> {

    public SplitComplexFunctionPlot(String title) {
        super(title);
        colorFn(new DomainColoringSplit());
    }

    @Override
    protected SplitComplexFunctionPanel createPanel() {
        return new SplitComplexFunctionPanel(this);
    }
}

