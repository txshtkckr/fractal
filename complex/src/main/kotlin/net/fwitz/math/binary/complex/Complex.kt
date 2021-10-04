package net.fwitz.math.binary.complex

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.binary.complex.functions.Erf
import kotlin.math.*
import kotlin.math.acos as racos
import kotlin.math.acosh as racosh
import kotlin.math.asin as rasin
import kotlin.math.atan as ratan
import kotlin.math.cos as rcos
import kotlin.math.cosh as rcosh
import kotlin.math.exp as rexp
import kotlin.math.hypot as rhypot
import kotlin.math.sin as rsin
import kotlin.math.sinh as rsinh
import kotlin.math.sqrt as rsqrt
import kotlin.math.tanh as rtanh

/**
 * An immutable complex number.
 *
 *
 * Note that since these values are immutable, a preference has been given to terminology like
 * [plus][.plus] or [times][.times] that connotes a result-yielding
 * operation as opposed to `add`, which might suggest that the operation is mutative and
 * and encourage mistakes.
 *
 */
@Suppress("unused")
class Complex(x: Double, y: Double) : BinaryNumber<Complex>(x, y) {
    companion object {
        /**
         * The maximum number of roots that may be requested using [.roots].
         */
        const val MAX_ROOTS = 10000
        private val NO_ROOTS = emptyList<Complex>()

        val POSITIVE_RE_INFINITY: Complex = real(Double.POSITIVE_INFINITY)
        val NaN: Complex = Complex(Double.NaN, Double.NaN)
        val ZERO: Complex = real(0.0)
        val ONE: Complex = real(1.0)
        val TWO: Complex = real(2.0)
        val E: Complex = real(Math.E)
        val PI: Complex = real(Math.PI)
        val I: Complex = imaginary(1.0)

        private const val A_CROSSOVER = 1.5
        private const val B_CROSSOVER = 0.6417
        private const val TWO_PI = Math.PI * 2.0
        private const val PI_OVER_2 = Math.PI * 0.5
        private val LN_2 = ln(2.0)
        private val LN_2_RECIP = 1.0 / LN_2
        private val LN_10 = ln(10.0)
        private val LN_10_RECIP: Double = 1.0 / LN_10
        private val ONE_OVER_2_I = imaginary(2.0).inverse

        // The logic and rules applied here are taken loosely from the example in Annex G of the C11 standard
        // ("IEC 60559-compatible complex arithmetic"), but tweaked for Java's slightly different rules around
        // infinities and NaN values.
        private fun times(z: Complex, w: BinaryNumber<out Complex>): Complex {
            var a = z.x
            var b = z.y
            var c: Double = w.x
            var d: Double = w.y
            val ac = a * c
            val ad = a * d
            val bc = b * c
            val bd = b * d
            var x = ac - bd
            var y = ad + bc
            if (x.isNaN() || y.isNaN()) {
                var recalc = false
                if (a.isInfinite() || b.isInfinite()) {
                    // "Box" infinity and change NaNs in the other factor to 0
                    a = boxInf(a)
                    b = boxInf(b)
                    c = zeroOutNaN(c)
                    d = zeroOutNaN(d)
                    recalc = true
                }
                if (c.isInfinite() || d.isInfinite()) {
                    // "Box" infinity and change NaNs in the other factor to 0
                    a = zeroOutNaN(a)
                    b = zeroOutNaN(b)
                    c = boxInf(c)
                    d = boxInf(d)
                    recalc = true
                }

                // If we got a NaN, none of the inputs were infinite, but at least one intermediate term was infinite,
                // then we need to recover the infinite result by zeroing out NaNs.  (Otherwise, the NaN result is
                // correct and gets retained.)
                if (!recalc && (ac.isInfinite() || ad.isInfinite() || bc.isInfinite() || bd.isInfinite())) {
                    a = zeroOutNaN(a)
                    b = zeroOutNaN(b)
                    c = zeroOutNaN(c)
                    d = zeroOutNaN(d)
                    recalc = true
                }
                if (recalc) {
                    // recalc now that we have fixed things up (do not reuse the products we calculated earlier!)
                    x = Double.POSITIVE_INFINITY * (a * c - b * d)
                    y = Double.POSITIVE_INFINITY * (a * d + b * c)
                }
            }
            return Complex(x, y)
        }

        private fun div(z: Complex, w: BinaryNumber<out Complex>): Complex {
            val absIm = w.y.absoluteValue
            if (absIm == 0.0) return z.div(w.x)

            var a = z.x
            var b = z.y
            var c = w.x
            var d = w.y
            var x: Double
            var y: Double
            val u: Double
            val denom: Double

            val absRe = c.absoluteValue
            if (absRe < absIm) {
                u = c / d
                denom = c * u + d
                x = (a * u + b) / denom
                y = (b * u - a) / denom
            } else {
                u = d / c
                denom = c + d * u
                x = (a + b * u) / denom
                y = (b - a * u) / denom
            }

            // Recover infinities and zeros that computed as NaN+iNaN;
            // the only cases are nonzero/zero, infinite/finite, and finite/infinite, ...
            if (x.isNaN() && y.isNaN()) {
                if (denom == 0.0 && (!a.isNaN() || !b.isNaN())) {
                    val inf = Double.POSITIVE_INFINITY.withSign(c)
                    x = inf * a
                    y = inf * b
                } else if ((a.isInfinite() || b.isInfinite()) && c.isFinite() && d.isFinite()) {
                    a = boxInf(a)
                    b = boxInf(b)
                    x = Double.POSITIVE_INFINITY * (a * c + b * d)
                    y = Double.POSITIVE_INFINITY * (b * c - a * d)
                } else if ((c.isInfinite() || d.isInfinite()) && a.isFinite() && b.isFinite()) {
                    c = boxInf(c)
                    d = boxInf(d)
                    x = 0.0 * (a * c + b * d)
                    y = 0.0 * (b * c - a * d)
                }
            }
            return Complex(x, y)
        }


        /**
         * Maps an angle theta expressed in radians to the range `[0, 2*PI)`.
         * Most of the functions in this class branch cut along the negative real axis, meaning that they use an
         * angle in the range `[-PI, PI]`, with special cases around `-0.0` determining when `-PI`
         * is preferred over `+PI`.  This function normalizes the range to `[0, 2*PI)`, as required for
         * sorting the roots returned by [.roots].
         *
         * @param theta the angle to normalize
         * @return the angle normalized to `[0, 2*PI)`, corresponding to a branch cut along the positive real axis
         */
        private fun positiveRealBranchCut(theta: Double): Double {
            val th = atan2(rsin(theta), rcos(theta))
            return if (th < 0.0) th + TWO_PI else th
        }

        /**
         * Returns the complex square root of real argument `x`.
         * This is equivalent to [Math.sqrt], except that it returns imaginary roots for negative numbers.
         */
        fun sqrt(x: Double) = if (x >= 0) real(rsqrt(x)) else imaginary(rsqrt(-x))

        //================================================================
        // Inverse trigonometric
        //----------------------------------------------------------------
        // Note that "inverse" here is in reference to inverting the trigonometric
        // function, not a multiplicative inverse as for the inverse() method.
        // The overloading of this term is admittedly unhelpful...
        //----------------------------------------------------------------
        /**
         * Returns the complex inverse sine of the given real value.
         * This is equivalent to [Math.asin], except that its domain is extended by complex analysis.
         * This is also a convenience method equivalent to first forming a complex number and using [.asin]
         * on that, except that this method is slightly more efficient.
         */
        fun asin(a: Double) = when {
            a.absoluteValue <= 1.0 -> real(rasin(a))
            a < 0.0 -> Complex(-PI_OVER_2, racosh(-a))
            else -> Complex(PI_OVER_2, -racosh(a))
        }

        /**
         * Returns the complex inverse sine of the given real value.
         * This is equivalent to [Math.acos], except that its domain is extended by complex analysis.
         * This is also a convenience method equivalent to first forming a complex number and using [.acos]
         * on that, except that this method is slightly more efficient.
         * The algorithm used is from <cite>Hull, T. 1997. Implementing the Complex Arcsine and
         * Arccosine Functions Using Exception Handling. Comm. ACM 23, 3</cite>.
         */
        fun acos(a: Double) = when {
            a.absoluteValue <= 1.0 -> real(racos(a))
            a < 0.0 -> Complex(Math.PI, -racosh(-a))
            else -> imaginary(racosh(a))
        }

        /**
         * Returns the complex inverse hyperbolic cosine of the given real value.
         * This convenience method is equivalent to first forming a complex number and using [.acosh]
         * on that, except that this method is slightly more efficient.
         */
        fun acosh(a: Double) = when {
            a >= 1.0 -> real(racosh(a))
            a >= -1.0 -> imaginary(racos(a))
            else -> Complex(racosh(-a), Math.PI)
        }

        /**
         * Returns the complex inverse hyperbolic tangent of the given real number.
         * This convenience method is equivalent to first forming a complex number and using [.atanh]
         * on that.
         */
        fun atanh(a: Double) = real(a).atanh

        /**
         * Factory method that promotes a simple real number to a complex number with real component `x` and
         * imaginary component `0`.
         *
         * @param re the real component of the complex number
         * @return `x` as a complex number
         */
        fun real(re: Double) = Complex(re, 0.0)
        fun real(re: Int) = real(re.toDouble())

        /**
         * Factory method that promotes a simple real number to a complex number with real component `0` and
         * imaginary component `y`.
         *
         * @param im the imaginary component of the complex number
         * @return `y * i` as a complex number
         */
        fun imaginary(im: Double) = Complex(0.0, im)
        fun imaginary(im: Int) = imaginary(im.toDouble())

        /**
         * Factory method for constructing a complex number from its real and imaginary rectangular components.
         *
         * @param re the real (or "x") component
         * @param im the imaginary (or "y") component
         * @return the resulting complex number
         */
        fun complex(re: Double = 0.0, im: Double = 0.0) = Complex(re, im)
        fun complex(re: Int = 0, im: Double = 0.0) = Complex(re.toDouble(), im)
        fun complex(re: Double = 0.0, im: Int = 0) = Complex(re, im.toDouble())
        fun complex(re: Int = 0, im: Int = 0) = Complex(re.toDouble(), im.toDouble())

        /**
         * Factory method for constructing a complex number from its radial and angular polar coordinate components.
         *
         * @param r     the radial distance of the complex number from the origin; negative values are accepted and behave
         * consistently (that is, `polar(5, 0)` should be approximately equivalent to
         * `polar(-5, PI)`.
         * @param theta the angular rotation counter-clockwise relative to the positive real axis.  The angle's permissible
         * domain is not arbitrarily restricted.
         * @return the complex number corresponding the given polar coordinates.
         */
        fun polar(r: Double, theta: Double): Complex {
            // Ensure we don't end up with -0.0 for either part
            return if (r == 0.0 && !theta.isNaN()) ZERO
            else Complex(r * rcos(theta), r * rsin(theta))
        }

        // compute erfcx(z) = exp(z^2) erfz(z)
        private fun erfcx(z: Complex, relerr: Double) = w(z.timesJ, relerr)

        private fun w(z: Complex, relerr: Double) = Erf.w(z, relerr)

        @JvmStatic
        fun main(args: Array<String>) {
            println(real(Math.PI / 2).sin)
        }
    }

