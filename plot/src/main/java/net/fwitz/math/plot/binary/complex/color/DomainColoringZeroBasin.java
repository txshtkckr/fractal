package net.fwitz.math.plot.binary.complex.color;

import net.fwitz.math.complex.Complex;

import java.awt.*;

import static java.awt.Color.getHSBColor;
import static java.lang.Math.floor;
import static java.lang.Math.pow;

public class DomainColoringZeroBasin implements ComplexColorFunction {
    private static final double TWO_PI = Math.PI * 2.0;

    private static float hue(Complex z) {
        double hue = z.arg() / TWO_PI;
        return (float) (hue - floor(hue));
    }

    private static double mantissa(double x) {
        return x - floor(x);
    }

    private static float brightness(double k) {
        double val = k < 0.5 ? k * 2 : 1 - (k - 0.5) * 2;
        val = 1 - val;
        val = 1 - pow((1 - val), 3);
        val = 0.4 + val * 0.6;
        return (float) val;
    }

    private static float saturation(double k) {
        double sat = (k < 0.5) ? k * 2 : 1 - (k - 0.5) * 2;
        sat = 1 - pow(1 - sat, 3);
        sat = 0.4 + sat * 0.6;
        return (float) sat;
    }

    public Color apply(Complex c, Complex z) {
        double logZ = z.logabs();
        double k = mantissa(logZ);
        float hue = hue(z);
        float sat = saturation(k);
        float val = brightness(k);

        double absRe = Math.abs(z.x());
        if (absRe < 0.05) {
            sat *= absRe * absRe * 200 + 0.5;
        }

        double absIm = Math.abs(z.y());
        if (absIm < 0.05) {
            val *= absIm * absIm * 200 + 0.5;
        }

        return getHSBColor(hue, sat, val);
    }
}
