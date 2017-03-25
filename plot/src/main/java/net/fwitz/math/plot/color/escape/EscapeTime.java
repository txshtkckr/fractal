package net.fwitz.math.plot.color.escape;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.main.escape.EscapeTimeResult;

import java.awt.*;

import static java.lang.Math.floor;

/**
 * Maps a complex value whose real value is the escape time and imaginary value is the final {@code |z|}
 * onto an escape time color plot.  Use {@code NaN} as the value if it had not escaped after the full iteration.
 */
public class EscapeTime implements EscapeTimeColorFunction {
    private static float hue(int iters) {
        double hue = iters / 100.0;
        return (float) (hue - floor(hue));
    }

    private static double mantissa(double x) {
        return x - floor(x);
    }

    private static float escapeBrightness(double ratio) {
        return (float) (1.0 - 0.6 * ratio);
    }

    private static float containedBrightness(double k) {
        double x = mantissa(6 * k);
        x *= 1 - x;
        if (x < 0.02) {
            return (float) (0.2 + 0.7 * mantissa(k));
        }
        return (float) (0.3 + 0.7 * mantissa(k));
    }

    public Color apply(Complex c, EscapeTimeResult result) {
        if (result.escaped()) {
            int iters = result.iters();
            double ratio = (double) iters / result.maxIters();

            float hue = hue(iters);
            float val = escapeBrightness(ratio);
            return Color.getHSBColor(hue, 1.0f, val);
        }

        double k = result.z().logabs();
        float val = containedBrightness(k);
        return new Color(val, val, 0.0f);
    }
}