    constructor(x: Int, y: Double): this(x.toDouble(), y)
    constructor(x: Double, y: Int): this(x, y.toDouble())
    constructor(x: Int, y: Int): this(x.toDouble(), y.toDouble())

    //================================================================
    // BinaryNumber helpers
    //----------------------------------------------------------------

    /**
     * Returns this complex number with its imaginary component replaced by the given value.
     * The real component is copied from this complex number without modification.
     */
    override fun z(x: Double, y: Double) = Complex(x, y)

    /**
     * Returns the absolute value of this complex number, also known as its magnitude, `|z|`.
     * This is specified in terms of [Math.hypot], from which it follows that:
     *
     *  * If either component is infinite, the result is positive infinity.
     *  * If either component is NaN (and neither component is infinite), the result is NaN.
     *  * Otherwise, the result is the square root of the sum of the squares of the two components.
     *
     */
    override val abs get() = rhypot(x, y)

    /**
     * Returns the argument of this complex number, `arg(z)`, with a range of `[-PI, PI]`.
     * The value is calculated as per the specification for [Math.atan2].
     * All behaviours regarding sign, infinities, and NaN values are given in its documentation.
     * This function has a branch cut along the negative real axis.
     */
    override val arg get() = atan2(y, x)

    /**
     * Returns the multiplicative inverse ("reciprocal") of this complex number, `1/z`.
     */
    private var inverseCached: Complex? = null
    override val inverse: Complex
        get() {
            val cached = inverseCached
            if (cached != null) return cached

            val re2 = x * x
            val im2 = y * y
            // Prevent real numbers from using -0.0 as the imaginary portion of the inverse, but
            // if both parts were +/-0, then make sure that they both end up NaN.
            return when {
                im2 == 0.0 -> if (re2 == 0.0) NaN else real(1.0 / x)
                re2 == 0.0 -> imaginary(-1.0 / y)
                else -> {
                    // 1 / (a + bi)            [ multiply both top and bottom by the complex conjugate, (a - bi) ]
                    // (a - bi) / (a^2 + b^2)  [ substitute u = a^2 + b^2 ]
                    // (a - bi) / u            [ distribute ]
                    // (a/u) + (-b/u)i
                    val u = re2 + im2
                    Complex(x / u, -y / u)
                }
            }.also { inverseCached = it }
        }

