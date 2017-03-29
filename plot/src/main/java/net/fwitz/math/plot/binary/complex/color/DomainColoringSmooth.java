package net.fwitz.math.plot.binary.complex.color;


import net.fwitz.math.complex.Complex;

import java.awt.*;

import static java.awt.Color.getHSBColor;
import static java.lang.Math.floor;
import static java.lang.Math.log;

public class DomainColoringSmooth implements ComplexColorFunction {
    private static final double TWO_PI = Math.PI * 2.0;

    private static float hue(double arg) {
        double hue = arg / TWO_PI;
        return (float) (hue - floor(hue));
    }

    private static double logScale(Complex z) {
        return log(z.abs());
    }

    private static double mantissa(double x) {
        return x - floor(x);
    }

    private static float brightness(float hue, double k) {
        double x = mantissa(6 * hue);
        x *= 1 - x;
        if (x < 0.02) {
            return (float) (0.2 + 0.7 * mantissa(k));
        }
        return (float) (0.3 + 0.7 * mantissa(k));
    }

    public Color apply(Complex c, Complex z) {
        double arg = z.arg();
        double k = logScale(z);

        float hue = hue(arg);
        float val = brightness(hue, k);
        return getHSBColor(hue, 1.0f, val);
    }
}
