package net.fwitz.math.main.escape;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.complex.escape.EscapeTimePlot;

import java.util.function.Function;

public class BurningShipPlot {
    private static final double P_MIN = -2;
    private static final double P_MAX = 1;
    private static final double Q_MIN = -1.5;
    private static final double Q_MAX = 1.5;
    private static final int ITERS = 1000;
    private static final Function<Complex, EscapeTimeResult> BURNING_SHIP = EscapeFunction.builder()
            .init(c -> c)
            .containmentTest(z -> z.abs() < 2)
            .step((c, z) -> z.rectify().pow2().plus(c))
            .maxIters(ITERS)
            .build();

    public static void main(String[] args) {
        new EscapeTimePlot("Burning Ship (Escape time)")
                .computeFn(BURNING_SHIP)
                .domainBound(P_MIN, Q_MIN, P_MAX, Q_MAX)
                .render();
    }
}