    //================================================================
    // Other simple properties
    //----------------------------------------------------------------

    // Note: This has to explicitly check for either component being infinite to ensure that they override NaN as
    // required.
    /**
     * Returns the square of this complex number's absolute value, `|z|^2`.
     * This is specified in the same terms as [.abs], except no square root is taken in the last case.
     */
    override val abs2 get() = if (x.isInfinite() || y.isInfinite()) Double.POSITIVE_INFINITY else x * x + y * y

    //================================================================
    // Multiplication and Division
    //----------------------------------------------------------------
    override val timesJ get() = Complex(-y, x)

    override fun timesJ(y: Double) = Complex(-this.y * y, x * y)

    override val timesNegativeJ get() = Complex(y, -x)

    /**
     * {@inheritDoc}
     *
     * This method uses the well-known and straight-forward approach of First Outside Inside Last distribution
     * of terms, then simplifying:
     *
     *  * (a + bi)(c + di)
     *  * ac + adi + bci + bd(i^2)
     *  * ac - bd + adi + bci
     *  * (ac - bd) + i(ad + bc)
     *
     */
    override operator fun times(c: BinaryNumber<out Complex>) = when {
        c.y == 0.0 -> times(c.x)
        y == 0.0 -> c.times(x)
        else -> times(this, c)
    }

