package net.fwitz.math.plot.complex.escape;

import net.fwitz.math.fractal.escape.EscapeTimeResult;
import net.fwitz.math.plot.complex.filter.EscapeTimeValuesFilters;
import net.fwitz.math.plot.complex.AbstractComplexFunctionPanel;
import net.fwitz.math.plot.complex.ComplexFunctionRenderer;

public class EscapeTimePanel extends AbstractComplexFunctionPanel<EscapeTimeResult> {
    EscapeTimePanel(EscapeTimePlot plot) {
        super(EscapeTimeResult.class, plot);
    }

    @Override
    protected ComplexFunctionRenderer<EscapeTimeResult> createRenderer(int width, int height) {
        return new ComplexFunctionRenderer<>(this, EscapeTimeResult.class, width, height,
                EscapeTimeValuesFilters.getInstance());
    }
}
