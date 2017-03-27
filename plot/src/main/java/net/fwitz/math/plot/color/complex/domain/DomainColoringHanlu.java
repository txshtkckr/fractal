package net.fwitz.math.plot.color.complex.domain;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.color.complex.ComplexColorFunction;

import java.awt.*;

import static java.lang.Math.floor;
import static java.lang.Math.log;

/**
 * See http://users.mai.liu.se/hanlu09/complex/domain-coloring.scm
 */
public class DomainColoringHanlu implements ComplexColorFunction {
    private static final double TWO_PI = Math.PI * 2;
    private static final double LN_2 = Math.log(2);
    private static final double DELTA = 0.05;

    private static double frac(double x) {
        return x - floor(x);
    }

    private static boolean closeToInt(double x, double delta) {
        double frac = frac(x);
        if (frac > 0.5) {
            frac = 1.0 - frac;
        }
        return frac <= delta;
    }

    private static boolean isOnGrid(Complex z) {
        return closeToInt(z.re(), DELTA) || closeToInt(z.im(), DELTA);
    }

    public Color apply(Complex c, Complex z) {
        double phase = frac(z.arg() / TWO_PI);
        double logAbs = log(z.abs() / LN_2);
        final Color clr = fnNoGrid(phase, logAbs);
        if (isOnGrid(z)) {
            return new Color(clr.getRed() / 2, clr.getGreen() / 2, clr.getBlue() / 2);
        }
        return clr;
    }

    private static Color fnNoGrid(double phase, double logAbs) {
        double logGradient = frac(logAbs);

        if (phase < 0.5) {
            double redMax = phase * 2;
            double redMin = redMax * redMax;
            return new Color(value(redMin, redMax, logGradient), 0, 0);
        }

        double grnMax = phase * 2 - 1;
        double grnMin = grnMax * grnMax;
        return new Color(255, value(grnMin, grnMax, logGradient), 0);
    }

    private static int value(double min, double max, double logGradient) {
        double value = min + (max - min) * logGradient;
        int x = (int) (value * 256);
        return (x < 0) ? 0 : ((x > 255) ? 255 : x);
    }
}
