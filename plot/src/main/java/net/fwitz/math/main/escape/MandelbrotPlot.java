package net.fwitz.math.main.escape;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeFunction;
import net.fwitz.math.fractal.escape.EscapeTimeResult;
import net.fwitz.math.plot.color.escape.EscapeTimePaletteFunction;
import net.fwitz.math.plot.color.palette.PaletteVGA8bitRGB;
import net.fwitz.math.plot.complex.escape.EscapeTimePanel;
import net.fwitz.math.plot.complex.escape.EscapeTimePlot;

import javax.swing.*;

import static net.fwitz.math.plot.complex.AbstractComplexFunctionPanel.action;

public class MandelbrotPlot {
    private static final double P_MIN = -2;
    private static final double P_MAX = 1;
    private static final double Q_MIN = -1.5;
    private static final double Q_MAX = 1.5;
    private static final int ITERS = 1000;

    private static final EscapeFunction MANDELBROT = EscapeFunction.builder()
            .step((c, z) -> z.pow2().plus(c))
            .escapeTest(z -> z.abs() >= 2)
            .maxIters(ITERS)
            .build();

    public static void main(String[] args) {
        new MandelbrotPlot().render();
    }

    private volatile EscapeFunction delegate = MANDELBROT;
    private final EscapeTimePlot plot;

    private MandelbrotPlot() {
        this.plot = new EscapeTimePlot("Mandelbrot (Escape time)")
                .computeFn(this::applyDelegate)
                .domainBound(P_MIN, Q_MIN, P_MAX, Q_MAX)
                .colorFn(EscapeTimePaletteFunction.escapeTime(new PaletteVGA8bitRGB()))
                .decoratePanel(this::addListeners);
    }

    private EscapeTimeResult applyDelegate(Complex c) {
        return delegate.apply(c);
    }

    private void render() {
        plot.render();
    }

    private static EscapeFunction julia(Complex c) {
        return EscapeFunction.builder()
                .init(z0 -> z0.pow2().plus(c))
                .step((z0, z) -> z.pow2().plus(c))
                .escapeTest(z -> z.abs() > 2)
                .maxIters(ITERS)
                .build();
    }

    private void toggleJulia(EscapeTimePanel panel) {
        if (delegate != MANDELBROT) {
            // Switching from Julia to Mandelbrot, so it doesn't matter where the mouse is
            delegate = MANDELBROT;
            panel.reset();
        } else {
            panel.getMouseLocationAsComplexValue().ifPresent(c0 -> {
                delegate = julia(c0);
                panel.reset();
            });
        }
    }

    private void addListeners(EscapeTimePanel panel) {
        panel.getInputMap().put(KeyStroke.getKeyStroke('j'), "toggleJulia");
        panel.getActionMap().put("toggleJulia", action(e -> toggleJulia(panel)));
    }
}