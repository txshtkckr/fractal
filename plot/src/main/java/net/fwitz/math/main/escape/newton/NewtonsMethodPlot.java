package net.fwitz.math.main.escape.newton;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeFunction;
import net.fwitz.math.plot.color.escape.NewtonsMethodDarkZero;
import net.fwitz.math.plot.complex.escape.EscapeTimePlot;

import java.util.function.Function;

import static net.fwitz.math.fractal.escape.newton.NewtonsMethod.newtonsMethod;

public class NewtonsMethodPlot {
    private NewtonsMethodPlot() {
    }

    public static void renderEscape(String name, EscapeFunction escapeFn) {
        new EscapeTimePlot(name)
                .computeFn(escapeFn)
                .domainBound(-3.5, -2.5, 3.5, 2.5)
                .colorFn(new NewtonsMethodDarkZero())
                .render();
    }

    public static void render(String name, Function<Complex, Complex> f) {
        renderEscape(name, newtonsMethod(f));
    }

    public static void render(String name, Function<Complex, Complex> f, Function<Complex, Complex> df) {
        renderEscape(name, newtonsMethod(f, df));
    }

    public static void zPowMinus1(double power) {
        render("z^" + power + " - 1 (Newton's Method)",
                z -> z.pow(power).minus(1),
                z -> z.pow(power - 1).times(power));
    }

    public static void zPowMinus1(Complex power) {
        render("z^" + power + " - 1",
                z -> z.pow(power).minus(1),
                z -> z.pow(power.minus(1)).times(power));
    }
}
