package net.fwitz.math.main.escape;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeFunction;
import net.fwitz.math.fractal.escape.EscapeTimeResult;
import net.fwitz.math.plot.binary.escape.color.EscapeTimePaletteFunction;
import net.fwitz.math.plot.renderer.palette.PaletteVGA256;
import net.fwitz.math.plot.binary.escape.EscapeTimePlot;

import java.util.function.Function;

public class BurningShipPlot {
    private static final double P_MIN = -2;
    private static final double P_MAX = 1;
    private static final double Q_MIN = -1.5;
    private static final double Q_MAX = 1.5;
    private static final int ITERS = 1000;
    private static final Function<Complex, EscapeTimeResult> BURNING_SHIP = EscapeFunction.builder()
            .includeInit()
            .containmentTest(z -> z.abs() < 2)
            .step((c, z) -> z.rectify().pow2().plus(c))
            .maxIters(ITERS)
            .build();

    public static void main(String[] args) {
        EscapeTimePlot.complex("Burning Ship (Escape time)")
                .computeFn(BURNING_SHIP)
                .colorFn(EscapeTimePaletteFunction.escapeTime(new PaletteVGA256()))
                .domainX(P_MIN, P_MAX)
                .domainY(Q_MIN, Q_MAX)
                .render();
    }
}
