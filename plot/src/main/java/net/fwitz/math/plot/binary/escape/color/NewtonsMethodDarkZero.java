package net.fwitz.math.plot.binary.escape.color;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeTimeResult;

import java.awt.*;

import static java.lang.Math.floor;

public class NewtonsMethodDarkZero implements EscapeTimeColorFunction {
    private static final double ONE_OVER_TWO_PI = 0.5 / Math.PI;
    private static final int GRADES = 10;

    @Override
    public Color apply(Complex c, EscapeTimeResult result) {
        if (result.contained()) {
            return Color.BLACK;
        }

        float hue = hue(result.z());
        float val = (float) val(result.iters());
        return Color.getHSBColor(hue, 1.0f, val);
    }

    private double val(int iters) {
        double relativeCloseness = (double) (GRADES - (iters % GRADES)) / GRADES;
        return val(relativeCloseness);
    }

    double val(double closeness) {
        return 1 - 0.9 * closeness;
    }

    private static float hue(Complex z) {
        double hue = z.arg() * ONE_OVER_TWO_PI;
        return (float) (hue - floor(hue));
    }
}
