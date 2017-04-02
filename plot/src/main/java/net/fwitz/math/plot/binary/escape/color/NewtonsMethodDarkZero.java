package net.fwitz.math.plot.binary.escape.color;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeTimeResult;

import java.awt.*;

import static java.lang.Math.floor;

public class NewtonsMethodDarkZero implements EscapeTimeColorFunction {
    private static final double ONE_OVER_TWO_PI = 0.5 / Math.PI;
    private static final int GRADES = 10;

    @Override
    public Color apply(Complex c, EscapeTimeResult result) {
        return result.contained() ? Color.BLACK : escaped(result);
    }

    private Color escaped(EscapeTimeResult result) {
        float hue = hue(result.z());
        return color(hue, result.iters());
    }

    private Color color(float hue, int n) {
        float val = (float) val(n);
        return Color.getHSBColor(hue, 1.0f, val);
    }

    private double val(int n) {
        double relativeCloseness = (double) (GRADES - (n % GRADES)) / GRADES;
        return val(relativeCloseness);
    }

    private static float hue(Complex z) {
        double hue = z.arg() * ONE_OVER_TWO_PI;
        return (float) (hue - floor(hue));
    }

    double val(double closeness) {
        return 1 - 0.9 * closeness;
    }

    public EscapeTimeColorFunction interpolated(double power, double radius) {
        return new EscapeTimeInterpolator(power, radius, Color.BLACK, (result, n) -> {
            float hue = hue(result.z());
            return color(hue, n);
        });
    }
}
