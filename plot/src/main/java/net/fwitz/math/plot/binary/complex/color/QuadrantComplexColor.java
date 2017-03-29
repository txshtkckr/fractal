package net.fwitz.math.plot.binary.complex.color;

import net.fwitz.math.binary.complex.Complex;

import java.awt.*;

import static java.lang.Math.abs;

public class QuadrantComplexColor implements ComplexColorFunction {
    public Color apply(Complex c, Complex z) {
        if (abs(z.x()) == 0.0 || abs(z.y()) == 0.0) {
            return Color.BLACK;
        }
        if (z.y() > 0.0) {
            return (z.x() > 0.0) ? Color.BLUE : Color.GREEN;
        }
        return (z.x() > 0.0) ? Color.RED : Color.GRAY;
    }
}
