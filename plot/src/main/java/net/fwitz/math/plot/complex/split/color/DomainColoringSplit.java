package net.fwitz.math.plot.complex.split.color;

import net.fwitz.math.complex.SplitComplex;

import java.awt.*;

import static java.lang.Math.abs;
import static java.lang.Math.exp;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static net.fwitz.math.complex.SplitComplex.splitComplex;

public class DomainColoringSplit implements SplitComplexColorFunction {
    private static final double TWO_PI = Math.PI * 2.0;

    public Color apply(SplitComplex c, SplitComplex z) {
        return complexHSB(z);
    }

    private static Color complexHSB(SplitComplex z) {
        boolean invert = false;
        double hueOffset;

        SplitComplex.Classification classification = z.classify();
        switch (classification) {
            case NAN:
                return Color.DARK_GRAY;
            case ZERO:
                return Color.BLACK;
            case POS_NULL_VECTOR:
            case NEG_NULL_VECTOR:
                return Color.LIGHT_GRAY;
            case POS_X:
                hueOffset = 0.875;
                break;
            case POS_Y:
                hueOffset = 0.125;
                invert = true;
                break;
            case NEG_X:
                hueOffset = 0.375;
                break;
            case NEG_Y:
                hueOffset = 0.625;
                invert = true;
                break;
            default:
                throw new IllegalStateException("Unexpected Unit type: " + classification);
        }

        if (z.isInfinite()) {
            return Color.WHITE;
        }

        double h = hue(z, invert) + hueOffset;
        double s = abs(sin(TWO_PI * z.abs()));
        double b = sqrt(sqrt(abs(sin(TWO_PI * z.y()) * sin(TWO_PI * z.x()))));
        double oneMsMb = 1.0 - s - b;
        double b2 = 0.5 * ((1 - s) + b + sqrt(oneMsMb * oneMsMb + 0.01));
        return hsb(h, sqrt(s), b2);
    }

    // Hrmmm... how to smash  [-Inf, 0, +Inf] into [0.0, 0.5, 1.0]...
    private static double hue(SplitComplex z, boolean invert) {
        double hue = 0.25 / (1 + exp(-z.arg()));
        return invert ? (0.25 - hue) : hue;
    }

    private static Color hsb(double h, double s, double b) {
        return Color.getHSBColor(f(h), f(s), f(b));
    }

    private static float f(double x) {
        final float f = (float) x;
        return (f < 0.0f) ? 0.0f : ((f > 1.0f) ? 1.0f : f);
    }

    public static void main(String[] args) {
        debug(5, -5);
        debug(5, -4.999);
        debug(5, -4.9);
        debug(5, -1);
        debug(5, 0);
        debug(5, 1);
        debug(5, 4.9);
        debug(5, 4.999);
        debug(5, 5);
    }

    private static void debug(double x, double y) {
        final SplitComplex z = splitComplex(x, y);
        System.out.printf("z=(%10.7f, %10.7f)  abs=%10.7f  arg=%10.7f  color=%s\n",
                x, y, z.abs(), z.arg(), complexHSB(z));
    }
}
