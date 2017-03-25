package net.fwitz.math.plot.color.complex.domain;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.color.complex.ComplexColorFunction;

import java.awt.*;

import static java.awt.Color.getHSBColor;
import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.pow;

public class ComplexColorWheel implements ComplexColorFunction {
    private static final double TWO_PI = Math.PI * 2.0;

    private static float hue(Complex z) {
        double hue = z.arg() / TWO_PI;
        return (float) (hue - floor(hue));
    }

    private static double logScaleMantissa(Complex z) {
        double m = log(z.abs());
        return m - floor(m);
    }

    private static float saturation(double k) {
        double sat = (k < 0.5) ? k * 2 : 1 - (k - 0.5) * 2;
        sat = 1 - pow(1 - sat, 3);
        sat = 0.4 + sat * 0.6;
        return (float) sat;
    }

    private static float brightness(double k) {
        double val = k < 0.5 ? k * 2 : 1 - (k - 0.5) * 2;
        val = 1 - val;
        val = 1 - pow((1 - val), 3);
        val = 0.6 + val * 0.4;
        return (float) val;
    }

    public Color apply(Complex c, Complex z) {
        double k = logScaleMantissa(z);
        float hue = hue(z);
        float sat = saturation(k);
        float val = brightness(k);
        return getHSBColor(hue, sat, val);
    }
}
