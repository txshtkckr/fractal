package net.fwitz.math.main.escape;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeFunction;
import net.fwitz.math.plot.binary.escape.color.EscapeTimeColor;
import net.fwitz.math.plot.binary.escape.EscapeTimePlot;

// https://www.flickr.com/photos/fractal_ken/3476756762
public class SignOfTheSunGod {

    private static final double P_MIN = -2.0;
    private static final double P_MAX = 1.0;
    private static final double Q_MIN = -1.5;
    private static final double Q_MAX = 1.5;
    private static final int ITERS = 50;

    public static void main(String[] args) {
        EscapeTimePlot.complex("z(n) = 1 / (z(n-1) + z(0))^2 (Escape time)")
                .computeFn(EscapeFunction.builder()
                        .init(c -> Complex.ZERO)
                        .step((c, z) -> z.plus(c).pow(-2))
                        .escapeTest(z -> z.abs() > 10.0)
                        .maxIters(ITERS)
                        .build())
                .domainX(P_MIN, P_MAX)
                .domainY(Q_MIN, Q_MAX)
                .colorFn(new EscapeTimeColor())
                .render();
    }
}
