package net.fwitz.math.fractal.escape;

import net.fwitz.math.complex.Complex;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface EscapeFunction extends Function<Complex, EscapeTimeResult> {
    static Builder builder() {
        return DefaultEscapeFunction.builder();
    }

    interface Builder {
        Builder init(Function<Complex, Complex> init);
        Builder containmentTest(Predicate<Complex> containmentTest);
        Builder escapeTest(Predicate<Complex> escapeTest);
        Builder step(BiFunction<Complex, Complex, Complex> step);
        Builder maxIters(int maxIters);
        Builder excludeInit();
        EscapeFunction build();
    }
}

