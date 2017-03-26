package net.fwitz.math.plot.color.filter;

import net.fwitz.math.complex.Complex;

import java.util.Arrays;
import java.util.function.Function;

import static net.fwitz.math.complex.Complex.imaginary;
import static net.fwitz.math.complex.Complex.real;

public class ComplexFunctionValueFilters extends ValueFilters<Complex> {
    private static final ComplexFunctionValueFilters INSTANCE = new ComplexFunctionValueFilters();

    public static ComplexFunctionValueFilters getInstance() {
        return INSTANCE;
    }

    private ComplexFunctionValueFilters() {
        super(
                new ValuesFilter<>("Real only", mapEachValue(z -> real(z.re()))),
                new ValuesFilter<>("Imaginary only", mapEachValue(z -> imaginary(z.im()))),
                new ValuesFilter<>("dz/dx", dz()),
                new ValuesFilter<>("d2z/dx2", d2z())
        );
    }

    private static ValuesFilterFunction<Complex> mapEachValue(Function<Complex, Complex> f) {
        return (rv, iv, zv) -> Arrays.stream(zv)
                .map(f)
                .toArray(Complex[]::new);
    }

    private static ValuesFilterFunction<Complex> dz() {
        return ComplexFunctionValueFilters::derivative;
    }

    private static ValuesFilterFunction<Complex> d2z() {
        return (rv, iv, zv) -> derivative(rv, iv, derivative(rv, iv, zv));
    }

    private static Complex[] derivative(double[] rv, double iv, Complex[] zv) {
        if (zv.length < 3) {
            return zv;
        }

        Complex[] dz = new Complex[zv.length];
        double h = rv[1] - rv[0];
        int last = dz.length - 1;
        for (int i = 0; i < last; ++i) {
            dz[i] = zv[i + 1].minus(zv[i]).div(h);
        }

        // Assume that the derivative is stable at the right edge, since we don't have an additional point to use
        dz[last] = dz[last - 1];
        return dz;
    }
}