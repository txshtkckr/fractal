package net.fwitz.math.main.escape.halley;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeFunction;
import net.fwitz.math.plot.complex.escape.color.NewtonsMethodDarkZero;
import net.fwitz.math.plot.complex.escape.EscapeTimePlot;

import java.util.function.Function;

import static net.fwitz.math.complex.calculus.Derivation.derivative;
import static net.fwitz.math.fractal.escape.newton.HalleysMethod.halleysMethod;

public class HalleysMethodPlot {
    public static void renderEscape(String name, EscapeFunction escapeFn) {
        new EscapeTimePlot(name)
                .computeFn(escapeFn)
                .domainBound(-3.5, -2.5, 3.5, 2.5)
                .size(700, 500)
                .colorFn(new NewtonsMethodDarkZero())
                .render();
    }

    public static void render(
            String name,
            Function<Complex, Complex> f) {
        final Function<Complex, Complex> df = derivative(f);
        final Function<Complex, Complex> d2f = derivative(df);
        render(name, f, df, d2f);
    }

    public static void render(
            String name,
            Function<Complex, Complex> f,
            Function<Complex, Complex> df) {
        final Function<Complex, Complex> d2f = derivative(df);
        render(name, f, df, d2f);
    }

    public static void render(
            String name,
            Function<Complex, Complex> f,
            Function<Complex, Complex> df,
            Function<Complex, Complex> d2f) {
        renderEscape(name, halleysMethod(f, df, d2f));
    }

    public static void zPowMinus1(double power) {
        double power2m1 = power * (power - 1);
        render("z^" + power + " - 1 (Halley's Method)",
                z -> z.pow(power).minus(1),
                z -> z.pow(power - 1).times(power),
                z -> z.pow(power - 2).times(power2m1));
    }

    public static void zPowMinus1(Complex power) {
        Complex power2m1 = power.times(power.minus(1));
        render("z^" + power + " - 1",
                z -> z.pow(power).minus(1),
                z -> z.pow(power.minus(1)).times(power),
                z -> z.pow(power.minus(2)).times(power2m1));
    }
}
