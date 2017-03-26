package net.fwitz.math.main.escape;

import net.fwitz.math.fractal.escape.EscapeFunction;
import net.fwitz.math.plot.color.escape.EscapeTimeColor;
import net.fwitz.math.plot.complex.escape.EscapeTimePlot;

public class Biomorph2 {
    private static final double P_MIN = -2.0;
    private static final double P_MAX = 1.0;
    private static final double Q_MIN = -1.5;
    private static final double Q_MAX = 1.5;
    private static final int ITERS = 100;

    public static void main(String[] args) {
        new EscapeTimePlot("Biomorph2 (Escape time)")
                .computeFn(EscapeFunction.builder()
                        .init(c -> c.plus(1))
                        .step((c, z) -> z.times(z).plus(c).plus(z.cos()))
                        .escapeTest(z -> z.abs() > 2)
                        .maxIters(ITERS)
                        .build())
                .domainBound(P_MIN, Q_MIN, P_MAX, Q_MAX)
                .colorFn(new EscapeTimeColor())
                .render();
    }
}
