package net.fwitz.math.plot.binary.dual;

import net.fwitz.math.binary.dual.DualNumber;
import net.fwitz.math.plot.binary.BinaryNumberFunctionPlot;
import net.fwitz.math.plot.binary.dual.color.DomainColoringDual;

public class DualNumberFunctionPlot extends BinaryNumberFunctionPlot<
        DualNumberFunctionPlot,
        DualNumberFunctionPanel,
        DualNumber,
        DualNumber> {

    public DualNumberFunctionPlot(String title) {
        super(title);
        colorFn(new DomainColoringDual());
    }

    @Override
    public DualNumber value(double x, double y) {
        return DualNumber.dualNumber(x, y);
    }

    @Override
    protected DualNumberFunctionPanel createPanel() {
        return new DualNumberFunctionPanel(this);
    }
}
