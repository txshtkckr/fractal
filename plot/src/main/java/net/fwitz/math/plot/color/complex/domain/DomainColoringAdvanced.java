package net.fwitz.math.plot.color.complex.domain;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.color.complex.ComplexColorFunction;

import java.awt.*;

import static java.lang.Math.abs;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Advanced domain coloring algorithm based on a Mathematica example discussed on StackOverflow..
 * <blockquote>
 * <pre>
 * complexHSB = Compile[{{Z, _Complex, 2}}, Block[{h, s, b, b2},
 *     h = Arg[Z]/(2 Pi);
 *     s = Abs[Sin[2 Pi Abs[Z]]];
 *     b = Sqrt[Sqrt[Abs[Sin[2 Pi Im[Z]] Sin[2 Pi Re[Z]]]]];
 *     b2 = 0.5 ((1 - s) + b + Sqrt[(1 - s - b)^2 + 0.01]);
 *     Transpose[{h, Sqrt[s], b2}, {3, 1, 2}]]];
 * </pre>
 * <p>
 * <cite>&mdash; <a href="http://mathematica.stackexchange.com/users/862/simon-woods">Simon Woods</a> in
 * <a href="http://mathematica.stackexchange.com/a/7359">this StackOverflow answer</a></cite>
 * </p>
 * </blockquote>
 */
public class DomainColoringAdvanced implements ComplexColorFunction {
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
        return hsb(h, sqrt(s), filterBrightness(b2, z));
    }

    protected double filterBrightness(double b, Complex z) {
        return b;
    }

    private static Color hsb(double h, double s, double b) {
        return Color.getHSBColor(f(h), f(s), f(b));
    }

    private static float f(double x) {
        final float f = (float) x;
        return (f < 0.0f) ? 0.0f : ((f > 1.0f) ? 1.0f : f);
    }
}
