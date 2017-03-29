package net.fwitz.math.fractal.escape;

import net.fwitz.math.binary.complex.Complex;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An escape time calculation function.
 */
public interface EscapeFunction extends Function<Complex, EscapeTimeResult> {
    /**
     * @return a builder for creating an escape time function that behaves in a fairly standard way
     */
    static Builder builder() {
        return DefaultEscapeFunction.builder();
    }

    /**
     * The escape time calculation.
     *
     * @param c the complex value at which to perform the escape time calculation
     * @return the result of the escape time calculation
     */
    @Override
    EscapeTimeResult apply(Complex c);

    /**
     * A helpful builder for constructing an escape time function.
     */
    interface Builder {
        /**
         * The initialization step.
         * In some situations, the initial step is known to take a special form and may be cheaper to calculate.
         * If no initial function is specified, then {@link Function#identity()} is assumed.
         *
         * @param init a function to map the initial parameter {@code c} passed to {@link EscapeFunction#apply(Complex)}
         *             to some other value.  See {@link #includeInit()} for why this is useful.
         */
        Builder init(Function<Complex, Complex> init);

        /**
         * Interprets the {@link #init(Function)} function as performing the first iteration instead of a trivial
         * initialization.  For many escape time functions, the initial iteration has a special form that can be
         * handled efficiently as a special case.
         * <p>
         * For example, the Mandelbrot Set iteration initializes with {@code 0} as its value, and the first iteration
         * consists of squaring that {@code 0} and adding our input parameter {@code c} to it.  Obviously, squaring
         * {@code 0} and adding it to anything it pointless.  But the default {@link #init(Function)} function can
         * be used as-is to assign {@code c} as the output of the first iteration, so that's actually perfect.
         * <p>
         * Except that then we wouldn't normally count that as an iteration, so the iteration count would be off by
         * one.  Using this {@code includeInit()} method marks {@code init} as being the first iteration rather than
         * a trivial initialization step.  By default, the {@code init} method is not counted as an iteration.
         */
        Builder includeInit();

        /**
         * Specifies a short-circuit test that if {@code true} indicates that the value can be assumed to escape
         * without performing any iteration testing on it.
         * <p>
         * For example, the Mandelbrot Set's main cardioid and circular nodes are relatively cheap to check for and
         * and can make up a large portion of some of its images.  Provided there is no other reason to perform the
         * iteration (such as caring about cycle periods or final values), then testing for these two special cases
         * can save a lot of time.
         * <p>
         * If this parameter is not specified, then it is assumed to return false, indicating that all input values
         * must be tested normally.
         *
         * @param shortcutContainmentTest a cheap containment test function that can avoid iterating on values that
         *                                are known to be contained without doing the work
         */
        Builder shortcutContainmentTest(Predicate<Complex> shortcutContainmentTest);

        /**
         * The test that indicates a point has not yet escaped the iteration conditions.
         * <p>
         * For example, the Mandelbrot set condition could be written as {@code z -> z.abs() < 2} or perhaps
         * {@code z -> z.abs2() < 4}.
         * <p>
         * Either a {@code containmentTest} or an {@link #escapeTest(Predicate) escapeTest}
         * <strong>MUST</strong> be provided.
         *
         * @param containmentTest a test that indicates the value is still within the set, returning {@code true}
         *                        to continue iterating or {@code false} to indicate that the value has escaped
         */
        Builder containmentTest(Predicate<Complex> containmentTest);

        /**
         * The test that indicates a point has escaped the iteration conditions.
         * <p>
         * This is just an alternate phrasing of {@link #containmentTest(Predicate)} for when specifying the exit
         * condition is more intuitive.
         * <p>
         * Either a {@link #containmentTest(Predicate) containmentTest} or an {@code escapeTest escapeTest}
         * <strong>MUST</strong> be provided.
         *
         * @param escapeTest a test that indicates the value has escaped the set, returning {@code false}
         *                   to continue iterating or {@code true} to indicated that the value has escaped
         */
        Builder escapeTest(Predicate<Complex> escapeTest);

        /**
         * The function to apply during each iteration step.
         * <p>
         * For example, the Mandelbrot Set uses {@code (c, z) -> z.pow2().plus(c)}.
         * <p>
         * The step function <strong>MUST</strong> be provided.
         *
         * @param step the iteration function
         */
        Builder step(BiFunction<Complex, Complex, Complex> step);

        /**
         * The maximum number of iterations to perform.
         * <p>
         * The maximum iteration count <strong>MUST</strong> be provided.
         *
         * @param maxIters the positive number of iterations to perform before considering the value to be contained.
         */
        Builder maxIters(int maxIters);

        /**
         * @return the new escape function
         */
        EscapeFunction build();
    }
}

