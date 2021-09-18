package net.fwitz.math.binary.complex.functions

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.binary.complex.analysis.Gamma

@Suppress("unused", "MemberVisibilityCanBePrivate")
object Factorial {
    private val FACTORIAL = longArrayOf(
        1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600,
        6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L,
        121645100408832000L, 2432902008176640000L
    )

    val MAX_INT_VALUE = FACTORIAL.lastIndex

    fun factorial(n: Int) = try {
        FACTORIAL[n]
    } catch (e: ArrayIndexOutOfBoundsException) {
        // Larger values would overflow Long.MAX_VALUE
        throw IllegalArgumentException("n must be non-negative and less than " + FACTORIAL.size + ": " + n)
    }

    fun factorial(n: Double) = Gamma.gamma(n + 1.0)
    fun factorial(z: Complex) = Gamma.gamma(z + 1.0)
}
