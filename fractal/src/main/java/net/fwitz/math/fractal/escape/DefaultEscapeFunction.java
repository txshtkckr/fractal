package net.fwitz.math.fractal.escape;

import net.fwitz.math.complex.Complex;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

class DefaultEscapeFunction implements EscapeFunction {
    private final Function<Complex, Complex> init;
    private final Predicate<Complex> shortcutContainmentTest;
    private final Predicate<Complex> escapeTest;
    private final BiFunction<Complex, Complex, Complex> step;
    private final int maxIters;
    private final boolean includeInit;

    DefaultEscapeFunction(DefaultBuilder builder) {
        this.init = builder.init;
        this.shortcutContainmentTest = builder.shortcutContainmentTest;
        this.escapeTest = requireNonNull(builder.escapeTest, "escapeTest or containmentTest");
        this.step = requireNonNull(builder.step, "step");
        this.maxIters = requireNonNull(builder.maxIters, "maxIters");
        this.includeInit = builder.includeInit;
    }

    public EscapeTimeResult apply(Complex c) {
        // Note that if a shortcut test is used, then the fractal generation is faster, but since no
        // iterations are performed, the final value is the initial value rather than whatever iteration
        // would have produced.
        if (shortcutContainmentTest.test(c)) {
            return EscapeTimeResult.contained(maxIters, c);
        }

        int i = includeInit ? 1 : 0;
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
        Predicate<Complex> shortcutContainmentTest = c -> false;
        boolean includeInit;

        Predicate<Complex> escapeTest;
        BiFunction<Complex, Complex, Complex> step;
        Integer maxIters;

        private DefaultBuilder() {
        }

        int maxIters() {
            return maxIters;
        }

        public Builder init(Function<Complex, Complex> init) {
            this.init = requireNonNull(init);
            return this;
        }

        @Override
        public Builder shortcutContainmentTest(Predicate<Complex> shortcutContainmentTest) {
            this.shortcutContainmentTest = requireNonNull(shortcutContainmentTest);
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

        public Builder includeInit() {
            this.includeInit = true;
            return this;
        }

        public EscapeFunction build() {
            return new DefaultEscapeFunction(this);
        }
    }
}