    /**
     * Returns the result of dividing this complex value by the imaginary value `i`.
     * Note that diving by `i` is mathematically exactly equivalent to
     * [multiplying by -i][.timesNegativeJ].
     */
    override val divJ get() = timesNegativeJ

    override fun divJ(y: Double): Complex {
        return z(this.y / y, -x / y)
    }

    /**
     * Returns the result of dividing this complex value by the imaginary value `i`.
     * Note that diving by `-i` is mathematically exactly equivalent to
     * [multiplying by i][.timesJ].
     */
    override val divNegativeJ get() = timesJ

    /**
     * Divides this complex number by `c` and returns the result.
     * The algorithm used is from <cite>Smith, R. 1962. Complex division. Comm. ACM 5, 435</cite>:
     *
     *  * (a + bi) / (c + di)
     *  * (a + bi)(c - di) / (c + di)(c - di)  [ Use complex conjugate to make denominator real ]
     *  * [ (ac + bd) + (bc - ad)i ] / (cc + dd)  [ Distribute multiplications ]
     *  * (ac + bd) / (cc + dd) + [ (bc - ad) / (cc + dd) ] i  [ Distribute division ]
     *
     * If `|c| < |d|`, then:
     *
     *  * (ac/d + b) / (cc/d + d) + [ (bc/d - a) / (cc/d + d) ] i  [ Divide everything by d ]
     *  * (au + b) / (cu + d) + [ (bu - a) / (cu + d) ] i  [ Substitute u = c/d ]
     *
     * Otherwise, by the same reasoning and starting from after the division distribution, we have:
     *
     *  * (ac + bd) / (cc + dd) + [ (bc - ad) / (cc + dd) ] i
     *  * (a + bd/c) / (c + dd/c) + [ (b - ad/c) / (c + dd/c) ] i  [ Divide everything by c ]
     *  * (a + bu) / (c + du) + [ (b - au) / (c + du) ] i  [ Substitute u = d/c ]
     *
     * Dividing everything through by the larger term first minimizes the risk of overflow or loss of precision.
     *
     */
    override operator fun div(c: BinaryNumber<out Complex>) = div(this, c)

