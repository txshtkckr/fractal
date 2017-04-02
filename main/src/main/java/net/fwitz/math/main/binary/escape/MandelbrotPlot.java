package net.fwitz.math.main.binary.escape;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeFunction;
import net.fwitz.math.fractal.escape.EscapeTimeResult;
import net.fwitz.math.plot.binary.escape.EscapeTimePanel;
import net.fwitz.math.plot.binary.escape.EscapeTimePlot;
import net.fwitz.math.plot.binary.escape.color.EscapeTimeInterpolator;
import net.fwitz.math.plot.renderer.palette.EscapeTimePalette;

import javax.swing.*;

import static net.fwitz.math.plot.renderer.ImageRendererPanel.action;

public class MandelbrotPlot {
    private static final double P_MIN = -2;
    private static final double P_MAX = 1;
    private static final double Q_MIN = -1.5;
    private static final double Q_MAX = 1.5;
    private static final int ITERS = 1000;
    private static final int BAILOUT_RADIUS = 1 << 8;
    private static final int BAILOUT = BAILOUT_RADIUS * BAILOUT_RADIUS;

    private final double power;
    private final EscapeFunction mandel;

    public static void main(String[] args) {
        new MandelbrotPlot(2, true).render();
    }

    private volatile EscapeFunction delegate;
    private final EscapeTimePlot<Complex> plot;

    public MandelbrotPlot() {
        this(2, true);
    }

    public MandelbrotPlot(double power) {
        this(power, false);
    }

    private MandelbrotPlot(double power, boolean useShortcut) {
        this.power = power;
        this.plot = EscapeTimePlot.complex("Mandelbrot (Escape time)")
                .computeFn(this::applyDelegate)
                .domainX(P_MIN, P_MAX)
                .domainY(Q_MIN, Q_MAX)
                .colorFn(new EscapeTimeInterpolator(power, BAILOUT_RADIUS, new EscapeTimePalette()))
                .decoratePanel(this::addListeners);

        EscapeFunction.Builder builder = EscapeFunction.builder()
                .includeInit()
                .step((c, z) -> z.pow(power).plus(c))
                .escapeTest(z -> z.abs2() >= BAILOUT)
                .maxIters(ITERS);
        if (useShortcut) {
            builder.shortcutContainmentTest(MandelbrotPlot::inMainCardioidOrCircle);
        }
        this.mandel = builder.build();
        this.delegate = mandel;
    }

    private EscapeTimeResult applyDelegate(Complex c) {
        return delegate.apply(c);
    }

    public void render() {
        plot.render();
    }

    private static EscapeFunction julia(Complex c, double power) {
        return EscapeFunction.builder()
                .step((z0, z) -> z.pow(power).plus(c))
                .escapeTest(z -> z.abs2() >= BAILOUT)
                .maxIters(ITERS)
                .build();
    }

    private void toggleJulia(EscapeTimePanel<Complex> panel) {
        if (delegate != mandel) {
            // Switching from Julia to Mandelbrot, so it doesn't matter where the mouse is
            delegate = mandel;
            panel.reset();
        } else {
            panel.getMouseLocationAsValue().ifPresent(c0 -> {
                delegate = julia(c0, power);
                panel.reset();
            });
        }
    }

    private void addListeners(EscapeTimePanel<Complex> panel) {
        panel.getInputMap().put(KeyStroke.getKeyStroke('j'), "toggleJulia");
        panel.getActionMap().put("toggleJulia", action(e -> toggleJulia(panel)));
    }

    // Returns true if c is in the main cardioid or the largest circular bulb to avoid wasting iterations on them.
    // This is an optimization that greatly reduces the time to render the set when these portions of the set are
    // included in the picture.  They'x also
    private static boolean inMainCardioidOrCircle(Complex c) {
        double re = c.x();
        if (re <= -0.75) {
            // Check the main circle
            double q = re + 1;
            q = q * q + c.y() * c.y();
            return q < 0.0625;
        }

        // Check the main cardioid
        double im = c.y();
        double q = re - 0.25;
        q = q * q + im * im;
        q = q * (q + re - 0.25);
        return q < 0.25 * im * im;
    }
}
