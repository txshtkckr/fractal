package net.fwitz.math.binary

import kotlin.math.ln
import kotlin.math.ln1p
import kotlin.math.sqrt

/**
 * Some real-valued functions that aren't part of the standard library.
 */
object RealMath {
    fun racosh(a: Double) = ln(a + sqrt(a * a - 1))

    // atanh(x) = 0.5 * log[ (1+x) / (1-x) ]
    //          = 0.5 * (log(1 + x) - log(1 - x))
    //          = 0.5 * (log1p(x) - log1p(-x))
    //          = 0.5 * log1p(x) - 0.5 * log1p(-x)
    fun ratanh(x: Double) = 0.5 * ln1p(x) - 0.5 * ln1p(-x)
}