    //================================================================
    // Logarithmic
    //----------------------------------------------------------------
    // The naive approach is to return
    //     ln(sqrt(x * x + y + y))
    // but this risks an excessive loss of precision for small values.  To prevent this, we rewrite the expression
    // with a substitution of variables.  First, as signs and which component is which do not matter, let's assume
    // that neither value is 0 and name their absolute values a and b, without worrying just yet about which is which.
    // We can then restructure the expression as follows:
    //     log(sqrt(a^2 + b^2))
    //     log(sqrt(b^2 * (a/b)^2 + b^2))  [ a^2 = b^2 * (a/b)^2 ]
    //     log(sqrt(b^2 * u^2 + b^2))      [ defining u = a/b ]
    //     log(sqrt(b^2 * (u^2 + 1)))      [ factoring out b^2 within the sqrt ]
    //     log(b * sqrt(u^2 + 1))          [ moving the b^2 outside the sqrt as b ]
    //     log(b) + log(sqrt(u^2 + 1))     [ log(xy) = log(x) + log(y) ]
    //     log(b) + log((u^2 + 1) ^ 0.5)   [ sqrt(x) = x^0.5 ]
    //     log(b) + 0.5 * log(u^2 + 1)     [ log(x^y) = y * log(x) ]
    //     log(b) + 0.5 * log1p(u^2)       [ logp1(x) = log(1 + x) ]
    // log1p yields its best results when values are small, so assign the smaller value to a to ensure that
    // u = a/b <= 1.
    /**
     * Returns the natural logarithm of this complex number's [absolute value][.abs], `log |z|`.
     */
    private var logabsCache: Double? = null
    override val logabs: Double
        get() {
            val cached = logabsCache
            if (cached != null) return cached

            return when {
                y == 0.0 -> ln(x.absoluteValue)
                x == 0.0 -> ln(y.absoluteValue)
                else -> {
                    val absRe = x.absoluteValue
                    val absIm = y.absoluteValue
                    when {
                        absRe > absIm -> {
                            val u = absIm / absRe
                            ln(absRe) + 0.5 * ln1p(u * u)
                        }
                        else -> {
                            val u = absRe / absIm
                            ln(absIm) + 0.5 * ln1p(u * u)
                        }
                    }
                }
            }.also { logabsCache = it }
        }

    /**
     * {@inheritDoc}
     *
     *
     * All of the logarithmic functions provided here have a branch cut along the negative real axis.
     */
    override val log get() = Complex(logabs, arg)

    /**
     * Returns the logarithm of this complex number in base 2.
     * This convenience method is equivalent to `logN(2)`, but likely to be slightly more efficient.
     * All of the logarithmic functions provided here have a branch cut along the negative real axis.
     */
    val log2 get() = Complex(logabs * LN_2_RECIP, arg * LN_2_RECIP)

    /**
     * Returns the logarithm of this complex number in base 10.
     * This convenience method is equivalent to `logN(10)`, but likely to be slightly more efficient.
     * All of the logarithmic functions provided here have a branch cut along the negative real axis.
     */
    val log10 get() = Complex(logabs * LN_10_RECIP, arg * LN_10_RECIP)

    //================================================================
    // Exponential
    //----------------------------------------------------------------
    /**
     * Returns the natural exponential of this complex number, `e^z`.
     */
    override val exp get() = polar(rexp(x), y)

    /**
     * Returns the base 2 exponential of this complex number, `2^z`.
     */
    val exp2 get() = expln(LN_2)

    /**
     * Returns the base 10 exponential of this complex number, `10^z`.
     */
    val exp10 get() = expln(LN_10)

    /**
     * Returns the base `x` exponential of this complex number, `x^z`.
     */
    fun exp(x: Double) = expln(ln(x))

    /**
     * Returns the base `e^x` exponential of this complex number, `(e^x)^z`.
     */
    fun expln(x: Double): Complex {
        val r = rexp(x * this.x)
        val theta = x * y
        return polar(r, theta)
    }

    /**
     * {@inheritDoc}
     *
     * This function has a branch cut for `z` (this complex number) along the negative real axis.
     */
    override fun pow(a: Double) = when {
        // This incorrectly returns 1 for 0^0, but so does Math.pow, so...
        a == 0.0 -> ONE
        a == 1.0 -> this
        a == -1.0 -> inverse
        x == 0.0 && y == 0.0 -> ZERO
        else -> polar(
            r = rexp(logabs * a),
            theta = arg * a
        )
    }

