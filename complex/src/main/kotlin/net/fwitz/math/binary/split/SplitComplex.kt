package net.fwitz.math.binary.split

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.binary.RealMath.ratanh
import net.fwitz.math.binary.RealMath.rcos
import net.fwitz.math.binary.RealMath.rcosh
import net.fwitz.math.binary.RealMath.rexp
import net.fwitz.math.binary.RealMath.rsin
import net.fwitz.math.binary.RealMath.rsinh
import net.fwitz.math.binary.RealMath.rsqrt
import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.binary.split.SplitComplex.Classification.NAN
import java.util.function.Function
import kotlin.math.*

@Suppress("MemberVisibilityCanBePrivate")
class SplitComplex(x: Double, y: Double) : BinaryNumber<SplitComplex>(x, y) {
    companion object {
        val NaN = SplitComplex(Double.NaN, Double.NaN)
        val ZERO = SplitComplex(0.0, 0.0)
        val ONE = SplitComplex(1.0, 0.0)

        fun splitComplex(x: Double, y: Double): SplitComplex {
            return SplitComplex(x, y)
        }

        private fun polar(r: Double, theta: Double): SplitComplex {
            return SplitComplex(r * rcosh(theta), r * rsinh(theta))
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val c = splitComplex(-1.0, 0.0)
            val c2 = c.pow2
            val cp2 = c.pow(2.0)
            val c3 = c.pow3
            val cp3 = c.pow(3.0)
            println("c = $c")
            println("c.pow2() = $c2")
            println("c.pow(2) = $cp2")
            println("c.pow3() = $c3")
            println("c.pow(3) = $cp3")
            var z1: SplitComplex = ZERO
            var z2: SplitComplex = ZERO
            for (i in 0..999) {
                z1 = z1 * z1 + c
                z2 = z2 * z2 + c
            }
            println("z1 = $z1")
            println("z2 = $z2")
        }
    }

    constructor(x: Int, y: Double): this(x.toDouble(), y)
    constructor(x: Double, y: Int): this(x, y.toDouble())
    constructor(x: Int, y: Int): this(x.toDouble(), y.toDouble())

    override fun z(x: Double, y: Double) = SplitComplex(x, y)

    override val timesJ get() = SplitComplex(x = y, y = x)
    override fun timesJ(y: Double) = SplitComplex(this.y * y, this.x * y)
    override val timesNegativeJ get() = SplitComplex(x = -y, y = x)
    override fun times(c: BinaryNumber<out SplitComplex>) = SplitComplex(
        x = x * c.x + y * c.y,
        y = x * c.y + y * c.x
    )

    override val divJ get() = timesJ
    override fun divJ(y: Double) = SplitComplex(x = this.y / y, y = this.x / y)
    override val divNegativeJ get() = timesNegativeJ

    override val inverse get() = modulus2.let { if (it == 0.0) NaN else SplitComplex(x / it, -y / it) }

    // See comments on abs() to get started.  This tells us that for the polar decomposition
    //    x + jy = r exp(ja)
    // We know that
    //    x = r cosh a
    //    y = r sinh a
    // From this we can get a as follows::
    //   y/x = (r sinh a) / (r cosh a)
    //   y/x = tanh a
    //     a = atanh(y/x)  [ take atanh of both sides, then swap ]
    override val arg
        get() = when {
            isNaN -> Double.NaN
            else -> {
                val z = region1dual(this)
                // If x is 0, then y must also be 0 or the region mapping would have swapped them.
                if (x == 0.0) 0.0 else ratanh(z.y / z.x)
            }
        }

