package net.fwitz.math.plot.binary.escape.color;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeTimeResult;
import net.fwitz.math.plot.renderer.palette.Interpolate;
import net.fwitz.math.plot.renderer.palette.Palette;

import java.awt.*;
import java.util.function.BiFunction;

import static java.lang.Math.log;
import static java.util.Objects.requireNonNull;

public class EscapeTimeInterpolator implements EscapeTimeColorFunction {
    private final double mN;
    private final double mP;
    private final Color color0;
    private final BiFunction<EscapeTimeResult, Integer, Color> lookup;

    /**
     *
     * @param power the largest power used during iteration, such as 2 for the standard Mandelbrot set
     * @param radius the bailout radius; 2 on the normal Mandelbrot set
     * @param palette the color palette to apply
     */
    public EscapeTimeInterpolator(double power, double radius, Palette palette) {
        this(power, radius, palette.index(0), (result, n) -> palette.indexExcluding0(n));
    }

    public EscapeTimeInterpolator(double power, double radius, Color color0,
                                  BiFunction<EscapeTimeResult, Integer, Color> lookup) {
        // nu = n - log(log(|z|) / log(N)) / log(P)
        // nu = n - log(log(|z|) * mN) * mP
        this.mN = 1 / log(radius);
        this.mP = 1 / log(power);
        this.color0 = requireNonNull(color0, "color0");
        this.lookup = requireNonNull(lookup, "lookup");
    }


    @Override
    public Color apply(Complex c, EscapeTimeResult result) {
        if (result.contained()) {
            return color0;
        }

        int iters;
        double mantissa;
        if (result.smoothing().isPresent()) {
            iters = result.iters();
            mantissa = result.smoothing().getAsDouble();
        } else {
            double nu = result.iters() + 1 - log(result.z().logabs() * mN) * mP;
            iters = (int) Math.floor(nu);
            mantissa = nu - iters;
        }

        if (iters < 1) {
            return lookup.apply(result, 1);
        }

        Color c1 = lookup.apply(result, iters);
        Color c2 = lookup.apply(result, iters + 1);
        return Interpolate.interpolate(c1, c2, mantissa);
    }
}
