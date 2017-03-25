package net.fwitz.math.main.escape.halley;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeFunction;
import net.fwitz.math.fractal.escape.EscapeTimeResult;
import net.fwitz.math.plot.complex.escape.EscapeTimePlot;

import java.awt.*;
import java.util.function.Function;

import static java.lang.Math.floor;
import static net.fwitz.math.fractal.escape.newton.HalleysMethod.halleysMethod;

public class HalleysMethodPlot {
    private static final double TWO_PI = Math.PI * 2.0;
    private static final int GRADES = 10;

    private static Color color(EscapeTimeResult result) {
        if (result.contained()) {
            return Color.BLACK;
        }

        float hue = hue(result.z());
        float val = (float) val(result.iters(), result.maxIters());
        return Color.getHSBColor(hue, 1.0f, val);
    }

    private static double val(int iters, int maxIters) {
        double absoluteCloseness = (double) (maxIters - iters) / maxIters;
        double relativeCloseness = (double) (GRADES - (iters % GRADES)) / GRADES;
        return 0.2 + 0.8 * absoluteCloseness * relativeCloseness;
    }

    private static float hue(Complex z) {
        double hue = z.arg() / TWO_PI;
        return (float) (hue - floor(hue));
    }

    public static void render(String name, EscapeFunction escapeFn) {
        new EscapeTimePlot(name)
                .computeFn(escapeFn)
                .domainBound(-3.5, -2.5, 3.5, 2.5)
                .colorFn((c, result) -> color(result))
                .render();
    }

    public static void render(
            String name,
            Function<Complex, Complex> f,
            Function<Complex, Complex> df,
            Function<Complex, Complex> d2f) {
        render(name, halleysMethod(f, df, d2f));
    }

    public static void zPowMinus1(double power) {
        double power2m1 = power * (power - 1);
        render("z^" + power + " - 1 (Halley's Method)", halleysMethod(
                z -> z.pow(power).minus(1),
                z -> z.pow(power - 1).times(power),
                z -> z.pow(power - 2).times(power2m1)));
    }

    public static void zPowMinus1(Complex power) {
        Complex power2m1 = power.times(power.minus(1));
        render("z^" + power + " - 1", halleysMethod(
                z -> z.pow(power).minus(1),
                z -> z.pow(power.minus(1)).times(power),
                z -> z.pow(power.minus(2)).times(power2m1)));
    }
}