    // What is 'r' for a polar coordinates conversion in split-complex numbers?
    // Assume that x + jy has a polar decomposition as r exp(ja).  Then:
    //     x + jy = r exp(ja)
    //     x + jy = r [cosh a + j sinh a]
    // 1 and j are orthogonal, so separating the real and imaginary components gives:
    //     x = r cosh a
    //     y = r sinh a
    // To get r, we square both x and y, then subtract, using the identity that (cosh a)^2 - (sinh a)^2 = 1:
    //   x^2 = r^2 (cosh a)^2
    //   y^2 = r^2 (sinh a)^2
    //   x^2 - y^2 = r^2 (cosh a)^2 - r^2 (sinh a)^2   [ Subtracted equations ]
    //   x^2 - y^2 = r^2 [(cosh a)^2 - (sinh a)^2]  [ Factored out r^2 ]
    //   r^2 = x^2 - y^2   [ used the identity and swapped sides ]
    //   r = sqrt(|x^2 - y^2|)
    /**
     * {@inheritDoc}
     *
     *
     * **WARNING**: See terminology note on [.modulus2]!
     */
    override val abs get() = rsqrt(abs2)
    override val abs2 get() = modulus2.absoluteValue

    // The naive approach is to return
    //     log(sqrt(abs(x * x - y + y)))
    // but this risks an excessive loss of precision for small values.  To prevent this, we rewrite the expression
    // with a substitution of variables.  First, signs do not matter, but since we need to take a square root and
    // and we're taking the different of two squares, we need to decide which one is smaller up front.  To keep the
    // structure of this as similar as possible to what we have in Complex.logabs(), let's make "a" smaller and
    // swap the terms inside the sqrt to take this into account so we can skip a call to abs().  We can then
    // restructure the expression as follows:
    //     log(sqrt(abs(a^2 - b^2))
    //     log(sqrt(b^2 - a^2))            [ a < b ]
    //     log(sqrt(b^2 - b^2 * (a/b)^2))  [ a^2 = b^2 * (a/b)^2 ]
    //     log(sqrt(b^2 - b^2 * u^2))      [ defining u = a/b ]
    //     log(sqrt(b^2 * (1 - u^2)))      [ factoring out b^2 within the sqrt ]
    //     log(b * sqrt(1 - u^2))          [ moving the b^2 outside the sqrt as b ]
    //     log(b) + log(sqrt(1 - u^2))     [ log(xy) = log(x) + log(y) ]
    //     log(b) + log((1 - u^2) ^ 0.5)   [ sqrt(x) = x^0.5 ]
    //     log(b) + 0.5 * log(1 - u^2)     [ log(x^y) = y * log(x) ]
    //     log(b) + 0.5 * log1p(-u^2)      [ log1p(x) = log(1 + x) ]
    // log1p yields its best results when values are small, so assign the smaller value to a to ensure that
    // u = a/b <= 1.  Of course, this last case is also a hazard.  If a == b, then this is effectively asking
    // us for the log of 0, which isn't defined.
    /**
     * Returns the natural logarithm of this split complex number's [absolute value][.abs], `log |z|`.
     */
    override val logabs get() = when {
        y == 0.0 -> ln(x.absoluteValue)
        x == 0.0 -> ln(y.absoluteValue)
        else -> {
            val absX = x.absoluteValue
            val absY = y.absoluteValue
            when {
                absX > absY -> {
                    val u = absY / absX
                    ln(absX) + 0.5 * ln1p(-u * u)
                }
                absX == absY -> Double.NEGATIVE_INFINITY
                else -> {
                    val u = absX / absY
                    ln(absY) + 0.5 * ln1p(-u * u)
                }
            }
        }
    }

    /**
     * The modulus-squared for this point.
     *
     * **WARNING**: The terminology for split-complex numbers is not well established and some of the
     * most commonly used conventions are very confusing.  For example, it is common in the literature to refer
     * to the value `X^2 - Y^2` as the "modulus", but this is confusing because when it comes to things like
     * the `r` to use for polar decomposition, the **square root** of (the absolute value of)
     * this value must be used.  Its meaning therefore parallels [Complex.abs2], not [Complex.abs].
     *
     * Yet there is also a major difference in behavior as compared to `Complex.abs2()`: the value is negative
     * when `|y| > |x|`.  Calling this method `abs2()` is therefore obviously not a good idea.  So to
     * keep confusion to a minimum, this code uses the following conventions:
     *
     *  * `abs`  used for the non-negative polar decomposition distance, which parallels
     * [Complex.abs] in form and function
     *  * `abs2`  *avoided*
     *  * `modulus`  *avoided*
     *  * `modulus2`  (also "modulus-squared") used for the raw distance metric `X^2 - Y^2`,
     * which parallels [Complex.abs2] in form and function **except that it can be
     * negative**.
     *
     *
     * @return the modulus-squared for this point.  **WARNING**: See terminology note above!
     */
    val modulus2 get() = (x + y) * (x - y)  // x^2 - y^2, rearranged to minimize cancellation error

