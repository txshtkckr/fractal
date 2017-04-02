package net.fwitz.math.fractal.escape;

import net.fwitz.math.binary.complex.Complex;

import java.util.OptionalDouble;
import java.util.function.BiFunction;
import java.util.function.Consumer;
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
    private final OptionalDouble convergenceAdjust;
    private final OptionalDouble divergenceAdjust;

    DefaultEscapeFunction(DefaultBuilder builder) {
        this.init = builder.init;
        this.shortcutContainmentTest = builder.shortcutContainmentTest;
        this.escapeTest = requireNonNull(builder.escapeTest, "escapeTest or containmentTest");
        this.step = requireNonNull(builder.step, "step");
        this.maxIters = requireNonNull(builder.maxIters, "maxIters");
        this.includeInit = builder.includeInit;
        this.convergenceAdjust = builder.convergenceSmoothingFactor.isPresent()
                ? OptionalDouble.of(builder.convergenceSmoothingFactor.getAsDouble() / maxIters)
                : OptionalDouble.empty();
        this.divergenceAdjust = builder.divergenceSmoothingFactor.isPresent()
                ? OptionalDouble.of(builder.divergenceSmoothingFactor.getAsDouble() / maxIters)
                : OptionalDouble.empty();
    }

    public EscapeTimeResult apply(Complex c) {
        Complex zOld = Complex.ZERO;

        // Note that if a shortcut test is used, then the fractal generation is faster, but since no
        // iterations are performed, the final value is the initial value rather than whatever iteration
        // would have produced and no convergence information is available.
        if (shortcutContainmentTest.test(c)) {
            return EscapeTimeResult.contained(c, OptionalDouble.empty());
        }

        int i = 0;
        Complex z = init.apply(c);
        Smoother cSmooth = convergenceAdjust.isPresent()
                ? new ConvergenceSmoother(convergenceAdjust.getAsDouble(), includeInit ? z : c)
                : Smoother.NULL;
        Smoother dSmooth = divergenceAdjust.isPresent()
                ? new DivergenceSmoother(divergenceAdjust.getAsDouble())
                : Smoother.NULL;

        if (includeInit) {
            ++i;
            cSmooth.accept(z);
            dSmooth.accept(z);
        }

        if (escapeTest.test(z)) {
            return escaped(i, z, dSmooth);
        }

        while (++i <= maxIters) {
            z = step.apply(c, z);
            cSmooth.accept(z);
            dSmooth.accept(z);
            if (escapeTest.test(z)) {
                return escaped(i, z, dSmooth);
            }
        }

        return contained(z, cSmooth);
    }

    private EscapeTimeResult escaped(int iters, Complex z, Smoother dSmooth) {
        return EscapeTimeResult.escaped(iters, z, dSmooth.finish());
    }

    private EscapeTimeResult contained(Complex z, Smoother cSmooth) {
        return EscapeTimeResult.contained(z, cSmooth.finish());
    }

    static Builder builder() {
        return new DefaultBuilder();
    }


    private interface Smoother extends Consumer<Complex> {
        Smoother NULL = z -> {
        };

        default OptionalDouble finish() {
            return OptionalDouble.empty();
        }
    }

    private static class ConvergenceSmoother implements Smoother {
        private final double adjust;

        private Complex zOld;
        private double sum;

        private ConvergenceSmoother(double adjust, Complex zOld) {
            this.adjust = adjust;
            this.zOld = zOld;
        }

        @Override
        public void accept(Complex z) {
            sum += adjust * Math.exp(-1 / zOld.minus(z).abs());
            zOld = z;
        }

        @Override
        public OptionalDouble finish() {
            return OptionalDouble.of(sum);
        }
    }

    private static class DivergenceSmoother implements Smoother {
        private final double adjust;

        private double sum;

        private DivergenceSmoother(double adjust) {
            this.adjust = adjust;
        }

        @Override
        public void accept(Complex z) {
            sum += adjust * Math.exp(-1 / z.abs());
        }

        @Override
        public OptionalDouble finish() {
            return OptionalDouble.of(sum);
        }
    }


    private static class DefaultBuilder implements Builder {
        Function<Complex, Complex> init = c -> c;
        Predicate<Complex> shortcutContainmentTest = c -> false;
        boolean includeInit;
        OptionalDouble convergenceSmoothingFactor = OptionalDouble.empty();
        OptionalDouble divergenceSmoothingFactor = OptionalDouble.empty();

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

        @Override
        public Builder enableConvergenceSmoothing(double adjustFactor) {
            convergenceSmoothingFactor = OptionalDouble.of(adjustFactor);
            return this;
        }

        @Override
        public Builder enableDivergenceSmoothing(double adjustFactor) {
            divergenceSmoothingFactor = OptionalDouble.of(adjustFactor);
            return this;
        }

        public EscapeFunction build() {
            return new DefaultEscapeFunction(this);
        }
    }
}
