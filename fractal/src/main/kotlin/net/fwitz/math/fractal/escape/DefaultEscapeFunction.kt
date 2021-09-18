package net.fwitz.math.fractal.escape

import net.fwitz.math.binary.BinaryNumber
import kotlin.math.exp

internal class DefaultEscapeFunction<T : BinaryNumber<T>> private constructor(
    builder: DefaultBuilder<T>
) : EscapeFunction<T> {
    private val init: (T) -> T
    private val shortcutContainmentTest: (T) -> Boolean
    private val escapeTest: (T) -> Boolean
    private val step: (T, T) -> T
    private val maxIters: Int
    private val includeInit: Boolean
    private val convergenceAdjust: Double?
    private val divergenceAdjust: Double?

    companion object {
        fun <T : BinaryNumber<T>> builder(): EscapeFunction.Builder<T> {
            return DefaultBuilder()
        }
    }

    override fun invoke(c: T): EscapeTimeResult<T> {
        // Note that if a shortcut test is used, then the fractal generation is faster, but since no
        // iterations are performed, the final value is the initial value rather than whatever iteration
        // would have produced and no convergence information is available.
        if (shortcutContainmentTest(c)) return EscapeTimeResult.contained(c, null)

        var i = 0
        var z = init(c)
        val cSmooth: Smoother<T> =
            if (convergenceAdjust == null) NullSmoother()
            else ConvergenceSmoother(convergenceAdjust, if (includeInit) z else c)
        val dSmooth: Smoother<T> =
            if (divergenceAdjust == null) NullSmoother()
            else DivergenceSmoother(divergenceAdjust)

        if (includeInit) {
            ++i
            cSmooth(z)
            dSmooth(z)
        }
        if (escapeTest(z)) return escaped(i, z, dSmooth)

        while (++i <= maxIters) {
            z = step(c, z)
            cSmooth(z)
            dSmooth(z)
            if (escapeTest(z)) return escaped(i, z, dSmooth)
        }

        return contained(z, cSmooth)
    }

    private fun escaped(iters: Int, z: T, dSmooth: Smoother<T>): EscapeTimeResult<T> {
        return EscapeTimeResult.escaped(iters, z, dSmooth.finish())
    }

    private fun contained(z: T, cSmooth: Smoother<T>): EscapeTimeResult<T> {
        return EscapeTimeResult.contained(z, cSmooth.finish())
    }

    interface Smoother<T : BinaryNumber<T>> : (T) -> Unit {
        fun finish(): Double? = null
    }

    private class NullSmoother<T : BinaryNumber<T>> : Smoother<T> {
        override fun invoke(z: T) = Unit
    }

    private class ConvergenceSmoother<T : BinaryNumber<T>>(
        val adjust: Double,
        private var zOld: T
    ) : Smoother<T> {
        private var sum = 0.0

        override fun invoke(z: T) {
            sum += adjust * exp(-1 / (zOld - z).abs)
            zOld = z
        }

        override fun finish() = sum
    }

    private class DivergenceSmoother<T : BinaryNumber<T>>(
        val adjust: Double
    ) : Smoother<T> {
        private var sum = 0.0

        override fun invoke(z: T) {
            sum += adjust * exp(-1 / z.abs)
        }

        override fun finish() = sum
    }

    private class DefaultBuilder<T : BinaryNumber<T>> : EscapeFunction.Builder<T> {
        var init: (T) -> T = { it }
        var shortcutContainmentTest: (T) -> Boolean = { false }
        var includeInit = false
        var convergenceSmoothingFactor: Double? = null
        var divergenceSmoothingFactor: Double? = null
        var escapeTest: ((T) -> Boolean)? = null
        var step: ((T, T) -> T)? = null
        var maxIters: Int? = null

        fun maxIters() = maxIters!!

        override fun init(init: (T) -> T) = this.also {
            this.init = init
        }

        override fun shortcutContainmentTest(shortcutContainmentTest: (T) -> Boolean) = this.also {
            this.shortcutContainmentTest = shortcutContainmentTest
        }

        override fun containmentTest(containmentTest: (T) -> Boolean) = this.also {
            escapeTest = { !containmentTest(it) }
        }

        override fun escapeTest(escapeTest: (T) -> Boolean) = this.also {
            this.escapeTest = escapeTest
        }

        override fun step(step: (T, T) -> T) = this.also {
            this.step = step
        }

        override fun maxIters(maxIters: Int) = this.also {
            require(maxIters >= 1) { "maxIters < 1: $maxIters" }
            this.maxIters = maxIters
        }

        override fun includeInit() = this.also {
            includeInit = true
        }

        override fun enableConvergenceSmoothing(adjustFactor: Double) = this.also {
            convergenceSmoothingFactor = adjustFactor
            return this
        }

        override fun enableDivergenceSmoothing(adjustFactor: Double) = this.also {
            divergenceSmoothingFactor = adjustFactor
            return this
        }

        override fun build(): EscapeFunction<T> {
            return DefaultEscapeFunction(this)
        }
    }

    init {
        init = builder.init
        shortcutContainmentTest = builder.shortcutContainmentTest
        escapeTest = requireNotNull(builder.escapeTest) { "escapeTest or containmentTest" }
        step = requireNotNull(builder.step) { "step" }
        maxIters = requireNotNull(builder.maxIters) { "maxIters" }
        includeInit = builder.includeInit
        convergenceAdjust = builder.convergenceSmoothingFactor?.let { it / maxIters }
        divergenceAdjust = builder.divergenceSmoothingFactor?.let { it / maxIters }
    }
}