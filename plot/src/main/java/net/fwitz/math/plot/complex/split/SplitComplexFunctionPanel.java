package net.fwitz.math.plot.complex.split;

import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.complex.filter.SplitComplexFunctionValuesFilters;

public class SplitComplexFunctionPanel extends AbstractSplitComplexFunctionPanel<SplitComplex> {
    SplitComplexFunctionPanel(SplitComplexFunctionPlot plot) {
        super(SplitComplex.class, plot);
    }

    @Override
    protected SplitComplexFunctionRenderer<SplitComplex> createRenderer(int width, int height) {
        return new SplitComplexFunctionRenderer<>(this, valueType, width, height, SplitComplexFunctionValuesFilters.getInstance());
    }
}
