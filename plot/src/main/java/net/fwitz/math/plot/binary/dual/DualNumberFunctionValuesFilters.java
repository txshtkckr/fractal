package net.fwitz.math.plot.binary.dual;

import net.fwitz.math.binary.dual.DualNumber;
import net.fwitz.math.plot.renderer.filter.ValuesFilter;
import net.fwitz.math.plot.renderer.filter.ValuesFilterFunction;
import net.fwitz.math.plot.renderer.filter.ValuesFilters;

import java.util.Arrays;
import java.util.function.Function;

import static net.fwitz.math.binary.dual.DualNumber.dualNumber;

public class DualNumberFunctionValuesFilters extends ValuesFilters<DualNumber> {
    private static final DualNumberFunctionValuesFilters INSTANCE = new DualNumberFunctionValuesFilters();

    public static DualNumberFunctionValuesFilters getInstance() {
        return INSTANCE;
    }

    private DualNumberFunctionValuesFilters() {
        super(
                new ValuesFilter<>("X only", mapEachValue(z -> dualNumber(z.x(), 0))),
                new ValuesFilter<>("Y only", mapEachValue(z -> dualNumber(0, z.y()))),
                new ValuesFilter<>("dz/dx", dz()),
                new ValuesFilter<>("d2z/dx2", d2z())
        );
    }

    private static ValuesFilterFunction<DualNumber> mapEachValue(Function<DualNumber, DualNumber> f) {
        return (rv, iv, zv) -> Arrays.stream(zv)
                .map(f)
                .toArray(DualNumber[]::new);
    }

    private static ValuesFilterFunction<DualNumber> dz() {
        return DualNumberFunctionValuesFilters::derivative;
    }

    private static ValuesFilterFunction<DualNumber> d2z() {
        return (rv, iv, zv) -> derivative(rv, iv, derivative(rv, iv, zv));
    }

    private static DualNumber[] derivative(double[] rv, double iv, DualNumber[] zv) {
        if (zv.length < 3) {
            return zv;
        }

        DualNumber[] dz = new DualNumber[zv.length];
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
