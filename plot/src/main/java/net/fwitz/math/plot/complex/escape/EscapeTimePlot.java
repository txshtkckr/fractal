package net.fwitz.math.plot.complex.escape;

import net.fwitz.math.main.escape.EscapeTimeResult;
import net.fwitz.math.plot.complex.AbstractComplexFunctionPlot;

public class EscapeTimePlot extends AbstractComplexFunctionPlot<EscapeTimePlot, EscapeTimePanel, EscapeTimeResult> {
    public EscapeTimePlot(String title) {
        super(title);
    }

    @Override
    protected EscapeTimePanel createPanel() {
        return new EscapeTimePanel(this);
    }
}
