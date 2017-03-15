package net.fwitz.math.plot.color.escape;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.color.ColorFunction;

import java.awt.*;

import static java.lang.Math.floor;

/**
 * Maps a complex value whose real value is the escape time and imaginary value is the maximum iteration count
 * onto an escape time color plot.  Use {@code NaN} as the value if it had not escaped after the full iteration.
 */
public class EscapeTime implements ColorFunction {
    private static float hue(int iters) {
        double hue = iters / 100.0;
        return (float) (hue - floor(hue));
    }

    private static float brightness(int iters, int maxIters) {
        return (float) (1.0 - 0.6 * iters / maxIters);
    }


    public Color apply(Complex c, Complex z) {
        if (z.isNaN()) {
            return Color.BLACK;
        }

        int iters = (int) Math.round(z.re());
        int maxIters = (int) Math.round(z.im());

        float hue = hue(iters);
        float val = brightness(iters, maxIters);
        return Color.getHSBColor(hue, 1.0f, val);
    }
}
