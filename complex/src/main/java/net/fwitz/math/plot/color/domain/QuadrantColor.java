package net.fwitz.math.plot.color.domain;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.color.ColorFunction;

import java.awt.*;

import static java.lang.Math.abs;

public class QuadrantColor implements ColorFunction {
    public Color apply(Complex c, Complex z) {
        if (abs(z.re()) == 0.0 || abs(z.im()) == 0.0) {
            return Color.BLACK;
        }
        if (z.im() > 0.0) {
            return (z.re() > 0.0) ? Color.BLUE : Color.GREEN;
        }
        return (z.re() > 0.0) ? Color.RED : Color.GRAY;
    }
}
