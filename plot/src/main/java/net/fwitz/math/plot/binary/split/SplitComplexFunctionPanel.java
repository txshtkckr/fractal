package net.fwitz.math.plot.binary.split;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel;

public class SplitComplexFunctionPanel extends BinaryNumberFunctionPanel<SplitComplex, SplitComplex> {
    SplitComplexFunctionPanel(SplitComplexFunctionPlot plot) {
        super(SplitComplex.class, SplitComplex.class, plot);
    }

    @Override
    protected SplitComplexFunctionRenderer<SplitComplex> createRenderer(int width, int height) {
        return new SplitComplexFunctionRenderer<>(
                this, valueType,
                width, height,
                SplitComplexFunctionValuesFilters.getInstance());
    }
}
