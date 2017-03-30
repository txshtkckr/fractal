package net.fwitz.math.plot.binary.dual;

import net.fwitz.math.binary.dual.DualNumber;
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel;

public class DualNumberFunctionPanel extends BinaryNumberFunctionPanel<DualNumber, DualNumber> {
    DualNumberFunctionPanel(DualNumberFunctionPlot plot) {
        super(DualNumber.class, DualNumber.class, plot);
    }

    @Override
    protected DualNumberFunctionRenderer<DualNumber> createRenderer(int width, int height) {
        return new DualNumberFunctionRenderer<>(
                this, valueType,
                width, height,
                DualNumberFunctionValuesFilters.getInstance());
    }
}
