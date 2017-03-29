package net.fwitz.math.plot.binary.escape;

import net.fwitz.math.binary.BinaryNumber;
import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.binary.split.SplitComplex;
import net.fwitz.math.fractal.escape.EscapeTimeResult;
import net.fwitz.math.plot.binary.BinaryNumberFunctionPlot;

import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

public class EscapeTimePlot<T extends BinaryNumber<T>> extends BinaryNumberFunctionPlot<
        EscapeTimePlot<T>,
        EscapeTimePanel<T>,
        T,
        EscapeTimeResult> {

    private final Class<T> binaryNumberType;
    private final BiFunction<Double, Double, ? extends T> valueFactory;

    private EscapeTimePlot(String title, Class<T> binaryNumberType,
                           BiFunction<Double, Double, ? extends T> valueFactory) {
        super(title);
        this.binaryNumberType = requireNonNull(binaryNumberType, "binaryNumberType");
        this.valueFactory = requireNonNull(valueFactory, "valueFactory");
    }

    @Override
    public T value(double x, double y) {
        return valueFactory.apply(x, y);
    }

    @Override
    protected EscapeTimePanel<T> createPanel() {
        return new EscapeTimePanel<>(binaryNumberType, this);
    }

    public static EscapeTimePlot<Complex> complex(String title) {
        return new EscapeTimePlot<>(title, Complex.class, Complex::complex);
    }

    public static EscapeTimePlot<SplitComplex> splitComplex(String title) {
        return new EscapeTimePlot<>(title, SplitComplex.class, SplitComplex::splitComplex);
    }
}
