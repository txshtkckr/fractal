package net.fwitz.math.main.escape;

import net.fwitz.math.complex.Complex;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class EscapeFunction implements Function<Complex, EscapeTimeResult> {
    private final Function<Complex, Complex> init;
    private final Predicate<Complex> escapeTest;
    private final BiFunction<Complex, Complex, Complex> step;
    private final int maxIters;

    private EscapeFunction(Builder builder) {
        this.init = builder.init;
        this.escapeTest = builder.escapeTest;
        this.step = builder.step;
        this.maxIters = builder.maxIters;
    }

    public EscapeTimeResult apply(Complex c) {
        Complex z = init.apply(c);
        if (escapeTest.test(z)) {
            return EscapeTimeResult.escaped(1, maxIters, z);
        }

        for (int i = 2; i <= maxIters; ++i) {
            z = step.apply(c, z);
            if (escapeTest.test(z)) {
                return EscapeTimeResult.escaped(i, maxIters, z);
            }
        }

        return EscapeTimeResult.contained(maxIters, z);
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private Function<Complex, Complex> init = c -> Complex.ZERO;
        private Predicate<Complex> escapeTest = z -> true;
        private BiFunction<Complex, Complex, Complex> step = (c, z) -> Complex.POSITIVE_RE_INFINITY;
        private int maxIters = 1;

        private Builder() {
        }

        int maxIters() {
            return maxIters;
        }

        public Builder init(Function<Complex, Complex> init) {
            this.init = requireNonNull(init);
            return this;
        }

        public Builder containmentTest(Predicate<Complex> containmentTest) {
            this.escapeTest = containmentTest.negate();
            return this;
        }

        public Builder escapeTest(Predicate<Complex> escapeTest) {
            this.escapeTest = requireNonNull(escapeTest);
            return this;
        }

        public Builder step(BiFunction<Complex, Complex, Complex> step) {
            this.step = requireNonNull(step);
            return this;
        }

        public Builder maxIters(int maxIters) {
            if (maxIters < 1) {
                throw new IllegalArgumentException("maxIters < 1: " + maxIters);
            }
            this.maxIters = maxIters;
            return this;
        }

        public EscapeFunction build() {
            return new EscapeFunction(this);
        }
    }

}

