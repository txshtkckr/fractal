package net.fwitz.math.main.binary.escape;

import net.fwitz.math.fractal.escape.EscapeFunction;
import net.fwitz.math.fractal.escape.collatz.Collatz;
import net.fwitz.math.plot.binary.escape.EscapeTimePlot;
import net.fwitz.math.plot.binary.escape.color.EscapeTimeInterpolator;
import net.fwitz.math.plot.renderer.palette.EscapeTimePalette;

public class CollatzPlot {
    private static final double P_MIN = -20;
    private static final double P_MAX = 20;
    private static final double Q_MIN = -20;
    private static final double Q_MAX = 20;
    private static final int ITERS = 1000;
    private static final int BAILOUT_RADIUS = 1 << 8;
    private static final int BAILOUT = BAILOUT_RADIUS * BAILOUT_RADIUS;

    private static final EscapeFunction COLLATZ = EscapeFunction.builder()
            .step((c, z) -> Collatz.collatz(z))
            .escapeTest(z -> z.abs2() > BAILOUT)
            .maxIters(ITERS)
            .build();

    public static void main(String[] args) {
        EscapeTimePlot.complex("Collatz (Escape time)")
                .computeFn(COLLATZ)
                .domainX(P_MIN, P_MAX)
                .domainY(Q_MIN, Q_MAX)
                .colorFn(new EscapeTimeInterpolator(100, BAILOUT_RADIUS, new EscapeTimePalette()))
                .render();
    }
}
