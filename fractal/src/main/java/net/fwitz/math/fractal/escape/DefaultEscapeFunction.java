package net.fwitz.math.fractal.escape;

import net.fwitz.math.complex.Complex;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

class DefaultEscapeFunction implements EscapeFunction {
    private final Function<Complex, Complex> init;
    private final Predicate<Complex> escapeTest;
    private final BiFunction<Complex, Complex, Complex> step;
    private final int maxIters;
    private final boolean excludeInit;

    DefaultEscapeFunction(DefaultBuilder builder) {
        this.init = builder.init;
        this.escapeTest = builder.escapeTest;
        this.step = builder.step;
        this.maxIters = builder.maxIters;
        this.excludeInit = builder.excludeInit;
    }

    public EscapeTimeResult apply(Complex c) {
        int i = excludeInit ? 0 : 1;
        Complex z = init.apply(c);
        if (escapeTest.test(z)) {
            return EscapeTimeResult.escaped(i, maxIters, z);
        }

        while (++i <= maxIters) {
            z = step.apply(c, z);
            if (escapeTest.test(z)) {
                return EscapeTimeResult.escaped(i, maxIters, z);
            }
        }

        return EscapeTimeResult.contained(maxIters, z);
    }

    static Builder builder() {
        return new DefaultBuilder();
    }


    private static class DefaultBuilder implements Builder {
        Function<Complex, Complex> init = c -> c;
        Predicate<Complex> escapeTest = z -> true;
        BiFunction<Complex, Complex, Complex> step = (c, z) -> Complex.POSITIVE_RE_INFINITY;
        int maxIters = 1;
        boolean excludeInit;

        private DefaultBuilder() {
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

        public Builder excludeInit() {
            this.excludeInit = true;
            return this;
        }

        public EscapeFunction build() {
            return new DefaultEscapeFunction(this);
        }
    }
}
