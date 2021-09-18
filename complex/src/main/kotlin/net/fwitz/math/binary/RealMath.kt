package net.fwitz.math.binary

import net.fwitz.math.binary.complex.functions.Erf
import kotlin.math.*

/**
 * Utility public static class that repackages many of the methods from [Math] under different names to
 * avoid clashes and adds a few that were missing.
 *
 * **WARNING**: All of the methods in this class are purely real-valued functions.  For example,
 * [rsqrt(-1)][.rsqrt] returns [Double.NaN], not [Complex.I].  For those functions
 * like square root whose domain gets extended under Complex analysis, look for a corresponding public static method
 * on [Complex], such as [Complex.sqrt].
 */
object RealMath {
    fun rabs(a: Double) = a.absoluteValue
    fun racos(a: Double) = acos(a)
    fun racosh(a: Double) = ln(a + sqrt(a * a - 1))
    fun rasin(a: Double) = asin(a)
    fun ratan(a: Double) = atan(a)

    // atanh(x) = 0.5 * log[ (1+x) / (1-x) ]
    //          = 0.5 * (log(1 + x) - log(1 - x))
    //          = 0.5 * (log1p(x) - log1p(-x))
    //          = 0.5 * log1p(x) - 0.5 * log1p(-x)
    fun ratanh(x: Double) = 0.5 * ln1p(x) - 0.5 * ln1p(-x)
    fun rcos(a: Double) = cos(a)
    fun rcosh(a: Double) = cosh(a)
    fun rexp(a: Double) = exp(a)
    fun rhypot(x: Double, y: Double) = hypot(x, y)
    fun rlog(a: Double) = ln(a)
    fun rlog1p(x: Double) = ln1p(x)
    fun rsin(a: Double) = sin(a)
    fun rsinh(a: Double) = sinh(a)

    /**
     * @see Complex.sqrt
     */
    fun rsqrt(a: Double) = sqrt(a)
    fun rtan(a: Double) = tan(a)
    fun rtanh(a: Double) = tanh(a)
    fun rerf(a: Double) = Erf.erf(a)
    fun rerfc(a: Double) = Erf.erfc(a)
    fun rerfi(a: Double) = Erf.erfi(a)
}