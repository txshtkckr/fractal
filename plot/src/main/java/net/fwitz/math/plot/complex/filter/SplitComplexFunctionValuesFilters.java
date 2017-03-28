package net.fwitz.math.plot.complex.filter;

import net.fwitz.math.complex.SplitComplex;

import java.util.Arrays;
import java.util.function.Function;

import static net.fwitz.math.complex.SplitComplex.splitComplex;

public class SplitComplexFunctionValuesFilters extends ValuesFilters<SplitComplex> {
    private static final SplitComplexFunctionValuesFilters INSTANCE = new SplitComplexFunctionValuesFilters();

    public static SplitComplexFunctionValuesFilters getInstance() {
        return INSTANCE;
    }

    private SplitComplexFunctionValuesFilters() {
        super(
                new ValuesFilter<>("X only", mapEachValue(z -> splitComplex(z.x(), 0))),
                new ValuesFilter<>("Y only", mapEachValue(z -> splitComplex(0, z.y()))),
                new ValuesFilter<>("dz/dx", dz()),
                new ValuesFilter<>("d2z/dx2", d2z())
        );
    }

    private static ValuesFilterFunction<SplitComplex> mapEachValue(Function<SplitComplex, SplitComplex> f) {
        return (rv, iv, zv) -> Arrays.stream(zv)
                .map(f)
                .toArray(SplitComplex[]::new);
    }

    private static ValuesFilterFunction<SplitComplex> dz() {
        return SplitComplexFunctionValuesFilters::derivative;
    }

    private static ValuesFilterFunction<SplitComplex> d2z() {
        return (rv, iv, zv) -> derivative(rv, iv, derivative(rv, iv, zv));
    }

    private static SplitComplex[] derivative(double[] rv, double iv, SplitComplex[] zv) {
        if (zv.length < 3) {
            return zv;
        }

        SplitComplex[] dz = new SplitComplex[zv.length];
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
