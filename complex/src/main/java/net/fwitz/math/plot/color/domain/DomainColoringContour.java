package net.fwitz.math.plot.color.domain;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.color.ColorFunction;

import java.awt.*;

import static java.awt.Color.getHSBColor;
import static java.lang.Math.floor;
import static java.lang.Math.log;

public class DomainColoringContour implements ColorFunction {
    private static final double TWO_PI = Math.PI * 2.0;

    private static float hue(Complex z) {
        double hue = z.arg() / TWO_PI;
        return (float) (hue - floor(hue));
    }

    private static double logScaleMantissa(Complex z) {
        double m = log(z.abs());
        return m - floor(m);
    }

    private static float brightness(double k) {
        return (float) (0.4 + 0.6 * k);
    }


    public Color apply(Complex c, Complex z) {
        double k = logScaleMantissa(z);
        float hue = hue(z);
        float val = brightness(k);
        return getHSBColor(hue, 1.0f, val);
    }
}
