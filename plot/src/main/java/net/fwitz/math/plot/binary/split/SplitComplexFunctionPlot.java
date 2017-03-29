package net.fwitz.math.plot.binary.split;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.binary.BinaryNumberFunctionPlot;
import net.fwitz.math.plot.binary.split.color.DomainColoringSplit;

public class SplitComplexFunctionPlot extends BinaryNumberFunctionPlot<
        SplitComplexFunctionPlot,
        SplitComplexFunctionPanel,
        SplitComplex,
        SplitComplex> {

    public SplitComplexFunctionPlot(String title) {
        super(title);
        colorFn(new DomainColoringSplit());
    }

    @Override
    public SplitComplex value(double x, double y) {
        return SplitComplex.splitComplex(x, y);
    }

    @Override
    protected SplitComplexFunctionPanel createPanel() {
        return new SplitComplexFunctionPanel(this);
    }
}