    /**
     * Returns the result of raising this complex number to the given power, `z^c`.
     */
    override fun pow(c: BinaryNumber<out Complex>): Complex {
        when {
            c.y == 0.0 -> return pow(c.x)
            x == 0.0 && y == 0.0 -> return ZERO
        }
        val logr = logabs
        val theta = arg
        val rho = rexp(logr * c.x - c.y * theta)
        val beta: Double = theta * c.x + c.y * logr
        return polar(rho, beta)
    }

    /**
     * Returns the `n` roots of this complex number, ordered by increasing argument starting from the
     * positive real axis.  Specifically, this returns the distinct solutions `z` for the equation
     * `z^n = c`, where `c` is this complex number.
     *
     * The number of roots to return must be at least one (in which case the list contains `this` complex
     * number as its only element).  The number may not be larger than [.MAX_ROOTS].  Provided both of these
     * conditions are met, this method will always return a list with the appropriate number of entries.
     *
     * If this complex number is [.ZERO], then the return value is a list with zero as its only element.
     * If this complex number is [not-a-number][.isNaN] or [infinite][.isInfinite],
     * then an empty list is returned.
     *
     * @param n the exponent, and therefore also the number of roots to be found
     * @return a list of solutions, as described
     */
    fun roots(n: Int): List<Complex> {
        require(n >= 1) { "n < 1: $n" }
        if (isNaN || isInfinite) return NO_ROOTS
        if (n == 1) return listOf(this)
        require(n <= MAX_ROOTS) { "n=$n > MAX_ROOTS=$MAX_ROOTS" }
        val r = rexp(logabs / n)
        if (r == 0.0) {
            return listOf(ZERO)
        }
        val theta0 = arg / n
        return (0 until n).asSequence()
            .map { x: Int -> x * TWO_PI / n + theta0 }
            .map { theta -> positiveRealBranchCut(theta) }
            .sorted()
            .map { theta -> polar(r, theta) }
            .toList()
    }

    /**
     * Returns the complex square root of this complex number.
     * This convenience method is equivalent to [pow(0.5)][.pow], except that it may
     * be more efficient and/or return more accurate results under certain conditions.
     * This complex function has a branch cut along the negative real axis.
     *
     *
     * The algorithm used comes from <cite>Friedland, P. 1967. Absolute value and square root of a
     * complex number. Comm. ACM 10, 665</cite>:
     *
     *
     *  * c + di = sqrt(a + bi)
     *  * t = sqrt((|a| + hypot(a, b)) / 2)
     *  * If a >= 0:  c = t, d = b / 2t
     *  * Otherwise:  c = |b|/2t, d = sgn(b) * t
     *
     */
    override val sqrt: Complex
        get() {
            val y = this.y.absoluteValue
            if (y == 0.0) {
                return sqrt(x)
            }
            val x = this.x.absoluteValue
            if (x == 0.0) {
                return ZERO
            }
            val t = rsqrt((x + rhypot(x, y)) / 2)
            return if (this.x >= 0.0) {
                Complex(t, this.y / (2 * t))
            } else Complex(y / (2 * t), t.withSign(this.y))
        }

    //================================================================
    // Trigonometric
    //----------------------------------------------------------------
    /**
     * Returns the sine of this complex value.
     */
    override val sin get() = Complex(rsin(x) * rcosh(y), rcos(x) * rsinh(y))

    /**
     * Returns the cosine of this complex value.
     */
    override val cos get() = Complex(rcos(x) * rcosh(y), rsin(x) * rsinh(-y))

    /**
     * Returns the tangent of this complex value.
     */
    override val tan: Complex
        get() {
            val cosRe = rcos(x)
            val sinhIm = rsinh(y)
            val scale = cosRe * cosRe + sinhIm * sinhIm
            if (y.absoluteValue < 1) {
                return Complex(0.5 * rsin(2.0 * x) / scale, 0.5 * rsinh(2.0 * y) / scale)
            }
            var f = cosRe / sinhIm
            f = 1 + f * f
            return Complex(0.5 * rsin(2.0 * x) / scale, 1.0 / (rtanh(y) * f))
        }

