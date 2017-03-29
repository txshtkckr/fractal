package net.fwitz.math.main.binary.escape.newton;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeFunction;
import net.fwitz.math.plot.binary.escape.color.NewtonsMethodDarkZero;
import net.fwitz.math.plot.binary.escape.EscapeTimePlot;

import java.util.function.Function;

import static net.fwitz.math.fractal.escape.newton.NewtonsMethod.newtonsMethod;

public class NewtonsMethodPlot {
    private NewtonsMethodPlot() {
    }

    public static void renderEscape(String name, EscapeFunction escapeFn) {
        EscapeTimePlot.complex(name)
                .computeFn(escapeFn)
                .domainX(-3.5, 3.5)
                .domainY(-2.5, 2.5)
                .size(700, 500)
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