    override val exp
        get() = when {
            // Ensure we don't end up with -0.0 for either part
            x == 0.0 && !y.isNaN() -> ZERO
            else -> rexp(x).let { r -> SplitComplex(r * cosh(y), r * sinh(y)) }
        }

    override fun pow(a: Double) = when {
        a == 0.0 -> ONE
        a == 1.0 -> this
        a == -1.0 -> inverse
        x == 0.0 && y == 0.0 -> ZERO
        isNaN || a.isNaN() -> NaN

        // Null vectors don't have a polar form, so exponentiation must be handled as a special case for them.
        // For whole numbers, we can find by repeated multiplication that (x + jx)^n or (x - jx)^n remains on
        // on the same null vector (with signs preserved). Additionally, x -> 2^(n-1) * x^n. For example,
        // (x - jx)^5 = 16x - j16. Extrapolating to non-integer powers and simplifying to have only one
        // exponentiation, we get that the magnitude of the terms is (2|x|)^a / 2.
        x == y -> {
            val magnitude = 0.5 * (2 * abs(x)).pow(a)
            val value = magnitude.withSign(x)
            SplitComplex(value, value)
        }

        x == -y -> {
            val magnitude = 0.5 * (2 * abs(x)).pow(a)
            val value = magnitude.withSign(x)
            SplitComplex(value, -value)
        }

        // As all the null vectors have already been handled, we can safely assume we can map to Region I.
        // This means that x is positive and larger in magnitude than y.
        else -> region1mapped { z ->
            val absY = z.y.absoluteValue
            val u = absY / z.x
            val logr = ln(z.x) + 0.5 * ln1p(-u * u)
            val theta = ratanh(z.y / z.x)
            polar(rexp(a * logr), a * theta)
        }
    }

    /**
     * {@inheritDoc}
     * >
     *
     *
     * The function that is returned is self-inverting, but it is only valid for this point and other points
     * with the same [Classification].  Points in other regions will be mapped consistently,
     * but not to `REGION_1`.  For example, if `region1dual()` is called on the value `2 - j5`,
     * that means it is in [REGION_IV][Classification.REGION_IV], where negative `y` values dominate.
     * The returned function is then [SplitComplex.timesNegativeJ], which reflects values over the line
     * `y = -x`, swapping regions `I` and `IV`.  Calling it on the original value will result
     * in `5 - 2j` (the values are swapped and negated).  Repeating this operation restores the original
     * value, `2 - j5`.
     *
     *
     * However, this operation also swaps regions `II` and `III`, so if you hold onto the function
     * and call it on `-5 + 2j`, the result will be `-2 + 5j`, not `5 - 2j` as would be
     * expected when mapping that value to `REGION_I` directly.
     *
     *
     * Note that any value that isn't in region `II`, `III`, or `IV` just returns
     * [Function.identity], as either the value is already in `I` or there is no way to
     * map it there.
     *
     */
    override val region1dual: (SplitComplex) -> SplitComplex
        get() {
            // For speed, test directly instead of using classify()
            if (y > x) {
                // REGION_II
                if (y > -x) return { it.timesJ }

                // REGION_III
                if (y < -x) return { -it }
            } else if (y < x) {
                // REGION_IV
                if (y < -x) return { it.timesNegativeJ }
            }

            // Already in REGION_I, or something else that can't be mapped onto it
            return { it }
        }

