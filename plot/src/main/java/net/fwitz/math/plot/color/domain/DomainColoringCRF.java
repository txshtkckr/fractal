package net.fwitz.math.plot.color.domain;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.color.ColorFunction;

import java.awt.*;

import static java.lang.Math.abs;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class DomainColoringCRF implements ColorFunction {
    private static final double TWO_PI = Math.PI * 2.0;

    public Color apply(Complex c, Complex z) {
        return complexHSB(z);
    }

    protected Color complexHSB(Complex z) {
        double h = z.arg() / TWO_PI;
        if (h < 0) {
            h += 1.0;
        }
        double s = abs(sin(TWO_PI * z.abs()));
        double b = sqrt(sqrt(abs(sin(TWO_PI * z.im()) * sin(TWO_PI * z.re()))));
        double oneMsMb = 1.0 - s - b;
        double b2 = 0.5 * ((1 - s) + b + sqrt(oneMsMb * oneMsMb + 0.01));
        return hsb(h, sqrt(s), 1 - b2);
    }

    private static Color hsb(double h, double s, double b) {
        return Color.getHSBColor(f(h), f(s), f(b));
    }

    private static float f(double x) {
        final float f = (float)x;
        return (f < 0.0f) ? 0.0f : ((f > 1.0f) ? 1.0f : f);
    }
}
