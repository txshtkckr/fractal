package net.fwitz.math.plot.complex.color;

import net.fwitz.math.complex.Complex;

import static java.lang.Math.floor;
import static java.lang.Math.log;

public class DomainColoringAdvancedPlusLogScale extends DomainColoringAdvanced {
    private static final double LN_2 = log(2);
    private static final double SCALE = 0.25 / LN_2;

    @Override
    protected double filterBrightness(double b, Complex z) {
        double x = z.logabs() * SCALE;
        x = 0.3 + 0.7 * (x - floor(x));
        x *= b;
        return (x < 0.0) ? 0.0 : ((x > 1.0) ? 1.0 : x);
    }
}
