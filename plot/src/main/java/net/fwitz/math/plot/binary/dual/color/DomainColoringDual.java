package net.fwitz.math.plot.binary.dual.color;

import net.fwitz.math.binary.dual.DualNumber;

import java.awt.*;

import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static java.lang.Math.floor;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static net.fwitz.math.binary.dual.DualNumber.dualNumber;

public class DomainColoringDual implements DualNumberColorFunction {
    private static final double TWO_PI = Math.PI * 2.0;

    public Color apply(DualNumber c, DualNumber z) {
        return complexHSB(z);
    }

    private static Color complexHSB(DualNumber z) {
        if (z.isNaN()) {
            return Color.DARK_GRAY;
        }
        if (z.isInfinite()) {
            return Color.WHITE;
        }
        if (z.x() == 0) {
            return (z.y() == 0) ? Color.BLACK : Color.LIGHT_GRAY;
        }

        double h = hue(z);
        double s = abs(sin(TWO_PI * z.abs()));
        double b = sqrt(sqrt(abs(sin(TWO_PI * z.y()) * sin(TWO_PI * z.x()))));
        double oneMsMb = 1.0 - s - b;
        double b2 = 0.5 * ((1 - s) + b + sqrt(oneMsMb * oneMsMb + 0.01));
        return hsb(h, sqrt(s), b2);
    }

    // Hrmmm... how to smash  [-Inf, 0, +Inf] into [0.0, 0.5, 1.0]...
    private static double hue(DualNumber z) {
        double hue = 0.5 / (1 + exp(-z.arg()));
        hue = (z.x() < 0) ? (hue + 0.25) : (hue + 0.75);
        return hue - floor(hue);
    }

    private static Color hsb(double h, double s, double b) {
        return Color.getHSBColor(f(h), f(s), f(b));
    }

    private static float f(double x) {
        final float f = (float) x;
        return (f < 0.0f) ? 0.0f : ((f > 1.0f) ? 1.0f : f);
    }

    public static void main(String[] args) {
        debug(5, 0.01);
        debug(4, 3);
        debug(3, 4);
        debug(0, 5);
        debug(-3, 4);
        debug(-4, 3);
        debug(-5, 0);
        debug(-4, -3);
        debug(-3, -4);
        debug(0.01, -5);
        debug(3, -4);
        debug(4, -3);
    }

    private static void debug(double x, double y) {
        final DualNumber z = dualNumber(x, y);
        System.out.printf("z=(%10.7f, %10.7f)  abs=%10.7f  arg=%10.7f  hue=%10.7f  color=%s\n",
                x, y, z.abs(), z.arg(), hue(z), complexHSB(z));
    }
}
