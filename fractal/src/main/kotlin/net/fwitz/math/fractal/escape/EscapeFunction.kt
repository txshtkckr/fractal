package net.fwitz.math.fractal.escape

import net.fwitz.math.binary.BinaryNumber
import java.util.function.Function

/**
 * An escape time calculation function.
 */
interface EscapeFunction<T : BinaryNumber<T>> : (T) -> EscapeTimeResult<T> {
    companion object {
        /**
         * @return a builder for creating an escape time function that behaves in a fairly standard way
         */
        fun <T : BinaryNumber<T>> builder(): Builder<T> = DefaultEscapeFunction.builder()
    }

    /**
     * The escape time calculation.
     *
     * @param c the complex value at which to perform the escape time calculation
     * @return the result of the escape time calculation
     */
    override fun invoke(c: T): EscapeTimeResult<T>

    /**
     * A helpful builder for constructing an escape time function.
     */
    interface Builder<T : BinaryNumber<T>> {
        /**
         * The initialization step.
         * In some situations, the initial step is known to take a special form and may be cheaper to calculate.
         * If no initial function is specified, then [Function.identity] is assumed.
         *
         * @param init a function to map the initial parameter `c` passed to
         * [EscapeFunction.apply] to some other value.  See [.includeInit] for
         * why this is useful.
         */
        fun init(init: (T) -> T): Builder<T>

        /**
         * Interprets the [.init] function as performing the first iteration instead of a trivial
         * initialization.  For many escape time functions, the initial iteration has a special form that can be
         * handled efficiently as a special case.
         *
         *
         * For example, the Mandelbrot Set iteration initializes with `0` as its value, and the first iteration
         * consists of squaring that `0` and adding our input parameter `c` to it.  Obviously, squaring
         * `0` and adding it to anything it pointless.  But the default [.init] function can
         * be used as-is to assign `c` as the output of the first iteration, so that's actually perfect.
         *
         *
         * Except that then we wouldn't normally count that as an iteration, so the iteration count would be off by
         * one.  Using this `includeInit()` method marks `init` as being the first iteration rather than
         * a trivial initialization step.  By default, the `init` method is not counted as an iteration.
         */
        fun includeInit(): Builder<T>

        /**
         * Specifies a short-circuit test that if `true` indicates that the value can be assumed to escape
         * without performing any iteration testing on it.
         *
         *
         * For example, the Mandelbrot Set's main cardioid and circular nodes are relatively cheap to check for and
         * and can make up a large portion of some of its images.  Provided there is no other reason to perform the
         * iteration (such as caring about cycle periods or final values), then testing for these two special cases
         * can save a lot of time.
         *
         *
         * If this parameter is not specified, then it is assumed to return false, indicating that all input values
         * must be tested normally.
         *
         * @param shortcutContainmentTest a cheap containment test function that can avoid iterating on values that
         * are known to be contained without doing the work
         */
        fun shortcutContainmentTest(shortcutContainmentTest: (T) -> Boolean): Builder<T>

        /**
         * The test that indicates a point has not yet escaped the iteration conditions.
         *
         *
         * For example, the Mandelbrot set condition could be written as `z -> z.abs() < 2` or perhaps
         * `z -> z.abs2() < 4`.
         *
         *
         * Either a `containmentTest` or an [escapeTest][.escapeTest]
         * **MUST** be provided.
         *
         * @param containmentTest a test that indicates the value is still within the set, returning `true`
         * to continue iterating or `false` to indicate that the value has escaped
         */
        fun containmentTest(containmentTest: (T) -> Boolean): Builder<T>

        /**
         * The test that indicates a point has escaped the iteration conditions.
         *
         *
         * This is just an alternate phrasing of [.containmentTest] for when specifying the exit
         * condition is more intuitive.
         *
         *
         * Either a [containmentTest][.containmentTest] or an `escapeTest escapeTest`
         * **MUST** be provided.
         *
         * @param escapeTest a test that indicates the value has escaped the set, returning `false`
         * to continue iterating or `true` to indicated that the value has escaped
         */
        fun escapeTest(escapeTest: (T) -> Boolean): Builder<T>

        /**
         * The function to apply during each iteration step.
         * For example, the Mandelbrot Set uses `(c, z) -> z.pow2 + c`.
         * The step function **MUST** be provided.
         *
         * @param step the iteration function
         */
        fun step(step: (T, T) -> T): Builder<T>

        /**
         * The maximum number of iterations to perform.
         *
         * The maximum iteration count **MUST** be provided.
         *
         * @param maxIters the positive number of iterations to perform before considering the value to be contained.
         */
        fun maxIters(maxIters: Int): Builder<T>

        /**
         * [Enable convergence smoothing][.enableConvergenceSmoothing] using the default adjustment
         * factor of `1.0`.
         */
        fun enableConvergenceSmoothing(): Builder<T> = enableConvergenceSmoothing(1.0)

        /**
         * Enables smoothed magnitude tracking for convergent values so that this information will be included in the
         * [EscapeTimeResult].  Both smoothing algorithms have a cost to them, so they are off by default.
         *
         * @param adjustFactor a multiplier for tuning the smoothing algorithm
         */
        fun enableConvergenceSmoothing(adjustFactor: Double): Builder<T>

        /**
         * [Enable divergence smoothing][.enableDivergenceSmoothing] using the default adjustment
         * factor of `1.0`.
         */
        fun enableDivergenceSmoothing() = enableDivergenceSmoothing(1.0)

        /**
         * Enables smoothed magnitude tracking for divergent values so that this information will be included in the
         * [EscapeTimeResult].  Both smoothing algorithms have a cost to them, so they are off by default.
         *
         * @param adjustFactor a multiplier for tuning the smoothing algorithm
         */
        fun enableDivergenceSmoothing(adjustFactor: Double): Builder<T>

        /**
         * @return the new escape function
         */
        fun build(): EscapeFunction<T>
    }
}