    override val log get() = SplitComplex(logabs, arg)

    // From the taylor expansion of sin:
    //    sin(a) = a - a^3/3! + a^5/5! - a^7/7! + ...
    //    sin(jy) = jy - (jy)^3/3! + (jy)^5/5! - (jy)^7/7! + ...   [ substitute a = jy ]
    //    sin(jy) = jy - j(y^3)/3! + j(y^5)/5! - j(y^7)/7! + ...   [ j^n = j for any odd n ]
    //    sin(jy) = j[y - y^3/3! + y^5/5! - y^7/7! + ...           [ factor out j ]
    //    sin(jy) = j sin y                                        [ def. of sin ]
    // Doing the same for cos, we find that it has only even powers, so all terms use an even power of j,
    // which is just 1, with the result that:
    //    cos(jy) = cos y
    // Then we can apply the angle addition formula:
    //    sin(a + b) = sin(a) cos(b) + cos(a) sin(b)
    //    sin(x + jy) = sin(x) cos(jy) + cos(x) sin(jy)            [ substitute a = x, b = jy ]
    //    sin(x + jy) = sin(x) cos(y) + j cos(x) sin(y)            [ sin(jy) = j sin(y), cos(jy) = cos(y) ]
    override val sin
        get() = SplitComplex(
            x = rsin(x) * rcos(y),
            y = rcos(x) * rsin(y)
        )

    // Reusing the ground work for sin(), we can apply the angle addition formula:
    //    cos(a + b) = cos(a) cos(b) - sin(a) sin(b)
    //    cos(x + jy) = cos(x) cos(jy) - sin(x) sin(jy)            [ substitute a = x, b = jy ]
    //    cos(x + jy) = cos(x) cos(y) - j sin(x) sin(y)            [ sin(jy) = j sin(y), cos(jy) = cos(y) ]
    override val cos
        get() = SplitComplex(
            x = rcos(x) * rcos(y),
            y = -rsin(x) * rsin(y)
        )

    // For sinh, the arguments are similar to those for sin and cos.
    // For sinh, all of the powers of j are odd, so there is a single common j that can be factored out, giving:
    //    sinh(jy) = j sinh(y)
    // For cosh, all of the powers of j are even, so all the j coefficients have no effect, giving:
    //    cosh(jy) = cosh(y)
    // Applying the angle addition formula gives us:
    //    sinh(a + b) = sinh(a) cosh(b) + cosh(a) sinh(b)
    //    sinh(x + jy) = sinh(x) cosh(jy) + cosh(x) sinh(jy)   [ Substitute a = x, b = jy ]
    //    sinh(x + jy) = sinh(x) cosh(y) + j cosh(x) sinh(y)   [ sinh(jy) = j sinh(y), cosh(jy) = cosh(y) ]
    override val sinh
        get() = SplitComplex(
            x = rsinh(x) * rcosh(y),
            y = rcosh(x) * rsinh(y)
        )

    // Applying the angle addition formula gives:
    //    cosh(a + b) = cosh(a) cosh(b) + sinh(a) sinh(b)
    //    cosh(x + jy) = cosh(x) cosh(jy) + sinh(x) sinh(jy)   [ Substitute a = x, b = jy ]
    //    cosh(x + jy) = cosh(x) cosh(y) + j sinh(x) sinh(y)   [ sinh(jy) = j sinh(y), cosh(jy) = cosh(y) ]
    override val cosh
        get() = SplitComplex(
            x = rcosh(x) * rcosh(y),
            y = rsinh(x) * rsinh(y)
        )

    /**
     * @see Classification
     */
    fun classify(): Classification {
        if (y > -x) {
            if (y < x) return Classification.REGION_I
            if (y > x) return Classification.REGION_II
        } else if (y < -x) {
            if (y > x) return Classification.REGION_III
            if (y < x) return Classification.REGION_IV
        }

        return when {
            isNaN -> NAN

            // We are on y = x or y = -x
            x == 0.0 -> Classification.ZERO
            x == y -> Classification.POS_NULL_VECTOR
            else -> Classification.NEG_NULL_VECTOR
        }
    }