    /**
     * Returns the inverse sine of this complex number.
     * This function has a branch cut on the real axis outside the interval `[-1, 1]`.
     * The algorithm used is from <cite>Hull, T. 1997. Implementing the Complex Arcsine and
     * Arccosine Functions Using Exception Handling. Comm. ACM 23, 3</cite>.
     */
    val asin: Complex
        get() {
            val y = this.y.absoluteValue
            if (y == 0.0) return asin(x)
            val x = this.x.absoluteValue
            val r = rhypot(x + 1.0, y)
            val s = rhypot(x - 1.0, y)
            val a = 0.5 * (r + s)
            val b = x / a // 0.5 * (r - s), avoiding risk of cancellation error
            val y2 = y * y
            val zRe = when {
                b <= B_CROSSOVER -> rasin(b)
                x <= 1 -> {
                    val scale = 0.5 * (a + x) * (y2 / (r + (x + 1.0)) + (s + (1.0 - x)))
                    ratan(x / rsqrt(scale))
                }
                else -> {
                    val apx = a + x
                    val scale = 0.5 * (apx / (r + (x + 1.0)) + apx / (s + (x - 1.0)))
                    ratan(x / (y * rsqrt(scale)))
                }
            }
            val zIm = when {
                a <= A_CROSSOVER -> {
                    val am1 = when {
                        x < 1 -> 0.5 * (y2 / (r + (x + 1.0)) + y2 / (s + (1.0 - x)))
                        else -> 0.5 * (y2 / (r + (x + 1.0)) + (s + (x - 1.0)))
                    }
                    ln1p(am1 + rsqrt(am1 * (a + 1.0)))
                }
                else -> ln(a + rsqrt(a * a - 1.0))
            }
            return Complex(zRe.withSign(this.x), zIm.withSign(this.y))
        }

    /**
     * Returns the inverse cosine of this complex value.
     * This function has a branch cut on the real axis outside the interval `[-1, 1]`.
     */
    val acos: Complex
        get() {
            if (y == 0.0) return acos(x)
            val x = this.x.absoluteValue
            val y = this.y.absoluteValue
            val r = rhypot(x + 1.0, y)
            val s = rhypot(x - 1.0, y)
            val a = 0.5 * (r + s)
            val b = x / a
            val y2 = y * y
            val zRe: Double
            zRe = if (b <= B_CROSSOVER) {
                racos(b)
            } else if (x <= 1) {
                val scale = 0.5 * (a + x) * (y2 / (r + x + 1.0) + (s + (1.0 - x)))
                ratan(rsqrt(scale) / x)
            } else {
                val apx = a + x
                val scale = 0.5 * (apx / (r + x + 1.0) + apx / (s + (x - 1.0)))
                ratan(y * rsqrt(scale) / x)
            }
            val zIm = when {
                a <= A_CROSSOVER -> {
                    val am1 = when {
                        x < 1 -> 0.5 * (y2 / (r + (x + 1.0)) + y2 / (s + (1.0 - x)))
                        else -> 0.5 * (y2 / (r + (x + 1.0)) + (s + (x - 1.0)))
                    }
                    ln1p(am1 + rsqrt(am1 * (a + 1.0)))
                }
                else -> ln(a + rsqrt(a * a - 1.0))
            }
            return Complex(zRe.absoluteValue, zIm.absoluteValue)
        }

    /**
     * Returns the inverse tangent for this complex value.
     * This function has a branch cut on the imaginary axis outside the interval `[-i, i]`.
     */
    val atan
        get(): Complex {
            if (y == 0.0) return real(ratan(x))
            val iMinusZ = Complex(-x, -y + 1)
            val iPlusZ = Complex(x, y + 1)
            return iMinusZ.div(iPlusZ).log.times(ONE_OVER_2_I)
        }

    /**
     * Returns the inverse cotangent (or arc-cotangent) of this complex number.
     * This convenience method is equivalent to `[{@link #atan()][.inverse]`.
     */
    val acot get() = inverse.atan

    /**
     * Returns the inverse secant (or arc-secant) of this complex number.
     * This convenience method is equivalent to `[.inverse].[.acos]`.
     */
    val asec get() = inverse.acos

    /**
     * Returns the inverse cosecant (or arc-cosecant) of this complex number.
     * This convenience method is equivalent to `[.inverse].[.asin]`.
     */
    val acsc get() = inverse.asin

    //================================================================
    // Hyperbolic
    //----------------------------------------------------------------
    /**
     * Returns the hyperbolic sine of this complex number.
     */
    override val sinh get() = Complex(rsinh(x) * rcos(y), rcosh(x) * rsin(y))