    override fun toString() = when {
        y.isNaN() -> "($x+j$y)"
        y < 0 -> (-y).let { "(${x}-j$it)" }
        else -> "($x+j$y)"
    }

    /**
     * Identifies the broad categorization of the point provided.
     *
     *
     * The hyperbolic plane is divided into four regions by the null vector axes `y = x` and `y = -x`:
     *
     * ```
     *      \  II   / (positive null vectors)
     *       \     / y = x
     *        \   /
     *         \ /
     *    III   0   I
     *         / \
     *        /   \
     *       /     \ y = -x
     *      /  IV   \ (negative null vectors)
     * ```
     *
     *
     * The following tests are used to determine which region the point is in:
     *
     *
     *  * **`y > -x`**  to the upper-right of `y = -x`, so in [.REGION_I] or
     * [.REGION_II] (or on [y = x][.POS_NULL_VECTOR] between them).
     *  * **`y > x`**  to the upper-left of `y = x`, so in [.REGION_II] or
     * [.REGION_III] (or on [y = -x][.NEG_NULL_VECTOR] between them).
     *  * **`y < -x`**  to the lower-left of `y = -x`, so in [.REGION_III] or
     * [.REGION_IV] (or on [y = x][.POS_NULL_VECTOR] between them).
     *  * **`y < x`**  to the lower-right of `y = x`, so in [.REGION_I] or
     * [.REGION_IV] (or on [y = -x][.NEG_NULL_VECTOR] between them).
     *
     * If no pair of these conditions is met, then the point does not belong to any region.  The possibilities are:
     *
     *  * **[.NAN]**  either `x` or `y` is [Double.NaN]
     *  * **[.ZERO]**  both `x` and `y` are `0`
     *  * **[.POS_NULL_VECTOR]**  on the line `y = x`, but not at `0`.
     *  * **[.NEG_NULL_VECTOR]**  on the line `y = -x`, but not at `0`.
     *
     */
    enum class Classification(
        /**
         * A representative point that is included in this region.
         * With the exception of [NAN], all `basis()` values have X and Y coordinates of
         * that are `-1`, `0`, or `1`.
         */
        basis: SplitComplex
    ) {
        /**
         * The X value, Y value, or both were [Double.NaN].
         */
        NAN(NaN),

        /**
         * True zero (both X and Y have that value).
         */
        ZERO(SplitComplex.ZERO),

        /**
         * Not [.ZERO], but on the line `y = x`.
         */
        POS_NULL_VECTOR(splitComplex(1.0, 1.0)),

        /**
         * Not [.ZERO], but on the line `y = -x`.
         */
        NEG_NULL_VECTOR(splitComplex(1.0, -1.0)),

        /**
         * In the positive X-axis's quadrant, as divided by the lines `y = x` and `y = -x`.
         * In Region I, `x > 0` and `|x| > |y|`, so positive `x` values dominate.
         * This is the "primary region" selected by [SplitComplex.region1mapped] .
         */
        REGION_I(splitComplex(1.0, 0.0)),

        /**
         * In the positive Y-axis's quadrant, as divided by the lines `y = x` and `y = -x`.
         * In Region II, `y > 0` and `|y| > |x|`, so positive `y` values dominate.
         */
        REGION_II(splitComplex(0.0, 1.0)),

        /**
         * In the negative X-axis's quadrant, as divided by the lines `y = x` and `y = -x`.
         * In Region III, `x < 0` and `|x| > |y|`, so negative `x` values dominate.
         */
        REGION_III(splitComplex(-1.0, 0.0)),

        /**
         * In the negative Y-axis's quadrant, as divided by the lines `y = x` and `y = -x`.
         * In Region IV, `y < 0` and `|y| > |x|`, so negative `y` values dominate.
         */
        REGION_IV(splitComplex(0.0, -1.0));

        val isNullVector = basis.x == basis.y || basis.x == -basis.y
    }
}