    /**
     * Returns the hyperbolic cosine of this complex number.
     */
    override val cosh get() = Complex(rcosh(x) * rcos(y), rsinh(x) * rsin(y))

    /**
     * Returns the hyperbolic tangent of this complex number.
     */
    override val tanh: Complex
        get() {
            val cosIm = rcos(y)
            val sinhRe = rsinh(x)
            val scale = cosIm * cosIm + sinhRe * sinhRe
            if (x.absoluteValue < 1.0) {
                return Complex(sinhRe * rcosh(x) / scale, 0.5 * rsin(2.0 * y) / scale)
            }
            var f = cosIm / sinhRe
            f = 1.0 + f * f
            return Complex(1 / (rtanh(x) * f), 0.5 * rsin(2.0 * y) / scale)
        }

    /**
     * {@inheritDoc}
     *
     * This implementation uses [.tanh] and [.inverse] instead, because its [.tanh]
     * implementation has better precision.
     */
    override val coth get() = tanh.inverse

    //================================================================
    // Inverse Hyperbolic
    //----------------------------------------------------------------
    // Note that "inverse" here is in reference to inverting the hyperbolic
    // function, not a multiplicative inverse as for the inverse() method.
    // The overloading of this term is admittedly unhelpful...
    //----------------------------------------------------------------
    /**
     * Returns the inverse hyperbolic sine of this complex number.
     * This function has a branch cut on the imaginary axis outside the range `[-i, i]`.
     */
    val asinh get() = timesJ.asin.timesNegativeJ

    /**
     * Returns the inverse hyperbolic cosine of this complex number.
     * This function has a branch cut for values less than 1 on the real axis.
     */
    val acosh: Complex
        get() {
            if (y == 0.0) return acosh(x)
            val z = acos
            return if (z.y > 0) z.timesNegativeJ else z.timesJ
        }

    /**
     * Returns the inverse hyperbolic tangent of this complex number.
     * This function has a branch cut on the real axis outside the range `[-1, 1]`.
     */
    val atanh get() = timesJ.atan.timesNegativeJ

    /**
     * Returns the inverse hyperbolic cotangent of this complex number.
     * This convenience method is equivalent to `[.inverse].[.tanh]`.
     */
    val acoth get() = inverse.tanh

    /**
     * Returns the inverse hyperbolic secant of this complex number.
     * This convenience method is equivalent to `[.inverse].[.cosh]`.
     */
    val asech get() = inverse.cosh

    /**
     * Returns the inverse hyperbolic cosecant of this complex number.
     * This convenience method is equivalent to `[.inverse].[.cosh]`.
     */
    val acsch get() = inverse.sinh

    /**
     * Projects this complex number onto a Riemann Sphere, in a strict sense that meets the normal
     * mathematical definition for this concept.  Specifically, this is the same as [.cproj],
     * except that there is only one infinity `(+infinity, 0)` and only one zero `(0, 0)`
     * because the signs are discarded for values that meet these conditions.  However, if neither of
     * the mapping conditions is met, then negative zeroes are preserved.
     *
     *  1. If either the real or imaginary component is infinite, then [.POSITIVE_RE_INFINITY] is returned
     *  1. If both the real and imaginary components are zero (with sign ignored), then [.ZERO] is returned
     *  1. Otherwise, this complex number is returned unmodified
     *
     */
    fun proj() = when {
        x.isInfinite() || y.isInfinite() -> POSITIVE_RE_INFINITY
        x == 0.0 && y == 0.0 -> ZERO
        else -> this
    }

    /**
     * Projects this complex number onto a Riemann Sphere, in the same sense as for the
     * `cproj(3)` C function.  Specifically, this is the same as the mathematical
     * definition of the Riemann Sphere, except that it has two infinities and 4 zeros
     * due the preservation of the signs.
     *
     *  1. If either the real or imaginary component is infinite, then the result is a new complex number with
     * positive infinity as its real component and either positive or negative `0` as its imaginary
     * component, preserving the existing sign from [.y].
     *  2. Otherwise, this complex number is returned unmodified
     *
     */
    fun cproj() = when {
        x.isInfinite() || y.isInfinite() -> Complex(Double.POSITIVE_INFINITY, 0.0.withSign(y))
        else -> this
    }

    override fun toString() = when {
        y.isNaN() -> "($x ${y}i)"
        y < 0 -> "($x${y}i)"
        else -> "($x+${y}i)"
    }
}